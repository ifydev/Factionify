package me.ifydev.factionify.spigot.events.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ifydev.factionify.api.manager.structures.Chunk;
import me.ifydev.factionify.api.manager.structures.Faction;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Innectic
 * @since 07/28/2018
 */
@AllArgsConstructor
@Getter
public class FactionUnclaimLandEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Chunk chunk;
    private Player claimer;
    private Faction faction;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
