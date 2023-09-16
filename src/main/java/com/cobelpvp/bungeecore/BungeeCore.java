package com.cobelpvp.bungeecore;

import com.cobelpvp.bungeecore.commands.maintenanace.MaintenanceCommand;
import com.cobelpvp.bungeecore.listeners.MOTDListener;
import com.cobelpvp.bungeecore.listeners.MaintenanceListener;
import com.cobelpvp.bungeecore.reconnect.PlayerListener;
import com.cobelpvp.bungeecore.reconnect.ReloadCommand;
import com.cobelpvp.bungeecore.utils.ConfigHelper;
import com.cobelpvp.bungeecore.commands.hub.HubCommand;
import com.cobelpvp.bungeecore.commands.motd.MOTDCommand;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

@Getter @Setter
public class BungeeCore extends Plugin {

    @Getter
    private static BungeeCore plugin;

    // Maintenance
    @Getter public static Boolean serverInMaintenance;

    // Config's
    private ConfigHelper motd;
    private ConfigHelper maintenance;
    private Configuration config;

    @Override
    public void onEnable() {
        plugin = this;
        loadConfig();

        /**
         * Plugin manager for register everything
         */
        PluginManager pluginManager = getProxy().getPluginManager();

        /**
         * Register the Configs
         */
        motd = new ConfigHelper(this, "motd.yml", true);
        maintenance = new ConfigHelper(this, "maintenance.yml", true);

        /**
         * Register the listeners
         */
        pluginManager.registerListener(this, new MaintenanceListener(this));
        pluginManager.registerListener(this, new MOTDListener(this));
        pluginManager.registerListener(this, new PlayerListener(this));

        /**
         * Register the commands
         */
        pluginManager.registerCommand(this, new MaintenanceCommand(this));
        pluginManager.registerCommand(this, new MOTDCommand(this));
        pluginManager.registerCommand(this, new HubCommand());
        pluginManager.registerCommand(this, new ReloadCommand(this));
    }

    public void loadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(loadResource("config.yml"));
        } catch (IOException e) {
            this.getLogger().log(Level.SEVERE, "Exception while reading config", e);
        }
    }

    public File loadResource(String resource) {
        File folder = this.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = this.getResourceAsStream(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            this.getLogger().log(Level.SEVERE, "Exception while writing default config", e);
        }
        return resourceFile;
    }

    @Override
    public void onDisable() {
        /**
         * Save the config
         */
        motd.save();
        maintenance.save();
        config = null;
    }

    public Configuration getConfig() {
        return this.config;
    }
}
