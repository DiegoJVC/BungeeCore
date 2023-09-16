package com.cobelpvp.bungeecore.reconnect;

import com.cobelpvp.bungeecore.BungeeCore;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command {

    private final BungeeCore plugin;

    public ReloadCommand(BungeeCore plugin) {
        super("reconnect", "reconnect.admin");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(new TextComponent("Please use /reconnect reload."));
        }
        switch (args[0]) {
            case "reload":
                plugin.loadConfig();
        }
    }
}