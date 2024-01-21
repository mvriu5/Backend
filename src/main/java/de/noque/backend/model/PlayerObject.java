package de.noque.backend.model;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class PlayerObject {

    private UUID Uuid;
    private String Name;
    private Date FirstJoin;
    private List<UUID> Friends;

    public PlayerObject() {}

    public PlayerObject(UUID uuid, String name) {
        Uuid = uuid;
        Name = name;
        FirstJoin = Date.from(Instant.from(LocalDate.now()));

    }
}
