package me.ifydev.factionify.spigot.events.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ifydev.factionify.api.faction.Faction;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Innectic
 * @since 07/28/2018
 */
@AllArgsConstructor
@Getter
public class FactionDisbandEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Faction faction;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
