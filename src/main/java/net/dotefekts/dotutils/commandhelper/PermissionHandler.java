package net.dotefekts.dotutils.commandhelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.permissions.PermissionDefault;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionHandler {
	String node() default "";
	String description() default "";
	PermissionDefault permissionDefault() default PermissionDefault.TRUE;
}