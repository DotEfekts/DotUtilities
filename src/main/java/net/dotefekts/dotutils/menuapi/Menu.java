package net.dotefekts.dotutils.menuapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Menu {
	private Inventory inv;
	private MenuButton[] buttons;
	private MenuManager manager;
	private boolean exists = true;
	
	Menu(Player player, int size, String title) {
		int mod = size % 9;
		if(mod != 0){
			int sizePre = size;
			size = mod < 4.5 ? size - mod : size + (9 - mod);
			Bukkit.getLogger().warning("A menu was created with " + sizePre + " slots."
					+ " The number of slots must be divisible by 9. The number has been corrected to " + size);
		}
		inv = Bukkit.createInventory(player, size, title);
		buttons = new MenuButton[size];
		manager.registerMenu(this);
	}
	
	public MenuButton setButton(ItemStack item, int x, int y, ButtonListener listener) {
		int pos = (x * 9) + y;		
		MenuButton button = new MenuButton(item, this, pos, listener);
		buttons[pos] = button;
		inv.setItem(button.getPos(), button.getItem());
		return button;
	}
	
	MenuButton getButton(int pos){
		if(pos < buttons.length)
			return buttons[pos];
		else return null;
	}
	
	void updateButton(MenuButton button) throws InternalMenuException {
		if(buttons[button.getPos()] != button)
			throw new InternalMenuException("Supplied button does not match button in menu.");
		else inv.setItem(button.getPos(), button.getItem());
	}
	
	public void showMenu() throws InternalMenuException {
		if(exists)
			((Player)inv.getHolder()).openInventory(inv);
		else throw new InternalMenuException("A menu that was marked for destruction was asked to be shown to a player."); 
	}
	
	public boolean getExists() {
		return exists;
	}
	
	public Player getPlayer() {
		return (Player)inv.getHolder();
	}
	
	public void markDestruction() {
		manager.deregisterMenu(this);
		exists = false;
	}

	public boolean compareInventory(Inventory inventory) {
		return inventory.equals(inv);
	}
}
