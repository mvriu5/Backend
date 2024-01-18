package de.noque.backend.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Data;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity("friendrequests") @Data
public class FriendRequest {

    private @Id ObjectId Id;
    private UUID Sender;
    private UUID Target;
    private Date TimeSent;

    public FriendRequest() {}

    public FriendRequest(UUID sender, UUID target) {
        Sender = sender;
        Target = target;
        TimeSent = Date.from(Instant.from(LocalDate.now()));
    }
}
