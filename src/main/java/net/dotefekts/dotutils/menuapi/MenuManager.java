package net.dotefekts.dotutils.menuapi;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MenuManager {
	private ArrayList<Menu> activeMenus;

	MenuManager() {
		activeMenus = new ArrayList<Menu>();
	}
	
	public Menu createMenu(Player player, int size, String title){
		return new Menu(player, size, title, this);
	}
	
	void registerMenu(Menu menu) {
		activeMenus.add(menu);
	}
	
	void deregisterMenu(Menu menu) {
		if(activeMenus.contains(menu))
			activeMenus.remove(menu);
	}

	LookupContainer lookup(Inventory inventory, int slot) {
		if(slot >= 0 && slot < inventory.getSize() ) {
			for(Menu menu : activeMenus) {
				if(menu.compareInventory(inventory)) {
					return new LookupContainer(menu.getButton(slot), menu);
				}
			}
		}
		return null;
	}
	
	public Menu findMenu(Inventory inventory) {
		for(Menu menu : activeMenus)
			if(menu.compareInventory(inventory))
				return menu;
		return null;
	}
	
	public boolean isMenu(Inventory inventory){
		for(Menu menu : activeMenus)
			if(menu.compareInventory(inventory))
				return true;
		return false;
	}

	void destroyPlayerMenus(Player player) {
		ArrayList<Menu> destruction = new ArrayList<Menu>();
		for(Menu menu : activeMenus)
			if(menu.getPlayer().equals(player))
				destruction.add(menu);
		for(Menu menu : destruction) menu.markDestruction();
	}
}

class LookupContainer {
	private MenuButton button;
	private Menu menu;
	private boolean hasListener;
	
	LookupContainer(MenuButton button, Menu menu){
		this.button = button;
		this.hasListener = !(button.getListener() == null);
		this.menu = menu;
	}
	
	MenuButton getButton() {
		return button;
	}
	
	Menu getMenu() {
		return menu;
	}
	
	boolean hasListener() {
		return hasListener;
	}
}
