package de.noque.backend.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

@Entity("friends") @Data
public class Friend {

    private @Id ObjectId Id;
    private UUID Uuid;
    private @Reference List<PlayerObject> Friends;

    public Friend() {}

    public Friend(UUID uuid) {
        Uuid = uuid;
    }
}
