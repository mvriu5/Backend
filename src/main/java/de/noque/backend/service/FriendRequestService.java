package de.noque.backend.service;

import com.mongodb.client.model.Filters;
import de.noque.backend.Network;
import de.noque.backend.model.FriendRequest;
import de.noque.backend.model.PlayerObject;
import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filter;

import java.util.List;
import java.util.UUID;

public class FriendRequestService {

    private final Datastore _datastore;

    public FriendRequestService(Network network) {
        _datastore = network.getMongoManager().getDatastore();
    }

    public boolean add(UUID sender, UUID target) {
        var query = _datastore.find(PlayerObject.class)
                .filter((Filter) Filters.eq("uuid", sender))
                .filter((Filter) Filters.eq("uuid", target)).first();

        if (query != null) return false;

        var document = new FriendRequest(sender, target);
        _datastore.save(document);
        return true;
    }

    public boolean remove(UUID sender, UUID target) {
        var document = _datastore.find(FriendRequest.class)
                .filter((Filter) Filters.eq("sender", sender))
                .filter((Filter) Filters.eq("target", target)).first();

        if (document == null) return false;

        _datastore.delete(document);
        return true;
    }

    public List<FriendRequest> getRequests(UUID target) {
        return _datastore.find(FriendRequest.class)
                .filter((Filter) Filters.eq("target", target)).iterator().toList();
    }
}
