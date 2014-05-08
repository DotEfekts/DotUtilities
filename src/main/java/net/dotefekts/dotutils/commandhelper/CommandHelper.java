package net.dotefekts.dotutils.commandhelper;


import net.dotefekts.dotutils.DotUtilities;

import org.bukkit.command.PluginCommand;

public class CommandHelper {
	private CommandManager manager;
	private DotUtilities plugin;
	private CommandRegistration register;
	private CommandListener listener;
	private static CommandHelper instance;
	
	private CommandHelper(DotUtilities plugin){
		this.plugin = plugin;
		manager = new CommandManager(this);
		listener = new CommandListener(manager);
		register = new CommandRegistration(plugin);		
	}
	
	PluginCommand getCommand(String command){
		return plugin.getCommand(command);
	}
	
	void registerCommand(String command){
		register.registerCommand(command, listener);
		
	}
	
	public static CommandManager get(DotUtilities plugin){
		if(instance == null)
			instance = new CommandHelper(plugin);
		return instance.manager;
	}
}
