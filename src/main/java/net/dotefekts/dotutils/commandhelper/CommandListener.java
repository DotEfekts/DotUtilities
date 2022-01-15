package net.dotefekts.dotutils.commandhelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ClassCanBeRecord")
public class CommandListener implements CommandExecutor {
	private final CommandManager manager;

	CommandListener(CommandManager manager) {
		this.manager = manager;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.Command cmd, @NotNull String label, String[] args) {
		Command command = manager.matchCommand(cmd.getName(), args, !(commandSender instanceof Player));
		if(command != null) {
			if(!command.isServerCommand()) {
				assert commandSender instanceof Player;
				if(!commandSender.hasPermission(command.getPermission())) {
					commandSender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
					return true;
				}
			}

			int len = command.getSubcommand().length;
			String[] newArgs = new String[args.length - len];

			if (args.length - len >= 0)
				System.arraycopy(args, len, newArgs, 0, args.length - len);

			var format = command.getFormat();
			if(format.length > 0) {
				boolean optional = false;
				for(int i = 0; i < format.length || i < newArgs.length; i++) {
					if(i < newArgs.length && i < format.length) {
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
							if(Bukkit.getPlayer(newArgs[i]) == null) {
								commandSender.sendMessage(ChatColor.RED + "Error, the argument " + newArgs[i] + " must be an online player.");
								return true;
							}
						}

						if(format[i].charAt(1) == '[')
							optional = true;
					} else {
						if(format.length > newArgs.length && !optional) {
							if(format[i].charAt(1) != '[' && !format[i].equals("...")) {
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
			} else if(newArgs.length > 0) {
				commandSender.sendMessage(ChatColor.RED + "Error, too many arguments provided.");
				return true;
			}

			try {
				return (Boolean) command.getExecutor().invoke(command.getCommandHandler(), new CommandEvent(commandSender, command, newArgs));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				commandSender.sendMessage(ChatColor.RED + "An exception occurred during execution of the command. You should let the server owner know about this.");
				return true;
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
		List<String> list = new ArrayList<>();
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
				System.arraycopy(args, 0, argC, 0, argC.length);
			}
		}
		return list;
	}

}
