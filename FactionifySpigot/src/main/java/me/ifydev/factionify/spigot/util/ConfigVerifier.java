package me.ifydev.factionify.spigot.util;

import me.ifydev.factionify.api.database.ConnectionInformation;
import me.ifydev.factionify.api.database.HandlerType;
import me.ifydev.factionify.api.database.impl.SQLHandler;
import me.ifydev.factionify.spigot.FactionifyMain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Innectic
 * @since 07/28/2018
 */
public class ConfigVerifier {

    public static Optional<ConnectionInformation> verifyConnectionInformation() {
        FactionifyMain plugin = FactionifyMain.getInstance();
        Optional<HandlerType> type = HandlerType.findType(plugin.getConfig().getString("storage", "sqlite"));
        if  (!type.isPresent()) return Optional.empty();

        Optional<ConnectionInformation> connectionInformation = Optional.empty();
        if (type.get().getHandler() == SQLHandler.class) {
            if (type.get().getDisplayName().equalsIgnoreCase("mysql")) {
                if (plugin.getConfig().getString("connection.host") == null) return Optional.empty();
                if (plugin.getConfig().getString("connection.database") == null) return Optional.empty();
                if (plugin.getConfig().getString("connection.port") == null) return Optional.empty();
                if (plugin.getConfig().getString("connection.username") == null) return Optional.empty();
                if (plugin.getConfig().getString("connection.password") == null) return Optional.empty();

                connectionInformation = Optional.of(new ConnectionInformation(
                        plugin.getConfig().getString("connection.host"),
                        plugin.getConfig().getString("connection.database"),
                        plugin.getConfig().getInt("connection.port"),
                        plugin.getConfig().getString("connection.username"),
                        plugin.getConfig().getString("connection.password"),
                        new HashMap<>())
                );
            } else if (type.get().getDisplayName().equalsIgnoreCase("sqlite")) {
                if (plugin.getConfig().getString("connection.file") == null) return Optional.empty();

                Map<String, Object> sqliteMeta = new HashMap<>();
                sqliteMeta.put("file", plugin.getDataFolder() + "/" + plugin.getConfig().getString("connection.file"));
                Map<String, Object> meta = new HashMap<>();
                meta.put("sqlite", sqliteMeta);

                connectionInformation = Optional.of(new ConnectionInformation("", "", 0, "", "", meta));
            }
        }
        return connectionInformation;
    }
}