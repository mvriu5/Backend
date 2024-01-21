package de.noque.backend.service;

import de.noque.backend.Network;
import de.noque.backend.model.PlayerObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerService {

    private final Connection _connection;

    private final String TABLE = "players";
    private final String COLUMN_UUID = "uuid";
    private final String COLUMN_NAME = "name";
    private final String COLUMN_FIRSTJOIN = "firstjoin";
    private final String COLUMN_FRIENDS = "friends";

    public PlayerService(Network network) {
        _connection = network.getConnection();
    }

    public void add(PlayerObject player) throws SQLException {
        if (getUUID(player.getName()) != null) return;

        PreparedStatement ps = _connection.prepareStatement("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)"
                .formatted(TABLE, COLUMN_UUID, COLUMN_NAME, COLUMN_FIRSTJOIN));
        ps.setString(1, player.getUuid().toString());
        ps.setString(2, player.getName());
        ps.setDate(1, (Date) player.getFirstJoin());

        ps.execute();
        ps.close();
    }

    public String getName(UUID uuid) throws SQLException {
        PreparedStatement ps = _connection.prepareStatement("SELECT * FROM %s WHERE %s = ?".formatted(TABLE, COLUMN_UUID));
        ps.setString(1, uuid.toString());
        ResultSet rs = ps.executeQuery();

        if (rs.first()) return rs.getString(COLUMN_NAME);

        return null;
    }

    public UUID getUUID(String name) throws SQLException {
        PreparedStatement ps = _connection.prepareStatement("SELECT * FROM %s WHERE %s = ?".formatted(TABLE, COLUMN_NAME));
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        if (rs.first()) return UUID.fromString(rs.getString(COLUMN_UUID));

        return null;
    }

    public List<UUID> getFriends(UUID uuid) throws SQLException {
        PreparedStatement ps = _connection.prepareStatement("SELECT * FROM %s WHERE %s = ?".formatted(TABLE, COLUMN_UUID));
        ps.setString(1, uuid.toString());
        ResultSet rs = ps.executeQuery();

        if (rs.first()) {
            String[] uuidArray = rs.getString(COLUMN_UUID).split(",");
            List<UUID> uuidList = new ArrayList<>();
            for (String s : uuidArray) uuidList.add(UUID.fromString(s));

            return uuidList;
        }

        return null;
    }

    public boolean addFriend(UUID uuid, UUID friendUUID) throws SQLException {
        if (containsFriend(uuid, friendUUID)) return false;

        PreparedStatement ps = _connection.prepareStatement("UPDATE %s SET %s = %s || ? WHERE %s = ?"
                .formatted(TABLE, COLUMN_FRIENDS, COLUMN_FRIENDS, COLUMN_UUID));
        ps.setString(1, ", " + friendUUID.toString());
        ps.setString(2, uuid.toString());

        ps.executeUpdate();
        ps.close();
        return true;
    }

    public boolean removeFriend(UUID uuid, UUID friendUUID) throws SQLException {
        if (!containsFriend(uuid, friendUUID)) return false;

        PreparedStatement ps = _connection.prepareStatement("UPDATE %s SET %s = REPLACE(%s, ?, '') WHERE %s = ?"
                .formatted(TABLE, COLUMN_FRIENDS, COLUMN_FRIENDS, COLUMN_NAME));
        ps.setString(1, ", " + friendUUID.toString());
        ps.setString(2, uuid.toString());

        ps.executeUpdate();
        ps.close();
        return true;
    }

    public boolean containsFriend(UUID uuid, UUID friendUUID) throws SQLException {
        PreparedStatement ps = _connection.prepareStatement("SELECT * FROM %s WHERE %s = ?".formatted(TABLE, COLUMN_UUID));
        ps.setString(1, uuid.toString());
        ResultSet rs = ps.executeQuery();

        if (rs.first())
            return rs.getString(COLUMN_FRIENDS).contains(friendUUID.toString());

        return false;
    }
}
