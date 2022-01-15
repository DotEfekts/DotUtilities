package net.dotefekts.dotutils.commandhelper;

import org.bukkit.permissions.Permission;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {
	private final String command;
	private final String[] subcommand;
	private final String description;
	private final String[] format;
	private final boolean serverCommand;
	private Permission permission;
	private final Method executor;
	private final Object commandHandler;
	
	Command(String command, String description, String format, boolean serverCommand, Permission permission, Method executor, Object commandHandler) {
		var commandSplit = command.split(" ");

		this.command = commandSplit[0];
		this.subcommand = Arrays.copyOfRange(commandSplit, 1, commandSplit.length);

		this.description = description;
		this.serverCommand = serverCommand;
		this.permission = permission;
		this.executor = executor;
		this.commandHandler = commandHandler;

		var pattern = Pattern.compile("([idps](?:<[\\w ]+>|\\[[\\w ]+])|\\.\\.\\.)");
		Matcher matcher = pattern.matcher(format);

		var formatList = new ArrayList<String>();

		while(matcher.find())
			formatList.add(matcher.group(1));

		this.format = formatList.toArray(new String[0]);
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

	public String[] getFormat() { return format; }

	public boolean isServerCommand() {
		return serverCommand;
	}

	Method getExecutor() { return executor; }

	Object getCommandHandler() { return commandHandler; }
	
	public String getParsedFormat() {
		StringJoiner formatted = new StringJoiner(" ");

		for(var param : format)
			formatted.add(param.equals("...") ? param : param.substring(1));

		return formatted.toString();
	}
	
	public String getSubcommandString() {
		StringJoiner formatted = new StringJoiner(" ");

		for(String str : subcommand)
			formatted.add(str);

		return formatted.toString();
	}
}
