package net.dotefekts.dotutils.menuapi;

public class ButtonRunnable implements Runnable {
	
	private MenuButton button;
	private Menu menu;

	ButtonRunnable(Menu menu, MenuButton button) {
		this.button = button;
		this.menu = menu;
	}
	
	@Override
	public void run() {
		if(button.getListener().buttonClicked(menu, button))
			menu.getPlayer().closeInventory();
	}

}
