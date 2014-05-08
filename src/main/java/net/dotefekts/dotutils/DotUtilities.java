package net.dotefekts.dotutils;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import net.dotefekts.dotutils.commandhelper.CommandHelper;
import net.dotefekts.dotutils.commandhelper.CommandManager;
import net.dotefekts.dotutils.menuapi.MenuAPI;
import net.dotefekts.dotutils.menuapi.MenuManager;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DotUtilities extends JavaPlugin implements Listener {
	private static MenuManager menuAPI;
	private static CommandManager commandHelper;
	private static HashMap<UUID, String> uuids;
	
	static {
		uuids = new HashMap<UUID, String>();
	}
	
	@Override
	public void onEnable(){
		DotUtilities.menuAPI = MenuAPI.get(this);
		DotUtilities.commandHelper = CommandHelper.get(this);
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	public static MenuManager getMenuManager(){
		return menuAPI;
	}
	
	public static CommandManager getCommandHelper() {
		return commandHelper;
	}
	
	@EventHandler
	public void playerLogin(PlayerLoginEvent event) {
		uuids.put(event.getPlayer().getUniqueId(), event.getPlayer().getName());
	}
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent event) {
		uuids.remove(event.getPlayer().getUniqueId());
	}
	
	public static UUID getUUID(String string) {
		for(Entry<UUID, String> ent : uuids.entrySet())
			if(ent.getValue().equalsIgnoreCase(string))
				return ent.getKey();
		return null;
	}
	
	public static String joinArray(String[] arr, String seperator){
		String result = "";
		for(String str : arr)
			if(result.isEmpty())
				result = str;
			else
				result = result + seperator + str;
		return result;
	}
}
