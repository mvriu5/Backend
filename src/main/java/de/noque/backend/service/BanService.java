package de.noque.backend.service;

import de.noque.backend.Network;
import de.noque.backend.model.PlayerBan;
import de.noque.backend.model.ServerObject;
import de.noque.backend.model.enums.BanReason;
import de.noque.backend.model.enums.State;

import java.sql.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.UUID;

public class BanService {

    private final Connection _connection;

    private final String TABLE = "bans";
    private final String COLUMN_UUID = "uuid";
    private final String COLUMN_REASON = "reason";
    private final String COLUMN_TIMEBANNED = "time_banned";
    private final String COLUMN_BANNEDUNTIL = "banned_until";


    public BanService(Network network) {
        _connection = network.getConnection();
    }

    public boolean add(UUID uuid, BanReason reason, Duration duration) throws SQLException {
        if (get(uuid) != null) return false;

        PreparedStatement ps = _connection.prepareStatement("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)"
                .formatted(TABLE, COLUMN_UUID, COLUMN_REASON, COLUMN_TIMEBANNED));
        ps.setString(1, uuid.toString());
        ps.setString(2, reason.name());
        ps.setString(3, duration.toString());

        ps.execute();
        ps.close();
        return true;
    }

    public boolean remove(UUID uuid) throws SQLException {
        if (get(uuid) != null) return false;

        PreparedStatement ps = _connection.prepareStatement("DELETE FROM %s WHERE %s = ?"
                .formatted(TABLE, COLUMN_UUID));
        ps.setString(1, uuid.toString());

        ps.execute();
        ps.close();
        return true;
    }

    public PlayerBan get(UUID uuid) throws SQLException {
        PreparedStatement ps = _connection.prepareStatement("SELECT * FROM %s WHERE %s = ?"
                .formatted(TABLE, COLUMN_UUID));
        ps.setString(1, uuid.toString());
        ResultSet rs = ps.executeQuery();

        if (rs.first()) {
            var ban = new PlayerBan();
            ban.setUuid(UUID.fromString(rs.getString(COLUMN_UUID)));
            ban.setReason(BanReason.valueOf(rs.getString(COLUMN_REASON)));
            ban.setBanDuration(Duration.parse(rs.getString(COLUMN_TIMEBANNED)));
            return ban;
        }
        return null;
    }
}
