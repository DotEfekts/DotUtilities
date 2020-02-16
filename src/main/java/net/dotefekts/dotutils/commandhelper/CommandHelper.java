package net.dotefekts.dotutils.commandhelper;


import java.util.HashMap;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHelper {
	private CommandManager manager;
	private CommandRegistration register;
	private CommandListener listener;
	private HashMap<String, JavaPlugin> commandSource;
	private static CommandHelper instance;
	
	private CommandHelper(){
		manager = new CommandManager(this);
		listener = new CommandListener(manager);
		register = new CommandRegistration();
		commandSource = new HashMap<String, JavaPlugin>();
	}
	
	PluginCommand getCommand(String command){
		if(commandSource.containsKey(command))
			return commandSource.get(command).getCommand(command);
		else
			return null;
	}
	
	void registerCommand(String command, JavaPlugin plugin){
		PluginCommand cmd = register.registerCommand(command, listener, plugin);
		commandSource.put(cmd.getLabel(), plugin);
	}
	
	public static CommandManager get(){
		if(instance == null)
			instance = new CommandHelper();
		return instance.manager;
	}
}
