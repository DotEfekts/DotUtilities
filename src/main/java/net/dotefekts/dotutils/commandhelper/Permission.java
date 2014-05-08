package net.dotefekts.dotutils.commandhelper;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

public class Permission {
	private String node;
	private String description;
	private PermissionDefault permissionDefault;
	
	Permission(String node, String description, PermissionDefault permissionDefault){
		this.node = node;
		this.description = description;
		this.permissionDefault = permissionDefault;
	}
	
	public String getNode(){
		return node;
	}
	
	public String getDescription(){
		return description;
	}
	
	public PermissionDefault getPermissionDefault(){
		return permissionDefault;
	}
	
	public boolean hasPermission(Player player) {
		if(!node.equalsIgnoreCase(""))
			if(!player.hasPermission(node)) {
				String built = "";
				boolean hasPerm = false;
				for(String str : node.split("[.]")) {
					if(built.isEmpty())
						built = str;
					else
						built = built + "." + str;
					if(player.hasPermission(built + ".*"))
						hasPerm = true;
				}
				
				if(!hasPerm)
					return false;
			} else;
		else
			switch(permissionDefault) {
			case FALSE:
				return false;
			case NOT_OP:
				if(player.isOp())
					return false;
				break;
			case OP:
				if(!player.isOp())
					return false;
				break;
			default:
				break;
			}
		return true;
	}
}
