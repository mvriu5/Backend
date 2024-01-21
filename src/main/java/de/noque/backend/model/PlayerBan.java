package de.noque.backend.model;

import de.noque.backend.model.enums.BanReason;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
public class PlayerBan {

    private UUID Uuid;
    private Duration BanDuration;
    private BanReason Reason;
    private Date BannedUntil;

    public PlayerBan() {}

    public PlayerBan(UUID uuid, BanReason reason, Duration banDuration) {
        Uuid = uuid;
        Reason = reason;
        BanDuration = banDuration;
        BannedUntil = new Date(new Date().getTime() + BanDuration.toDays());
    }
}
