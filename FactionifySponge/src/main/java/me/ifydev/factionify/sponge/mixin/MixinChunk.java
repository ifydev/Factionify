package me.ifydev.factionify.sponge.mixin;

import me.ifydev.factionify.api.structures.FactionifyChunk;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * @author Innectic
 * @since 01/17/2018
 */
@Mixin(Chunk.class)
public abstract class MixinChunk implements FactionifyChunk {

    @Override
    public Optional<UUID> getOwner() {
        if (new Random().nextBoolean()) return Optional.empty();
        return Optional.of(UUID.randomUUID());
    }
}
