package de.noque.backend.model;

import de.noque.backend.model.enums.State;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class ServerObject {

    private String Name;
    private State State;
    private String GameMode;

    public ServerObject() {}

    public ServerObject(String name, State state, String gameMode) {
        Name = name;
        State = state;
        GameMode = gameMode;
    }
}
