package com.ForgeEssentials.permission;

import java.util.ArrayList;
import java.util.TreeSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import com.ForgeEssentials.api.permissions.Group;
import com.ForgeEssentials.api.permissions.IPermissionsHelper;
import com.ForgeEssentials.api.permissions.RegGroup;
import com.ForgeEssentials.api.permissions.Zone;
import com.ForgeEssentials.api.permissions.ZoneManager;
import com.ForgeEssentials.api.permissions.events.PermissionSetEvent;
import com.ForgeEssentials.util.FunctionHelper;
import com.ForgeEssentials.util.Localization;

public class PermissionsHelper implements IPermissionsHelper
{
	public final String	EntryPlayer	= "_ENTRY_PLAYER_";
	private String		EPPrefix	= "";
	private String		EPSuffix	= "";
	private Group		DEFAULT		= new Group(RegGroup.ZONE.toString(), " ", " ", null, ZoneManager.getGLOBAL().getZoneName(), 0);

	@Override
	public Group createGroupInZone(String groupName, String zoneName, String prefix, String suffix, String parent, int priority)
	{
		Group g = new Group(groupName, prefix, suffix, parent, zoneName, priority);
		SqlHelper.createGroup(g);
		return g;
	}

	@Override
	public String setPlayerPermission(String username, String permission, boolean allow, String zoneID)
	{
		try
		{
			Zone zone = ZoneManager.getZone(zoneID);
			if (zone == null)
				return Localization.format(Localization.ERROR_ZONE_NOZONE, zoneID);

			Permission perm = new Permission(permission, allow);

			// send out permission string.
			PermissionSetEvent event = new PermissionSetEvent(perm, zone, "p:" + username);
			if (MinecraftForge.EVENT_BUS.post(event))
				return event.getCancelReason();

			SqlHelper.generatePlayer(username);
			boolean worked = SqlHelper.setPermission(username, false, perm, zoneID);

			if (!worked)
				return Localization.get(Localization.ERROR_PERM_SQL);
		}
		catch (Throwable t)
		{
			return t.getLocalizedMessage();
		}

		return null;
	}

	@Override
	public String setGroupPermission(String group, String permission, boolean allow, String zoneID)
	{
		try
		{
			Zone zone = ZoneManager.getZone(zoneID);

			if (zone == null)
				return Localization.format(Localization.ERROR_ZONE_NOZONE, zoneID);

			Group g = SqlHelper.getGroupForName(group);
			if (g == null)
				return Localization.format("message.error.nogroup", group);

			Permission perm = new Permission(permission, allow);

			// send out permission string.
			PermissionSetEvent event = new PermissionSetEvent(perm, zone, "g:" + group);
			if (MinecraftForge.EVENT_BUS.post(event))
				return event.getCancelReason();

			boolean worked = SqlHelper.setPermission(group, true, perm, zoneID);

			if (!worked)
				return Localization.get(Localization.ERROR_PERM_SQL);
		}
		catch (Throwable t)
		{
			return t.getMessage();
		}

		return null;
	}

	// ill recreate it when I need it...
	//
	// /**
	// * Gets all the groups that were explicitly created in the given zone.
	// these groups will only apply
	// * to the given Zone and all of its children.
	// * @param zoneID zone to check.
	// * @return List of Groups. may be an empty list, but never null.
	// */
	// protected ArrayList<Group> getAllGroupsCreatedForZone(String
	// zoneID)
	// {
	// ArrayList<Group> gs = new ArrayList<Group>();
	// for (Group g : groups.values())
	// if (g.zoneID.equals(zoneID))
	// gs.add(g);
	//
	// return gs;
	// }

	@Override
	public ArrayList<Group> getApplicableGroups(EntityPlayer player, boolean includeDefaults)
	{
		Zone zone = ZoneManager.getWhichZoneIn(FunctionHelper.getEntityPoint(player));

		return getApplicableGroups(player.username, includeDefaults, zone.getZoneName());
	}

	@Override
	public ArrayList<Group> getApplicableGroups(String player, boolean includeDefaults, String zoneID)
	{
		ArrayList<Group> list = new ArrayList<Group>();

		ArrayList<Group> temp;

		temp = SqlHelper.getGroupsForPlayer(player, zoneID);
		if (temp.isEmpty())
		{
			temp = SqlHelper.getGroupsForPlayer(player, ZoneManager.getGLOBAL().getZoneName());
		}
		list.addAll(temp);

		if (includeDefaults)
		{
			list.add(getDEFAULT());
		}

		return list;
	}

	@Override
	public Group getHighestGroup(EntityPlayer player)
	{
		Zone zone = ZoneManager.getWhichZoneIn(FunctionHelper.getEntityPoint(player));
		TreeSet<Group> list = new TreeSet<Group>();

		ArrayList<Group> temp;
		while (zone != null && list.size() <= 0)
		{
			temp = SqlHelper.getGroupsForPlayer(player.username, zone.getZoneName());

			if (!temp.isEmpty())
			{
				list.addAll(temp);
			}

			zone = ZoneManager.getZone(zone.parent);
		}

		if (list.size() == 0)
			return getDEFAULT();
		else
			return list.pollFirst();
	}

	@Override
	public Group getGroupForName(String name)
	{
		return SqlHelper.getGroupForName(name);
	}

	@Override
	public String setPlayerGroup(String group, String player, String zone)
	{
		SqlHelper.generatePlayer(player);
		return SqlHelper.setPlayerGroup(group, player, zone);
	}

	@Override
	public String addPlayerToGroup(String group, String player, String zone)
	{
		SqlHelper.generatePlayer(player);
		return SqlHelper.addPlayerGroup(group, player, zone);
	}

	@Override
	public String clearPlayerGroup(String group, String player, String zone)
	{
		return SqlHelper.removePlayerGroup(group, player, zone);
	}

	@Override
	public String clearPlayerPermission(String player, String node, String zone)
	{
		return SqlHelper.removePermission(player, false, node, zone);
	}

	@Override
	public void deleteGroupInZone(String group, String zone)
	{
		SqlHelper.deleteGroupInZone(group, zone);
	}

	@Override
	public boolean updateGroup(Group group)
	{
		return SqlHelper.updateGroup(group);
	}

	@Override
	public String clearGroupPermission(String name, String node, String zone)
	{
		return SqlHelper.removePermission(name, true, node, zone);
	}

	@Override
	public ArrayList getGroupsInZone(String zoneName)
	{
		return SqlHelper.getGroupsInZone(zoneName);
	}

	@Override
	public String getPermissionForGroup(String target, String zone, String perm)
	{
		return SqlHelper.getPermission(target, true, perm, zone);
	}

	@Override
	public ArrayList getPlayerPermissions(String target, String zone)
	{
		return SqlHelper.getAllPermissions(target, zone, 0);
	}

	@Override
	public ArrayList getGroupPermissions(String target, String zone)
	{
		return SqlHelper.getAllPermissions(target, zone, 1);
	}

	@Override
	public String getEPPrefix()
	{
		return EPPrefix;
	}

	public void setEPPrefix(String ePPrefix)
	{
		EPPrefix = ePPrefix;
	}

	@Override
	public Group getDEFAULT()
	{
		return DEFAULT;
	}

	@Override
	public void setEPPrifix(String ePPrefix)
	{
		EPPrefix = ePPrefix;
	}

	@Override
	public String getEPSuffix()
	{
		return EPSuffix;
	}

	@Override
	public void setEPSuffix(String ePSuffix)
	{
		EPSuffix = ePSuffix;
	}

	@Override
	public String getEntryPlayer()
	{
		return EntryPlayer;
	}
}
