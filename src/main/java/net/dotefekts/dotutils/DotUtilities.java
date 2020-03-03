package net.dotefekts.dotutils;

import net.dotefekts.dotutils.commandhelper.CommandHelper;
import net.dotefekts.dotutils.commandhelper.CommandManager;
import net.dotefekts.dotutils.menuapi.MenuAPI;
import net.dotefekts.dotutils.menuapi.MenuManager;

import org.bukkit.plugin.java.JavaPlugin;

public class DotUtilities extends JavaPlugin {
	private static MenuManager menuAPI;
	private static CommandManager commandHelper;
	
	@Override
	public void onEnable(){
		DotUtilities.menuAPI = MenuAPI.get(this);
		DotUtilities.commandHelper = CommandHelper.get();
		
		getLogger().info("DotUtilities loaded.");
	}
	
	public static MenuManager getMenuManager(){
		return menuAPI;
	}
	
	public static CommandManager getCommandHelper() {
		return commandHelper;
	}
}
