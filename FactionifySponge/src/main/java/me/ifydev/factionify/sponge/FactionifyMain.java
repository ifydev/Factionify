package me.ifydev.factionify.sponge;

import com.google.inject.Inject;
import lombok.Getter;
import me.ifydev.factionify.sponge.api.FactionifyChunk;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Innectic
 * @since 01/17/2018
 */
@Plugin(id = "factionify", name = "Factionify", version = "@VERSION@")
public class FactionifyMain {
    @Inject @Getter private Game game;
    @Inject @Getter private EventManager eventManager;
    @Inject private Logger logger;

    @Listener
    public void onServerPerInit(GamePreInitializationEvent e) {
        logger.info("Starting Factionify...");
    }

    @Listener
    public void onPlayerMove(MoveEntityEvent e) {
        if (e.getTargetEntity() instanceof Player) {
            Player player = (Player) e.getTargetEntity();

            FactionifyChunk chunk = (FactionifyChunk) player.getWorld().getChunk(player.getLocation().getChunkPosition()).orElse(null);
            if (chunk == null) {
                player.sendMessage(Text.of("No world"));
                return;
            }
            Optional<UUID> owner = chunk.getOwner();
            if (owner.isPresent()) {
                player.sendMessage(Text.of(owner.get().toString()));
                return;
            }
            player.sendMessage(Text.of("No one"));
        }
    }
}
