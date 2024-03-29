package net.dotefekts.dotutils.menuapi;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class MenuListener implements Listener {
	private final Plugin plugin;
	private final MenuManager manager;
	
	MenuListener(Plugin plugin, MenuManager manager) {
		this.plugin = plugin;
		this.manager = manager;
	}
	
	@EventHandler
	public void inventoryClick(InventoryClickEvent event) throws InternalMenuException {
		LookupContainer cont = manager.lookup(event.getClickedInventory(), event.getSlot());
		if(cont != null){
			if(cont.hasListener())
				Bukkit.getScheduler().runTask(plugin, new ButtonRunnable(cont.getMenu(), cont.getButton()));
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void inventoryDrag(InventoryDragEvent event) {
		if(manager.isMenu(event.getInventory()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent event) throws InternalMenuException {
		manager.destroyPlayerMenus(event.getPlayer());
	}
}
