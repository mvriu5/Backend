package de.noque.backend.service;

import de.noque.backend.Network;
import de.noque.backend.model.ServerObject;
import de.noque.backend.model.enums.State;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServerService {

    private final Connection _connection;

    private final String TABLE = "servers";
    private final String COLUMN_NAME = "name";
    private final String COLUMN_GAMEMODE = "gamemode";
    private final String COLUMN_STATE = "state";


    public ServerService(Network network) {
        _connection = network.getConnection();
    }

    public boolean add(String name, String gameMode) throws SQLException {
        if (get(name) != null) return false;

        PreparedStatement ps = _connection.prepareStatement("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)"
                .formatted(TABLE, COLUMN_NAME, COLUMN_STATE, COLUMN_STATE));
        ps.setObject(1, name);
        ps.setObject(2, gameMode);
        ps.setObject(1, State.WAITING);

        ps.execute();
        ps.close();
        return true;
    }

    public boolean remove(String name) throws SQLException {
        if (get(name) != null) return false;

        PreparedStatement ps = _connection.prepareStatement("DELETE FROM %s WHERE %s = ?".formatted(TABLE, COLUMN_NAME));
        ps.setString(1, name);

        ps.execute();
        ps.close();
        return true;
    }

    public ServerObject get(String name) throws SQLException {
        PreparedStatement ps = _connection.prepareStatement("SELECT * FROM %s WHERE %s = ?".formatted(TABLE, COLUMN_NAME));
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        if (rs.first()) {
            var server = new ServerObject();
            server.setName(rs.getString(COLUMN_NAME));
            server.setGameMode(rs.getString(COLUMN_GAMEMODE));
            server.setState(State.valueOf(rs.getString(COLUMN_STATE)));
            return server;
        }

        return null;
    }

    public List<ServerObject> getPerGamemode(String gameMode) throws SQLException {
        PreparedStatement ps = _connection.prepareStatement("SELECT * FROM %s WHERE %s = ?".formatted(TABLE, COLUMN_GAMEMODE));
        ps.setString(1, gameMode);
        ResultSet rs = ps.executeQuery();

        return loadServersFromResult(rs);
    }

    public List<ServerObject> getAll() throws SQLException {
        PreparedStatement ps = _connection.prepareStatement("SELECT * FROM %s".formatted(TABLE));
        ResultSet rs = ps.executeQuery();

        return loadServersFromResult(rs);
    }


    private List<ServerObject> loadServersFromResult(ResultSet rs) throws SQLException {
        List<ServerObject> serverList = new ArrayList<>();
        while (rs.next()) {
            var server = new ServerObject();
            server.setName(rs.getString(COLUMN_NAME));
            server.setGameMode(rs.getString(COLUMN_GAMEMODE));
            server.setState(State.valueOf(rs.getString(COLUMN_STATE)));
            serverList.add(server);
        }

        return serverList;
    }

}
