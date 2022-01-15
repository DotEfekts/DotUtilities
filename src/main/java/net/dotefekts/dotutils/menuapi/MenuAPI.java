package net.dotefekts.dotutils.menuapi;

import net.dotefekts.dotutils.DotUtilities;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class MenuAPI {
	private final MenuManager manager;
	private static MenuAPI instance;
	
	private MenuAPI(Plugin plugin){
		manager = new MenuManager();
		MenuListener listener = new MenuListener(plugin, manager);
		Bukkit.getPluginManager().registerEvents(listener, plugin);
	}
	
	public static MenuManager get(DotUtilities plugin) {
		if(instance == null)
			instance = new MenuAPI(plugin);
		return instance.manager;
	}
}
