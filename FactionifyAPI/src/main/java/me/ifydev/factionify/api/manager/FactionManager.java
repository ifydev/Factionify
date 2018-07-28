package me.ifydev.factionify.api.manager;

import me.ifydev.factionify.api.manager.structures.Faction;

import java.util.*;

/**
 * @author Innectic
 * @since 07/27/2018
 */
public class FactionManager {

    private Map<UUID, Faction> factions = new HashMap<>();

    public Optional<Faction> getFactionFromName(String name) {
        return factions.values().stream().filter(faction -> faction.getName().equalsIgnoreCase(name)).findFirst();
    }

    public Optional<Faction> getFactionFromUUID(UUID uuid) {
        return factions.values().stream().filter(faction -> faction.getUuid().equals(uuid)).findFirst();
    }

    public void createFaction(String name, UUID creator) {
        UUID uuid = UUID.randomUUID();

        Faction faction = new Faction(uuid, name, Collections.singletonList(creator), Collections.emptyList());
        factions.put(uuid, faction);
    }

    public void removeFaction(UUID uuid) {
        factions.remove(uuid);
    }
}
