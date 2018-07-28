package me.ifydev.factionify.api.structures;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Innectic
 * @since 01/17/2018
 */
public interface FactionifyChunk {

    Optional<UUID> getOwner();
}
