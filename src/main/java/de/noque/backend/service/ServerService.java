package de.noque.backend.service;

import com.mongodb.client.model.Filters;
import de.noque.backend.Network;
import de.noque.backend.model.ServerObject;
import de.noque.backend.model.enums.State;
import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filter;

import java.util.List;

public class ServerService {

    private final Datastore _datastore;

    public ServerService(Network network) {
        _datastore = network.getMongoManager().getDatastore();
    }

    public ServerObject loadFromName(String name) {
        return _datastore.find(ServerObject.class)
                .filter((Filter) Filters.eq("name", name)).first();
    }

    public List<ServerObject> loadAllFromGameMode(String gameMode) {
        return _datastore.find(ServerObject.class)
                .filter((Filter) Filters.eq("gameMode", gameMode)).iterator().toList();
    }

    public List<ServerObject> loadAll() {
        return _datastore.find(ServerObject.class).iterator().toList();
    }

    public boolean add(String name, String gameMode) {
        if (loadFromName(name) != null) return false;

        var document = new ServerObject(name, State.WAITING, gameMode);
        _datastore.save(document);
        return true;
    }

    public boolean remove(String name) {
        var document = _datastore.find(ServerObject.class).filter((Filter) Filters.eq("name", name)).first();

        if (document == null) return false;

        _datastore.delete(document);
        return true;
    }
}
