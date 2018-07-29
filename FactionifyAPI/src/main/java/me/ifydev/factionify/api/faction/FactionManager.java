package me.ifydev.factionify.api.faction;

import me.ifydev.factionify.api.FactionifyConstants;
import me.ifydev.factionify.api.util.FactionUtil;

import java.util.*;

/**
 * @author Innectic
 * @since 07/27/2018
 */
public class FactionManager {

    public Optional<Faction> getFactionFromName(String name) {
        return factions.values().stream().filter(faction -> faction.getName().equalsIgnoreCase(name)).findFirst();
    }

    public Optional<Faction> getFactionFromUUID(UUID uuid) {
        return factions.values().stream().filter(faction -> faction.getUuid().equals(uuid)).findFirst();
    }

    public Optional<Faction> createFaction(String name, UUID creator) {
        UUID uuid = UUID.randomUUID();

        Map<UUID, Role> roles = FactionUtil.getDefaultRoles(uuid);
        Optional<Role> owner = roles.values().stream().filter(r -> r.getName().equalsIgnoreCase(FactionifyConstants.DEFAULT_OWNER_NAME)).findFirst();
        if (!owner.isPresent()) {
            // I have no idea how this could ever happen but we like to be safe here.
            System.out.println("Somehow the owner role did not exist on faction creation?!");
            return Optional.empty();
        }

        Map<UUID, UUID> players = new HashMap<>();
        players.put(creator, owner.get().getUuid());

        Faction faction = new Faction(uuid, name, "", roles, players, new ArrayList<>());

        return Optional.of(faction);
    }

    public Optional<Faction> removeFaction(UUID uuid) {
        return Optional.ofNullable(factions.remove(uuid));
    }
}
