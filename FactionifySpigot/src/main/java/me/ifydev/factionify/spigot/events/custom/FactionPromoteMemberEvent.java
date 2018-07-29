package me.ifydev.factionify.spigot.events.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ifydev.factionify.api.manager.structures.Faction;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * @author Innectic
 * @since 07/28/2018
 */
@AllArgsConstructor
@Getter
public class FactionPromoteMemberEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Faction faction;
    private Player target;

    // TODO: Maybe these should be replaced in the future with a real role type
    private UUID oldRole;
    private UUID newRole;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
