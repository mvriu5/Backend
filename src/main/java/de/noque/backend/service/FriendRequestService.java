package de.noque.backend.service;

import de.noque.backend.Network;

import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;

public class FriendRequestService {

    private final Connection _connection;

    private final String TABLE = "friendrequest";
    private final String COLUMN_SENDER = "sender";
    private final String COLUMN_TARGET = "target";
    private final String COLUMN_TIMESENT = "time_sent";

    public FriendRequestService(Network network) {
        _connection = network.getConnection();
    }

    public void add(UUID sender, UUID target) throws SQLException {
        if (!getRequest(sender, target)) return;

        PreparedStatement ps = _connection.prepareStatement("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)"
                .formatted(TABLE, COLUMN_SENDER, COLUMN_TARGET, COLUMN_TIMESENT));
        ps.setString(1, sender.toString());
        ps.setString(2, target.toString());
        ps.setDate(1, Date.valueOf(LocalDate.now()));

        ps.executeUpdate();
        ps.close();
    }

    public void remove(UUID sender, UUID target) throws SQLException {
        if (!getRequest(sender, target)) return;

        PreparedStatement ps = _connection.prepareStatement("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)"
                .formatted(TABLE, COLUMN_SENDER, COLUMN_TARGET, COLUMN_TIMESENT));
        ps.setString(1, sender.toString());
        ps.setString(2, target.toString());
        ps.setDate(1, Date.valueOf(LocalDate.now()));

        ps.executeUpdate();
        ps.close();
    }

    public boolean getRequest(UUID sender, UUID target) throws SQLException {
        PreparedStatement ps = _connection.prepareStatement("SELECT * FROM %s WHERE %s = ? AND %s = ?"
                .formatted(TABLE, COLUMN_SENDER, COLUMN_TARGET));
        ps.setString(1, sender.toString());
        ps.setString(2, target.toString());
        ResultSet rs = ps.executeQuery();

        return rs.first();
    }
}
