package me.ifydev.factionify.api.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ifydev.factionify.api.faction.Faction;
import me.ifydev.factionify.api.util.Tristate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    public abstract Tristate createGroup(String name, UUID creator);
    public abstract Optional<Faction> removeFaction(UUID uuid);

    public abstract Optional<Faction> getFaction(UUID uuid);
    public abstract Optional<Faction> getFaction(String name);
}
