package net.dotefekts.dotutils.commandhelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

class CommandRegistration {
	
	PluginCommand registerCommand(String command, CommandExecutor executor, JavaPlugin plugin){
		if(plugin.getCommand(command) == null) 
			if(!getCommandMap().register(plugin.getName().toLowerCase().replace(' ', '_'), getCommand(command, plugin)))
				Bukkit.getLogger().warning("Fallback prefix registered for " + command + ".");
		
		PluginCommand cmd = plugin.getCommand(command);
		if(cmd != null)
			cmd.setExecutor(executor);
		
		return cmd;
	}
	
	private static CommandMap getCommandMap() {
		CommandMap commandMap = null;
	 
		try {
			if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
				Field f = SimplePluginManager.class.getDeclaredField("commandMap");
				f.setAccessible(true);
	 
				commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
			}
		} catch (NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException e) {
			e.printStackTrace();
		}
	 
		return commandMap;
	}
	
	private static PluginCommand getCommand(String name, Plugin plugin) {
		PluginCommand command = null;
	 
		try {
			Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			c.setAccessible(true);
	 
			command = c.newInstance(name, plugin);
		} catch (SecurityException | InvocationTargetException | IllegalArgumentException |
				IllegalAccessException | InstantiationException | NoSuchMethodException e) {
			e.printStackTrace();
		}

		return command;
	}
}
