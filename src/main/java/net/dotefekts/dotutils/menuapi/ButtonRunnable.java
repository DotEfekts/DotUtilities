package net.dotefekts.dotutils.menuapi;

public class ButtonRunnable implements Runnable {
	
	private ButtonListener listener;
	private Menu menu;

	ButtonRunnable(Menu menu, ButtonListener listener) {
		this.listener = listener;
		this.menu = menu;
	}
	
	@Override
	public void run() {
		if(listener.buttonClicked())
			menu.getPlayer().closeInventory();
	}

}
