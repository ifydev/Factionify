package me.ifydev.factionify.api.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ifydev.factionify.api.database.impl.SQLHandler;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Innectic
 * @since 07/28/2018
 */
@AllArgsConstructor
public enum HandlerType {

    MYSQL(SQLHandler.class, "MySQL"), SQLITE(SQLHandler.class, "SQLite");

    @Getter private Class<? extends DatabaseHandler> handler;
    @Getter private String displayName;

    /**
     * Get the type of a handler from a name
     *
     * @param type the name of the handler to attempt to find
     * @return the handler. Filled if found, empty otherwise.
     */
    public static Optional<HandlerType> findType(String type) {
        return Arrays.stream(values()).filter(handler -> handler.getDisplayName().equalsIgnoreCase(type)).findFirst();
    }
}
