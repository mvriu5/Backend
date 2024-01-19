package de.noque.backend.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Data;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity("players") @Data
public class PlayerObject {

    private @Id ObjectId Id;
    private UUID Uuid;
    private String Name;
    private Date FirstJoin;

    public PlayerObject() {}

    public PlayerObject(UUID uuid, String name) {
        Uuid = uuid;
        Name = name;
        FirstJoin = Date.from(Instant.from(LocalDate.now()));

    }
}
