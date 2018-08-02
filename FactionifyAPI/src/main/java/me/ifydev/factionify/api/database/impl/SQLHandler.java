package me.ifydev.factionify.api.database.impl;

import me.ifydev.factionify.api.FactionifyAPI;
import me.ifydev.factionify.api.FactionifyConstants;
import me.ifydev.factionify.api.database.ConnectionError;
import me.ifydev.factionify.api.database.ConnectionInformation;
import me.ifydev.factionify.api.database.DatabaseHandler;
import me.ifydev.factionify.api.faction.Faction;
import me.ifydev.factionify.api.faction.Role;
import me.ifydev.factionify.api.structures.Chunk;
import me.ifydev.factionify.api.util.FactionUtil;
import me.ifydev.factionify.api.util.Tristate;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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

            PreparedStatement factionsStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + database + "factions (uuid TEXT NOT NULL UNIQUE, `owner` TEXT NOT NULL, `name` TEXT NOT NULL UNIQUE, description TEXT NOT NULL)");
            factionsStatement.execute();
            factionsStatement.close();

            PreparedStatement factionMembersStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + database + "factionMembers (uuid TEXT NOT NULL UNIQUE, `faction` TEXT NOT NULL, `role` TEXT NOT NULL)");
            factionMembersStatement.execute();
            factionMembersStatement.close();

            PreparedStatement factionRolesStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + database + "factionRoles (faction TEXT NOT NULL, uuid TEXT NOT NULL, `name` TEXT NOT NULL, power INTEGER NOT NULL)");
            factionRolesStatement.execute();
            factionRolesStatement.close();

            PreparedStatement claimedChunksStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + database + "claimedChunks (faction TEXT NOT NULL, x INTEGER NOT NULL, z INTEGER NOT NULL, world TEXT NOT NULL)");
            claimedChunksStatement.execute();
            claimedChunksStatement.close();

            connection.close();
        } catch (SQLException e) {
            FactionifyAPI.getInstance().ifPresent(api -> api.getDisplayUtil().displayError(ConnectionError.DATABASE_EXCEPTION, Optional.of(e)));
            e.printStackTrace();
        }
    }

    @Override
    public void reload() {
        cachedFactions = this.loadAllFactions().stream().collect(Collectors.toMap(Faction::getUuid, faction -> faction));
    }

    @Override
    public List<Faction> loadAllFactions() {
        List<Faction> factions = new ArrayList<>();

        Optional<Connection> connection = getConnection();
        if (!connection.isPresent()) {
            FactionifyAPI.getInstance().ifPresent(api -> api.getDisplayUtil().displayError(ConnectionError.REJECTED, Optional.empty()));
            return factions;
        }

        try {
            PreparedStatement statement = connection.get().prepareStatement("SELECT * FROM factions");
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                UUID factionUUID = UUID.fromString(results.getString("uuid"));
                Map<UUID, Role> roles = getRoles(factionUUID).stream().collect(Collectors.toMap(Role::getUuid, role -> role));

                factions.add(new Faction(factionUUID, results.getString("name"), results.getString("description"), roles, getMembersOfFaction(factionUUID), getChunksForFaction(factionUUID)));
            }
        } catch (SQLException e) {
            FactionifyAPI.getInstance().ifPresent(api -> api.getDisplayUtil().displayError(ConnectionError.DATABASE_EXCEPTION, Optional.empty()));
            e.printStackTrace();
        }

        return factions;
    }

    @Override
    public Optional<Faction> createGroup(String name, UUID creator) {
        // :NameExistsCheck
        // TODO: some kind of a check to make sure a faction with this name doesn't exist.
        UUID uuid = UUID.randomUUID();

        Map<UUID, Role> roles = FactionUtil.getDefaultRoles(uuid);

        // We can immediately get this without a check because it's guaranteed to exist.
        Role owner = roles.values().stream().filter(r -> r.getName().equalsIgnoreCase(FactionifyConstants.DEFAULT_OWNER_NAME)).findFirst().get();
        Map<UUID, UUID> players = new HashMap<>();
        players.put(creator, owner.getUuid());

        Faction faction = new Faction(uuid, name, FactionifyConstants.DEFAULT_DESCRIPTION, roles, players, new ArrayList<>());
        cachedFactions.put(uuid, faction);

        Optional<Connection> connection = getConnection();
        if (!connection.isPresent()) {
            FactionifyAPI.getInstance().ifPresent(api -> api.getDisplayUtil().displayError(ConnectionError.REJECTED, Optional.empty()));
            return Optional.empty();
        }

        try {
            PreparedStatement statement = connection.get().prepareStatement("INSERT INTO factions (uuid, `owner`, `name`, description) VALUES (?, ?, ?, ?)");
            statement.setString(1, uuid.toString());
            statement.setString(2, creator.toString());
            statement.setString(3, name);
            statement.setString(4, faction.getDescription());

            statement.execute();
            statement.close();
            connection.get().close();
        } catch (SQLException e) {
            e.printStackTrace();
            FactionifyAPI.getInstance().ifPresent(api -> api.getDisplayUtil().displayError(ConnectionError.DATABASE_EXCEPTION, Optional.empty()));
            return Optional.empty();
        }

        return Optional.of(faction);
    }

    @Override
    public Optional<Faction> removeFaction(UUID uuid) {
        if (!getFaction(uuid).isPresent()) return Optional.empty();
        Faction faction = cachedFactions.remove(uuid);
        // :LargeFactionOptimizations
        // :BetterResultReturn
        for (UUID player : faction.getPlayers().keySet()) {
            Tristate result = this.removePlayerFromFaction(player, uuid);
            if (result == Tristate.NONE) return Optional.empty();
            else if (result == Tristate.FALSE) return Optional.empty();
        }

        Optional<Connection> connection = getConnection();
        if (!connection.isPresent()) {
            FactionifyAPI.getInstance().ifPresent(api -> api.getDisplayUtil().displayError(ConnectionError.REJECTED, Optional.empty()));
            return Optional.empty();
        }

        try {
            PreparedStatement statement = connection.get().prepareStatement("DELETE FROM factions WHERE uuid=?");
            statement.setString(1, uuid.toString());

            statement.execute();
            statement.close();
            connection.get().close();
        } catch (SQLException e) {
            FactionifyAPI.getInstance().ifPresent(api -> api.getDisplayUtil().displayError(ConnectionError.DATABASE_EXCEPTION, Optional.empty()));
            e.printStackTrace();
            return Optional.empty();
        }

        return Optional.of(faction);
    }

    @Override
    public Optional<Faction> getFaction(UUID uuid) {
        return cachedFactions.values().stream().filter(faction -> faction.getUuid().equals(uuid)).findFirst();
    }

    @Override
    public Optional<Faction> getFaction(String name) {
        return cachedFactions.values().stream().filter(faction -> faction.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public Optional<Faction> getFactionForPlayer(UUID uuid) {
        return cachedFactions.values().stream().filter(f -> f.getPlayers().containsKey(uuid)).findFirst();
    }

    @Override
    public Tristate removePlayerFromFaction(UUID player, UUID faction) {
        // :LargeFactionOptimizations
        // :BetterResultReturn

        Optional<Faction> f = getFaction(faction);
        if (!f.isPresent()) return Tristate.FALSE;

        f.get().getPlayers().remove(player);

        Optional<Connection> connection = getConnection();
        if (!connection.isPresent()) {
            FactionifyAPI.getInstance().ifPresent(api -> api.getDisplayUtil().displayError(ConnectionError.REJECTED, Optional.empty()));
            return Tristate.NONE;
        }

        try {
            PreparedStatement statement = connection.get().prepareStatement("DELETE FROM factionMembers WHERE uuid=? AND faction=?");
            statement.setString(1, player.toString());
            statement.setString(2, faction.toString());

            statement.execute();
            statement.close();

            connection.get().close();
        } catch (SQLException e) {
            FactionifyAPI.getInstance().ifPresent(api -> api.getDisplayUtil().displayError(ConnectionError.DATABASE_EXCEPTION, Optional.empty()));
            e.printStackTrace();
            return Tristate.NONE;
        }

        return Tristate.TRUE;
    }

    @Override
    public List<Chunk> getChunksForFaction(UUID faction) {
        Optional<Connection> connection = getConnection();
        List<Chunk> chunks = new ArrayList<>();

        if (!connection.isPresent()) {
            FactionifyAPI.getInstance().ifPresent(api -> api.getDisplayUtil().displayError(ConnectionError.REJECTED, Optional.empty()));
            return chunks;
        }

        try {
            PreparedStatement statement = connection.get().prepareStatement("SELECT * FROM claimedChunks WHERE faction=?");
            statement.setString(1, faction.toString());

            ResultSet results = statement.executeQuery();
            while (results.next()) chunks.add(new Chunk(results.getInt("x"), results.getInt("z"), results.getString("world")));

            results.close();
            statement.close();
            connection.get().close();

            return chunks;
        } catch (SQLException e) {
            FactionifyAPI.getInstance().ifPresent(api -> api.getDisplayUtil().displayError(ConnectionError.DATABASE_EXCEPTION, Optional.empty()));
            e.printStackTrace();
        }

        return chunks;
    }

    @Override
    public Map<UUID, UUID> getMembersOfFaction(UUID faction) {
        Map<UUID, UUID> members = new HashMap<>();

        Optional<Connection> connection = getConnection();
        if (!connection.isPresent()) {
            FactionifyAPI.getInstance().ifPresent(api -> api.getDisplayUtil().displayError(ConnectionError.REJECTED, Optional.empty()));
            return members;
        }

        try {
            PreparedStatement statement = connection.get().prepareStatement("SELECT * FROM factionMembers WHERE faction=?");
            statement.setString(1, faction.toString());

            ResultSet results = statement.executeQuery();
            while (results.next()) members.put(UUID.fromString(results.getString("uuid")), UUID.fromString(results.getString("role")));

            results.close();
            statement.close();
            connection.get().close();

            return members;
        } catch (SQLException e) {
            FactionifyAPI.getInstance().ifPresent(api -> api.getDisplayUtil().displayError(ConnectionError.DATABASE_EXCEPTION, Optional.empty()));
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Role> getRoles(UUID faction) {
        List<Role> roles = new ArrayList<>();

        Optional<Connection> connection = getConnection();
        if (!connection.isPresent()) {
            return roles;
        }

        try {
            PreparedStatement statement = connection.get().prepareStatement("SELECT * FROM factionRoles WHERE faction=?");
            statement.setString(1, faction.toString());

            ResultSet results = statement.executeQuery();
            while (results.next()) roles.add(new Role(UUID.fromString(results.getString("uuid")),
                    UUID.fromString(results.getString("faction")), results.getString("name"), results.getInt("power")));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }
}
