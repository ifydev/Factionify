package me.ifydev.factionify.api.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ifydev.factionify.api.faction.Faction;
import me.ifydev.factionify.api.faction.Role;
import me.ifydev.factionify.api.structures.Chunk;

import java.util.*;

/**
 * @author Innectic
 * @since 07/28/2018
 */
@RequiredArgsConstructor
public abstract class DatabaseHandler {

    @Getter protected Map<UUID, Faction> cachedFactions = new HashMap<>();

    @Getter protected final ConnectionInformation connectionInformation;

    public abstract void initialize();
    public abstract void reload();

    public abstract List<Faction> loadAllFactions();

    public abstract Optional<Faction> createGroup(String name, UUID creator);
    public abstract Optional<Faction> removeFaction(UUID uuid);

    public abstract Optional<Faction> getFaction(UUID uuid);
    public abstract Optional<Faction> getFaction(String name);

    public abstract List<Chunk> getChunksForFaction(UUID faction);
    public abstract Map<UUID, UUID> getMembersOfFaction(UUID faction);
    public abstract List<Role> getRoles(UUID faction);
}
