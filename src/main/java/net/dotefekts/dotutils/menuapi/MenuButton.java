package net.dotefekts.dotutils.menuapi;

import org.bukkit.inventory.ItemStack;

public class MenuButton {
	
	private ButtonListener listener;
	private ItemStack item;
	private Menu menu;
	private int pos;
	
	//MenuButton(ItemStack item, Menu menu, int pos) {
	//}
	
	MenuButton(ItemStack item, Menu menu, int pos, ButtonListener listener) {
		//this(item, menu, pos);
		this.item = item;
		this.menu = menu;
		this.pos = pos;
		this.listener = listener;
	}
	
	void buttonClicked() {
		if(listener != null)
			listener.buttonClicked(menu, this);
	}
	
	ButtonListener getListener() {
		return listener;
	}
	
	public ItemStack getItem() {
		return item.clone();
	}
	
	public int getPos() {
		return pos;
	}
	
	public void setListener(ButtonListener listener) {
		this.listener = listener;
	}
	
	public void setItem(ItemStack item) {
		this.item = item;
		try {
			menu.updateButton(this);
		} catch (InternalMenuException e) {
			e.printStackTrace();
		}
	}
	
	public Menu getParent() {
		return menu;
	}
}
