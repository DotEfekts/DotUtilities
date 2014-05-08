package net.dotefekts.dotutils.commandhelper;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandCompleter implements TabCompleter {
	String command;
	CommandManager manager;
	
	CommandCompleter(String command, CommandManager manager) {
		this.command = command;
		this.manager = manager;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		ArrayList<String> auto = new ArrayList<String>();
		
		if(!command.getName().equalsIgnoreCase(this.command)) {
			Bukkit.getLogger().warning("[CommandHelper] CommandCompleter asked for completion of another command.");
			return new ArrayList<String>();
		}
		
		net.dotefekts.dotutils.commandhelper.Command cmd = manager.matchCommand(command.getName(), args, !(sender instanceof Player));
		
		if(cmd == null) {
			return auto;
		}
		
		String[] subcommand = cmd.getSubcommand();
		String[] format = cmd.getFormat().split(" ");
		int argPos = args.length - 1;	
		int formatPos = argPos - subcommand.length;
		
		if(format.length >= args.length - subcommand.length && formatPos >= 0){
			if(format[formatPos].startsWith("p")){
				for(Player player : Bukkit.getOnlinePlayers())
					if(player.getName().startsWith(args[argPos]))
						auto.add(player.getName());
			}
		}
		return auto;
	}
}
