package me.ifydev.factionify.api.manager.structures;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
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
    private List<UUID> players;
    private List<Chunk> claimedChunks;
}
