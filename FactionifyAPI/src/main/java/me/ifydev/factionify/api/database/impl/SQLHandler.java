package me.ifydev.factionify.api.database.impl;

import me.ifydev.factionify.api.FactionifyAPI;
import me.ifydev.factionify.api.database.ConnectionError;
import me.ifydev.factionify.api.database.ConnectionInformation;
import me.ifydev.factionify.api.database.DatabaseHandler;
import me.ifydev.factionify.api.faction.Faction;
import me.ifydev.factionify.api.util.Tristate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Innectic
 * @since 07/28/2018
 */
public class SQLHandler extends DatabaseHandler {

    private String baseConnectionUrl;
    private boolean isUsingSQLite = false;

    public SQLHandler(ConnectionInformation connectionInformation) {
        super(connectionInformation);

        String type;
        String databaseURL;

        if (connectionInformation.getMeta().containsKey("sqlite")) {
            type = "sqlite";
            Map sqliteData = (Map) connectionInformation.getMeta().get("sqlite");
            databaseURL = (String) sqliteData.get("file");
            isUsingSQLite = true;
        } else {
            type = "mysql";
            databaseURL = "//" + connectionInformation.getUrl() + ":" + connectionInformation.getPort();
        }
        baseConnectionUrl = "jdbc:" + type + ":" + databaseURL;
    }

    private Optional<Connection> getConnection() {
        try {
            if (isUsingSQLite) return Optional.ofNullable(DriverManager.getConnection(baseConnectionUrl));
            String connectionURL = baseConnectionUrl + "/" + connectionInformation.getDatabase();
            return Optional.ofNullable(DriverManager.getConnection(connectionURL, connectionInformation.getUsername(), connectionInformation.getPassword()));
        } catch (SQLException e) {
            FactionifyAPI.getInstance().ifPresent(api -> api.getDisplayUtil().displayError(ConnectionError.REJECTED, Optional.of(e)));
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void initialize() {
        this.cachedFactions = new HashMap<>();

        try {
            Connection connection;
            if (isUsingSQLite) connection = DriverManager.getConnection(baseConnectionUrl);
            else connection = DriverManager.getConnection(baseConnectionUrl, connectionInformation.getUsername(), connectionInformation.getPassword());
            if (connection == null) {
                System.out.println("Could not connect to database during initialization.");
                FactionifyAPI.getInstance().ifPresent(api -> api.getDisplayUtil().displayError(ConnectionError.REJECTED, Optional.empty()));
                return;
            }

            String database = connectionInformation.getDatabase();

            if (!isUsingSQLite) {
                PreparedStatement statement = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS " + database);
                statement.execute();
                statement.close();
                database += ".";
            } else database = "";

            PreparedStatement factionsStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + database + "factions (uuid VARCHAR(767) NOT NULL UNIQUE, `owner` VARCHAR(767) NOT NULL, name VARCHAR(767) NOT NULL UNIQUE, description TEXT NOT NULL)");
            factionsStatement.execute();
            factionsStatement.close();

            PreparedStatement factionMembersStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + database + "factionMembers (uuid VARCHAR(767) NOT NULL UNIQUE, `faction` VARCHAR(767) NOT NULL)");
            factionMembersStatement.execute();
            factionMembersStatement.close();

            connection.close();
        } catch (SQLException e) {
            FactionifyAPI.getInstance().ifPresent(api -> api.getDisplayUtil().displayError(ConnectionError.DATABASE_EXCEPTION, Optional.of(e)));
            e.printStackTrace();
        }
    }

    @Override
    public void reload() {

    }

    @Override
    public Tristate createGroup(String name, UUID creator) {
        return null;
    }

    @Override
    public Optional<Faction> removeFaction(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<Faction> getFaction(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<Faction> getFaction(String name) {
        return Optional.empty();
    }
}
