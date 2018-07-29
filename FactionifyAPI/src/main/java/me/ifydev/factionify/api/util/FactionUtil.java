package me.ifydev.factionify.api.util;

import me.ifydev.factionify.api.FactionifyConstants;
import me.ifydev.factionify.api.faction.Role;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Innectic
 * @since 07/28/2018
 */
public class FactionUtil {

    public static Map<UUID, Role> getDefaultRoles(UUID faction) {
        Map<UUID, Role> roles = new HashMap<>();
        UUID uuid = UUID.randomUUID();

        roles.put(uuid, new Role(uuid, faction, FactionifyConstants.DEFAULT_OWNER_NAME, 100));
        roles.put(uuid, new Role(uuid, faction, FactionifyConstants.DEFAULT_MEMBER_NAME, 1));

        return roles;
    }
}
