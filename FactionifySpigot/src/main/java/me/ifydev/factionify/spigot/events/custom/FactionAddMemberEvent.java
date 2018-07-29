package me.ifydev.factionify.spigot.events.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
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
public class FactionAddMemberEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private UUID factionUUID;
    private UUID roleId;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
