package de.noque.backend.service;

import com.mongodb.client.model.Filters;
import de.noque.backend.Network;
import de.noque.backend.model.PlayerBan;
import de.noque.backend.model.PlayerObject;
import de.noque.backend.model.enums.BanReason;
import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filter;

import java.time.Duration;
import java.util.UUID;

public class BanService {

    private final Datastore _datastore;

    public BanService(Network network) {
        _datastore = network.getMongoManager().getDatastore();
    }

    public boolean add(UUID uuid, BanReason reason, Duration duration) {
        var query = _datastore.find(PlayerObject.class)
                .filter((Filter) Filters.eq("uuid", uuid));

        if (query != null) return false;

        var document = new PlayerBan(uuid, reason, duration);
        _datastore.save(document);
        return true;
    }

    public boolean remove(UUID uuid) {
        var document = _datastore.find(PlayerBan.class)
                .filter((Filter) Filters.eq("uuid", uuid)).first();

        if (document == null) return false;

        _datastore.delete(document);
        return true;
    }

    public PlayerBan get(UUID uuid) {
        return _datastore.find(PlayerBan.class)
                .filter((Filter) Filters.eq("uuid", uuid)).first();
    }
}
