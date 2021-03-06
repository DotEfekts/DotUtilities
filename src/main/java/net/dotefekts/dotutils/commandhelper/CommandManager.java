package net.dotefekts.dotutils.commandhelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager {
	private HashMap<String, JavaPlugin> commandLookup;
	private HashMap<String, CommandCompleter> tabCompleters;
	private CommandHelper helper;
	private SubmenuManager submanager;
	private Permission defPermission = new Permission("", "The default permission. Anyone can use this command.", PermissionDefault.TRUE);
	
	CommandManager(CommandHelper helper) {
		this.helper = helper;
		commandLookup = new HashMap<String, JavaPlugin>();
		tabCompleters = new HashMap<String, CommandCompleter>();
		submanager = new SubmenuManager();
	}
	
	public void registerCommands(Listener listener, JavaPlugin plugin) {
		int intMethods = 0;
		int intCommands = 0;
		Class<? extends Listener> listenerClass = listener.getClass();
		for(Method method : listenerClass.getDeclaredMethods()){
			Permission permission = null;
			
			if(method.isAnnotationPresent(PermissionHandler.class)) {
				PermissionHandler handler = method.getAnnotation(PermissionHandler.class);
				permission = new Permission(handler.node(), handler.description(), handler.permissionDefault());
			}
			
			if(method.isAnnotationPresent(CommandHandlers.class)) {
				if(method.getReturnType() != boolean.class) {
					Bukkit.getLogger().warning("[CommandHelper] Tried to register " + method.getName() + " but had a return type that was not boolean.");
					continue;
				}
					
				if(method.getParameterTypes().length == 1) {
					if(method.getParameterTypes()[0].equals(CommandEvent.class)) {
						CommandHandler[] annos = method.getAnnotation(CommandHandlers.class).value();
						ArrayList<Command> newCommands = new ArrayList<Command>();
						HashMap<String, JavaPlugin> newCommandLookup = new HashMap<String, JavaPlugin>();
						
						String command = "";
						String description = "";
						String format = "";
						boolean serverCommand = false;
					
						for(CommandHandler handler : annos) {
							
							if(checkValidSilent(handler.command(), method.getName()))
								command = handler.command();
							else if(command.isEmpty())
								if(!checkValid(handler.command(), method.getName()))
									continue;
							
							if(!handler.description().isEmpty())
								description = handler.description();
								
							if(checkFormat(handler.format()))
								if(!handler.format().isEmpty())
									format = handler.format();
								else;
							else {
								Bukkit.getLogger().warning("[CommandHelper] Format error on command " + handler.command());
								continue;
							}
								
							serverCommand = handler.serverCommand();
								
							newCommands.add(new Command(command, description, format, serverCommand, null, method, listener));
							intCommands++;
							if(!newCommandLookup.containsKey(command))
								newCommandLookup.put(command, plugin);
						}
					
						if(permission != null)
							for(Command cmd : newCommands)
								cmd.setPermission(permission);
						else
							for(Command cmd : newCommands)
								cmd.setPermission(defPermission);
						
						for(Command cmd : newCommands){
							registerCommand(cmd, plugin);
						}

						submanager.addList(newCommands);
						commandLookup.putAll(newCommandLookup);
						intMethods++;
					} else {
						Bukkit.getLogger().warning("[CommandHelper] Parameter type for method " + method.getName() + " incorrect for CommandHandler");
					}
				} else {
					Bukkit.getLogger().warning("[CommandHelper] Number of parameters for method " + method.getName() + " incorrect.");
				}
			} else if(method.isAnnotationPresent(CommandHandler.class)){
				if(method.getReturnType() != boolean.class) {
					Bukkit.getLogger().warning("[CommandHelper] Tried to register " + method.getName() + " but had a return type that was not boolean.");
					continue;
				}
				
				CommandHandler handler = method.getAnnotation(CommandHandler.class);
				
				if(checkValid(handler.command(), method.getName())){
					
					if(!checkFormat(handler.format())) {
						Bukkit.getLogger().warning("[CommandHelper] Format error on command " + handler.command());
						continue;
					}
					
					Command command = new Command(handler.command(), handler.description(), handler.format(), handler.serverCommand(), null, method, listener);
					submanager.addSubcommand(command);
					
					intCommands++;
					commandLookup.put(handler.command(), plugin);
				
					if(permission != null)
						command.setPermission(permission);
					else
						command.setPermission(defPermission);
						
					registerCommand(command, plugin);
					intMethods++;
				}
			}
		}
		Bukkit.getLogger().info("[CommandHelper] " + intCommands + " commands registered to " + intMethods + " methods.");
	}
	
	private boolean checkValidSilent(String command, String name) {
		if(command.isEmpty())
			return false;
		else if(commandLookup.containsKey(command))
			return false;
		else if(helper.getCommand(command.split(" ")[0]) != null)
			if(!(helper.getCommand(command.split(" ")[0]).getTabCompleter() instanceof CommandCompleter))
				return false;
		return true;
	}

	private boolean checkFormat(String format) {
		String[] arr = format.split(" ");
		
		for(String str : arr){
			if(!str.isEmpty())
			if(str.equalsIgnoreCase("n"))
				if(arr.length != 1)
					return false;
				else;
			else if(str.length() < 3)
				return false;
			else {
				switch(str.substring(1, 2)) {
					case "[":
						if(!str.substring(str.length() - 1, str.length()).equalsIgnoreCase("]"))
							return false;
						break;
					case "<":
						if(!str.substring(str.length() - 1, str.length()).equalsIgnoreCase(">"))
							return false;
						break;
					case ".":
						if(!str.equalsIgnoreCase("..."))
							return false;
						break;
				}
				
				switch(str.substring(0, 1)) {
					case "i":
					case "d":
					case "p":
					case "s":
					case ".":
						break;
					default:
						return false;
				}
			}
		}
		return true;
	}

	ArrayList<Command> matchCommands(String command, boolean server) {
		ArrayList<Command> cmdList = new ArrayList<Command>();
		for(Command cmd : submanager.getSubcommands(command))
			if(cmd.isServerCommand() == server)
				cmdList.add(cmd);
		return cmdList;
	}
	
	Command matchCommand(String command, String[] args, boolean server){
		Command matched = null;
		int len = -1;
		for(Command cmd : matchCommands(command, server)){
			if(matchArgs(cmd.getSubcommand(), args) && len < cmd.getSubcommand().length) {
				matched = cmd;
				len = cmd.getSubcommand().length;
			}
		}
		return matched;
	}
	
	private boolean matchArgs(String[] subcommand, String[] args) {
		if(args.length < subcommand.length)
			return false;
		else for(int i = 0; i < subcommand.length; i++)
			if(!subcommand[i].equalsIgnoreCase(args[i]))
				return false;
		return true;
	}

	private void registerCommand(Command cmd, JavaPlugin plugin) {
		String cmdString = cmd.getCommand().split(" ")[0];
		PluginCommand pCmd = helper.registerCommand(cmdString, plugin);
		cmdString = pCmd.getName();
		if(cmd.getSubcommand().length == 0) {
			if(!cmd.getDescription().isEmpty())
				pCmd.setDescription(cmd.getDescription());
			else
				pCmd.setDescription("This command does not have a description set.");
			
			if(!cmd.getFormat().isEmpty())
				pCmd.setUsage("/" + cmdString + " " + cmd.getParsedFormat());
			else
				pCmd.setUsage("A usage has not been set for this command.");	
		} else {
			if(pCmd.getDescription().isEmpty() || pCmd.getDescription() == null)
				pCmd.setDescription("This command is used to access a submenu.");
			if(pCmd.getUsage().isEmpty() || pCmd.getUsage() == null)
				pCmd.setUsage("/" + cmdString + " " + "<Subcommand> ...");
		}
		
		if(pCmd.getTabCompleter() == null) {
			CommandCompleter completer;
			completer = new CommandCompleter(cmdString, this);						
			pCmd.setTabCompleter(completer);
			tabCompleters.put(cmdString, completer);
		}
		
		if(Bukkit.getHelpMap().getHelpTopic(pCmd.getName()) == null)
			Bukkit.getHelpMap().addTopic(new GenericCommandHelpTopic(pCmd));
	}

	private boolean checkValid(String command, String method) {
		if(command.isEmpty()) {
			Bukkit.getLogger().warning("[CommandHelper] No command supplied for the method " + method);
			return false;
		} else if(commandLookup.containsKey(command)) {
			Bukkit.getLogger().warning("[CommandHelper] Method " + method+ " tried to register a command registered to another method.");
			return false;
		} else if(helper.getCommand(command.split(" ")[0]) != null)
			if(!(helper.getCommand(command.split(" ")[0]).getTabCompleter() instanceof CommandCompleter)) {
				Bukkit.getLogger().warning("[CommandHelper] Method " + method + " tried to register a command registered to another plugin.");
				return false;
			} else;
		return true;
	}

	SubmenuManager getSubmanager() {
		return submanager;		
	}
}
