package net.dotefekts.dotutils.commandhelper;

import java.lang.reflect.Method;

import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;

public class Command {
	private String command;
	private String[] subcommand;
	private String description;
	private String format;
	private boolean serverCommand;
	private Permission permission;
	private Method executor;
	private Listener listener;
	
	Command(String command, String description, String format, boolean serverCommand, Permission permission, Method executor, Listener listener) {
		this.command = command.split(" ")[0];
		
		subcommand = new String[command.split(" ").length - 1];
		for(int i = 1; i < command.split(" ").length; i++)
			subcommand[i - 1] = command.split(" ")[i];
		
		this.description = description;
		this.format = format;
		this.serverCommand = serverCommand;
		this.permission = permission;
		this.executor = executor;
		this.listener = listener;
	}
	
	public Permission getPermission() {
		return permission;
	}

	void setPermission(Permission permission) {
		this.permission = permission;
	}

	public String getCommand() {
		return command;
	}
	
	public String[] getSubcommand(){
		return subcommand;
	}

	public String getDescription() {
		return description;
	}

	public String getFormat() {
		return format;
	}

	public boolean isServerCommand() {
		return serverCommand;
	}

	Method getExecutor() {
		return executor;
	}

	Listener getListener() {
		return listener;
	}
	
	public String getParsedFormat() {
		String formatted = "";
		for(String str : format.split(" "))
			if(!str.isEmpty() && !str.equalsIgnoreCase("..."))
				formatted = formatted + " " + str.substring(1, str.length());
			else 
				formatted = formatted + " " + str;
		return formatted;
	}
	
	public String getSubcommandString() {
		String formatted = "";
		for(String str : subcommand)
			if(formatted.isEmpty())
				formatted = str;
			else 
				formatted = formatted + " " + str;
		return formatted;
	}
}
