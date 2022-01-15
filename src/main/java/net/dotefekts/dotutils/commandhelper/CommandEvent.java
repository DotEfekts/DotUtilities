package net.dotefekts.dotutils.commandhelper;

import org.bukkit.command.CommandSender;

public record CommandEvent(CommandSender sender, Command command, String[] args) { }
