package net.dotefekts.dotutils.commandhelper;

import org.bukkit.command.CommandSender;

public class CommandEvent {
	private CommandSender sender;
	private Command command;
	private String[] args;
	
	CommandEvent(CommandSender sender, Command command, String[] args){
		this.sender = sender;
		this.command = command;
		this.args = args;
	}
	
	public CommandSender getSender(){
		return sender;
	}
	
	public Command getCommand() {
		return command;
	}
	
	public String[] getArgs(){
		return args;
	}
}
