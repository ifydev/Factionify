package me.ifydev.factionify.api;

import lombok.Getter;
import me.ifydev.factionify.api.database.ConnectionInformation;
import me.ifydev.factionify.api.database.DatabaseHandler;
import me.ifydev.factionify.api.database.HandlerType;
import me.ifydev.factionify.api.util.DisplayUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * @author Innectic
 * @since 07/28/2018
 */
public class FactionifyAPI {

    private static Optional<FactionifyAPI> instance;

    @Getter private Optional<DatabaseHandler> databaseHandler;
    @Getter private DisplayUtil displayUtil;

    public void initialize(HandlerType type, Optional<ConnectionInformation> connectionInformation, DisplayUtil displayUtil) throws Exception {
        instance = Optional.of(this);

        this.displayUtil = displayUtil;

        try {
            databaseHandler = Optional.of(type.getHandler().getConstructor(ConnectionInformation.class).newInstance(connectionInformation.orElse(null)));
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        if (!databaseHandler.isPresent()) throw new Exception("No data handler present.");

        databaseHandler.ifPresent(handler -> {
            handler.initialize();
            handler.reload();
        });
    }

    public static Optional<FactionifyAPI> getInstance() {
        return instance;
    }
}
