package me.ifydev.factionify.api.faction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ifydev.factionify.api.structures.Chunk;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Innectic
 * @since 07/27/2018
 */
@AllArgsConstructor
@Getter
public class Faction {
    private UUID uuid;
    private String name;
    private String description;
    private Map<UUID, Role> roles;
    private Map<UUID, UUID> players;
    private List<Chunk> claimedChunks;

    public Optional <Role> getRoleForPlayer(UUID uuid) {
        return getRoleByUUID(players.get(uuid));
    }

    public Optional<Role> getRoleByUUID(UUID uuid) {
        return Optional.ofNullable(roles.getOrDefault(uuid, null));
    }
}
