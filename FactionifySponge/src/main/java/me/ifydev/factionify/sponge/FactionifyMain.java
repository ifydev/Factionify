package me.ifydev.factionify.sponge;

import com.google.inject.Inject;
import lombok.Getter;
import me.ifydev.factionify.sponge.event.PlayerMove;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

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
        eventManager.registerListeners(this, new PlayerMove());
    }
}
