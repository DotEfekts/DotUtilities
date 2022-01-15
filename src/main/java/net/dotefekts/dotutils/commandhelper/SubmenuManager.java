package net.dotefekts.dotutils.commandhelper;

import net.dotefekts.dotutils.UtilityFunctions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class SubmenuManager {
	private final HashMap<String, List<Command>> submenus;
	
	SubmenuManager(){
		submenus = new HashMap<>();
	}
	
	List<Command> getSubcommands(String command) {
		return submenus.getOrDefault(command, null);
	}
	
	void addSubcommand(Command command) {
		if(submenus.containsKey(command.getCommand())) {
			boolean allGood = true;
			for(Command cmd : submenus.get(command.getCommand())) {
				if(SubmenuManager.compareStringArr(cmd.getSubcommand(), command.getSubcommand()))
					allGood = false;
			}
			if(allGood)
				submenus.get(command.getCommand()).add(command);
			else
				Bukkit.getLogger().warning("[CommandHelper] Tried to register a duplicate submenu.");
		} else {
			ArrayList<Command> newList = new ArrayList<>();
			newList.add(command);
			submenus.put(command.getCommand(), newList);
		}
	}

	void addList(List<Command> newCommands) {
		for(Command cmd : newCommands)
			addSubcommand(cmd);
	}
	
	static boolean compareStringArr(String[] subOne, String[] subTwo){
		if(subOne.length == subTwo.length){
			boolean good = true;
			for(int i = 0; i < subOne.length; i++)
				if (!subOne[i].equalsIgnoreCase(subTwo[i])) {
					good = false;
					break;
				}
			return good;
		}
		return false;
	}
	
	static ArrayList<String> generateSubmenu(List<Command> commands, String[] submenu){
		if(commands.size() == 0)
			return null;
		String sub = UtilityFunctions.joinArray(submenu, " ");
		ArrayList<String> menu = new ArrayList<>();
		ArrayList<String> menus = new ArrayList<>();
		ArrayList<Command> menuCommands = new ArrayList<>();
		
		for(Command command : commands) {
			if(command.getSubcommand().length > 0)
				if(command.getSubcommandString().startsWith(sub)
						&& command.getSubcommand().length > submenu.length)
					if(command.getSubcommand().length == submenu.length + 1)
						menuCommands.add(command);
					else if(!menus.contains(command.getSubcommand()[submenu.length]))
						menus.add(command.getSubcommand()[submenu.length]);
		}
		
		String header = ChatColor.DARK_AQUA + "-- " + ChatColor.BLUE + 
				(commands.get(0).isServerCommand() ? "" : "/") 	+ commands.get(0).getCommand()
				+ " " + ChatColor.YELLOW + sub + (sub.isEmpty() ? "" : " ")
				+ ChatColor.DARK_AQUA + " --";
		menu.add(header);
		
		String base = ChatColor.YELLOW + "";
		
		for(Command cmd : menuCommands) {
			String format = cmd.getParsedFormat();
			String highSub = cmd.getSubcommand()[cmd.getSubcommand().length - 1];
			
			menu.add(base + highSub + ChatColor.GOLD + format);
			if(!cmd.getDescription().isEmpty())
				menu.add("  " + ChatColor.GRAY + cmd.getDescription());
		}
		
		for(String str : menus) {		
			menu.add(base + str);
			menu.add("  " + ChatColor.GRAY + "Access the " + str + " submenu.");
		}
		return menu;
	}
}
