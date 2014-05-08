package net.dotefekts.dotutils.commandhelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.dotefekts.dotutils.DotUtilities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {
	private CommandManager manager;

	CommandListener(CommandManager manager) {
		this.manager = manager;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command cmd, String label, String[] args) {
		Command command = manager.matchCommand(cmd.getName(), args, !(commandSender instanceof Player));
		if(command != null){
			int len = command.getSubcommand().length;
			String[] newArgs = new String[args.length - len];
			
			for(int i = len; i < args.length; i++)
				newArgs[i - len] = args[i];

			if(!command.isServerCommand())
				if(!command.getPermission().hasPermission((Player)commandSender)){
					commandSender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
					return true;
				}
			
			if(!command.getFormat().isEmpty()){	
				if(command.getFormat().equalsIgnoreCase("n") && args.length > 0) {
					commandSender.sendMessage(ChatColor.RED + "Error, too many arguments provided.");
					return true;
				}
				String[] format = command.getFormat().toLowerCase().split(" ");
				boolean optional = false;
				if(format.length > 0)
				if(!format[0].equalsIgnoreCase("n"))
				for(int i = 0; i < format.length || i < newArgs.length; i++){
					if(i < newArgs.length && i < format.length){
						if(format[i].startsWith("i")){
							try {
								Integer.parseInt(newArgs[i]);
							} catch(NumberFormatException e) {
								commandSender.sendMessage(ChatColor.RED + "Error, the argument " + newArgs[i] + " must be a valid whole number.");
								return true;
							}
						} else if(format[i].startsWith("d")){
							try {
								Double.parseDouble(newArgs[i]);
							} catch(NumberFormatException e) {
								commandSender.sendMessage(ChatColor.RED + "Error, the argument " + newArgs[i] + " must be a valid decimal number.");
								return true;
							}
						} else if(format[i].startsWith("p")){
							if(DotUtilities.getUUID(newArgs[i]) == null) {
								commandSender.sendMessage(ChatColor.RED + "Error, the argument " + newArgs[i] + " must be an online player.");
								return true;
							}
						}
						
						if(format[i].substring(1, 1).equals("["))
							optional = true;
					} else {
						if(format.length > newArgs.length && optional == false) {
							if(!format[i].substring(1, 2).equals("[") && !format[i].equals("...")) {
								commandSender.sendMessage(ChatColor.RED + "Error, not enough arguments provided.");
								return true;
							}
						} else if(format.length < newArgs.length && !(format[format.length - 1].equalsIgnoreCase("..."))) {
							commandSender.sendMessage(ChatColor.RED + "Error, too many arguments provided.");
							return true;
						} else {
							break;
						}
					}
				}
			}
			
			try {
				command.getExecutor().invoke(command.getListener(), new CommandEvent(commandSender, command, newArgs));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			List<String> list = getSublist(args, !(commandSender instanceof Player), cmd);
			if(list.size() != 0) 
				for(String str : list)
					commandSender.sendMessage(str);
			else if(manager.matchCommand(cmd.getName(), args, (commandSender instanceof Player)) != null || getSublist(args, (commandSender instanceof Player), cmd).size() != 0)
				if(commandSender instanceof Player)
					commandSender.sendMessage(ChatColor.RED + "This is a server command. It cannot be run as a player.");
				else
					commandSender.sendMessage(ChatColor.RED + "This is a player command. It cannot be run as the server.");
			
		}
		return true;
	}
	
	private List<String> getSublist(String[] args, boolean server, org.bukkit.command.Command cmd) {
		List<String> list = new ArrayList<String>();
		String[] argC = args.clone();
		List<Command> subs = manager.matchCommands(cmd.getName(), server);
		List<String> direct;
		for(int i = args.length; i > -1; i--){
			direct = SubmenuManager.generateSubmenu(subs, argC);
			if(direct != null)
			if(direct.size() != 1){
				if(i == args.length) {
					return direct;
				} else {
					list.add(ChatColor.RED + "Invalid subcommand.");
					return list;
				}
			} else {
				argC = new String[argC.length - 1];
				for(int x = 0; x < argC.length; x++)
					argC[x] = args[x];
			}
		}
		return list;
	}
	
}
