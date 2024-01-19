package de.noque.backend.model;

import de.noque.backend.model.enums.BanReason;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Data;
import org.bson.types.ObjectId;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity("bans") @Data
public class PlayerBan {

    private @Id ObjectId Id;
    private UUID Uuid;
    private Date TimeBanned;
    private BanReason Reason;
    private Date BannedUntil;

    public PlayerBan() {}

    public PlayerBan(UUID uuid, BanReason reason, Duration duration) {
        Uuid = uuid;
        Reason = reason;
        TimeBanned = Date.from(Instant.from(LocalDate.now()));

        var instant = TimeBanned.toInstant().plus(duration);
        BannedUntil = Date.from(instant);
    }
}
