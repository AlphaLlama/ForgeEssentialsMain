package com.ForgeEssentials.api.permissions;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;

import com.ForgeEssentials.api.permissions.query.PermQuery;
import com.ForgeEssentials.api.permissions.query.PermQuery.PermResult;
import com.ForgeEssentials.api.permissions.query.PermissionQueryBus;

// This is a bouncer class for all Permissions API duties.

public abstract class PermissionsAPI
{
	public static final PermissionQueryBus	QUERY_BUS	= new PermissionQueryBus();
	public static IPermissionsHelper		manager;

	public static boolean checkPermAllowed(PermQuery query)
	{
		QUERY_BUS.post(query);
		return query.isAllowed();
	}

	public static PermResult checkPermResult(PermQuery query)
	{
		QUERY_BUS.post(query);
		return query.getResult();
	}

	public static Group createGroupInZone(String groupName, String zoneName, String prefix, String suffix, String parent, int priority)
	{
		return manager.createGroupInZone(groupName, zoneName, prefix, suffix, parent, priority);
	}

	public static String setPlayerPermission(String username, String permission, boolean allow, String zoneID)
	{
		return manager.setPlayerPermission(username, permission, allow, zoneID);
	}

	public static String setGroupPermission(String group, String permission, boolean allow, String zoneID)
	{
		return manager.setGroupPermission(group, permission, allow, zoneID);
	}

	public static ArrayList<Group> getApplicableGroups(EntityPlayer player, boolean includeDefaults)
	{
		return manager.getApplicableGroups(player, includeDefaults);
	}

	public static ArrayList<Group> getApplicableGroups(String player, boolean includeDefaults, String zoneID)
	{
		return manager.getApplicableGroups(player, includeDefaults, zoneID);
	}

	public static Group getGroupForName(String name)
	{
		return manager.getGroupForName(name);
	}

	public static Group getHighestGroup(EntityPlayer player)
	{
		return manager.getHighestGroup(player);
	}

	public static String setPlayerGroup(String group, String player, String zone)
	{
		return manager.setPlayerGroup(group, player, zone);
	}

	public static String addPlayerToGroup(String group, String player, String zone)
	{
		return manager.addPlayerToGroup(group, player, zone);
	}

	public static String clearPlayerGroup(String group, String player, String zone)
	{
		return manager.clearPlayerGroup(group, player, zone);
	}

	public static String clearPlayerPermission(String player, String node, String zone)
	{
		return manager.clearPlayerPermission(player, node, zone);
	}

	public static void deleteGroupInZone(String group, String zone)
	{
		manager.deleteGroupInZone(group, zone);
	}

	public static boolean updateGroup(Group group)
	{
		return manager.updateGroup(group);
	}

	public static String clearGroupPermission(String name, String node, String zone)
	{
		return manager.clearGroupPermission(name, node, zone);
	}

	public static ArrayList getGroupsInZone(String zoneName)
	{
		return manager.getGroupsInZone(zoneName);
	}

	public static String getPermissionForGroup(String target, String zone, String perm)
	{
		return manager.getPermissionForGroup(target, zone, perm);
	}

	public static ArrayList getPlayerPermissions(String target, String zone)
	{
		return manager.getPlayerPermissions(target, zone);
	}

	public static ArrayList getGroupPermissions(String target, String zone)
	{
		return manager.getGroupPermissions(target, zone);
	}

	public static String getEPPrefix()
	{
		return manager.getEPPrefix();
	}

	public static void setEPPrefix(String ePPrefix)
	{
		manager.setEPPrifix(ePPrefix);
	}

	public static String getEPSuffix()
	{
		return manager.getEPSuffix();
	}

	public static void setEPSuffix(String ePSuffix)
	{
		manager.setEPSuffix(ePSuffix);
	}

	public static Group getDEFAULT()
	{
		return manager.getDEFAULT();
	}

	public static String getEntryPlayer()
	{
		return manager.getEntryPlayer();
	}
}
