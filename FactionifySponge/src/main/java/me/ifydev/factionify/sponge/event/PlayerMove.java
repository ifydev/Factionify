package me.ifydev.factionify.sponge.event;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;

/**
 * @author Innectic
 * @since 01/17/2018
 */
public class PlayerMove {

    @Listener
    public void onPlayerMove(MoveEntityEvent e) {
        if (e.getTargetEntity() instanceof Player) {
            Player player = (Player) e.getTargetEntity();

//            FactionifyChunk chunk = (FactionifyChunk) player.getWorld().getChunk(player.getLocation().getChunkPosition()).orElse(null);
//            if (chunk == null) {
//                player.sendMessage(Text.of("No world"));
//                return;
//            }
//            Optional<UUID> owner = chunk.getOwner();
//            if (owner.isPresent()) {
//                player.sendMessage(Text.of(owner.get().toString()));
//                return;
//            }
//            player.sendMessage(Text.of("No one"));
        }
    }
}
