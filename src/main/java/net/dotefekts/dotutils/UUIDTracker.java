package net.dotefekts.dotutils;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UUIDTracker implements Listener {
	private HashMap<UUID, String> uuids;
	private HashMap<String, UUID> playerNames;
	
	public UUIDTracker() {
		uuids = new HashMap<UUID, String>();
		playerNames = new HashMap<String, UUID>();
	}
	
	@EventHandler
	public void playerLogin(PlayerLoginEvent event) {
		uuids.put(event.getPlayer().getUniqueId(), event.getPlayer().getName());
		playerNames.put(event.getPlayer().getName(), event.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent event) {
		uuids.remove(event.getPlayer().getUniqueId());
		playerNames.remove(event.getPlayer().getName());
	}
	
	public UUID getUUID(String name) {
		return playerNames.get(name);
	}
	
	public String getName(UUID uuid) {
		return uuids.get(uuid);
	}
}
