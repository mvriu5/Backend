package de.noque.backend.service;

import com.mongodb.client.model.Filters;
import de.noque.backend.Network;
import de.noque.backend.model.FriendRequest;
import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filter;

import java.util.List;
import java.util.UUID;

public class FriendRequestService {

    private final Datastore _datastore;

    public FriendRequestService(Network network) {
        _datastore = network.getMongoManager().getDatastore();
    }

    public void add(UUID sender, UUID target) {
        var document = new FriendRequest(sender, target);
        _datastore.save(document);
    }

    public void remove(UUID sender, UUID target) {
        var document = _datastore.find(FriendRequest.class)
                .filter((Filter) Filters.eq("sender", sender))
                .filter((Filter) Filters.eq("target", target)).first();

        if (document != null) _datastore.delete(document);
    }

    public boolean checkRequest(UUID sender, UUID target) {

        return false;
    }

    public List<FriendRequest> getRequests(UUID target) {
        return _datastore.find(FriendRequest.class)
                .filter((Filter) Filters.eq("target", target)).iterator().toList();
    }
}
