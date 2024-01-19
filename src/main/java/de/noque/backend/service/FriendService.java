package de.noque.backend.service;

import com.mongodb.client.model.Filters;
import de.noque.backend.Network;
import de.noque.backend.model.Friend;
import de.noque.backend.model.PlayerObject;
import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filter;
import dev.morphia.query.experimental.updates.UpdateOperators;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class FriendService {

    private final Datastore _datastore;

    public FriendService(Network network) {
        _datastore = network.getMongoManager().getDatastore();
    }

    public void add(Player player) {
        var query = _datastore.find(Friend.class)
                .filter((Filter) Filters.eq("uuid", player.getUniqueId()));

        if (query == null) {
            var document = new Friend(player.getUniqueId());
            _datastore.save(document);
        }
    }

    public void addFriend(UUID player, UUID target) {
        _datastore.find(Friend.class)
                .filter((Filter) Filters.eq("", player))
                .update(UpdateOperators.addToSet("friends", target));

        _datastore.find(Friend.class)
                .filter((Filter) Filters.eq("", target))
                .update(UpdateOperators.addToSet("friends", player));
    }

    public void removeFriend(UUID player, UUID target) {
        _datastore.find(Friend.class)
                .filter((Filter) Filters.eq("", player))
                .update(UpdateOperators.pullAll("friends", List.of(target)));

        _datastore.find(Friend.class)
                .filter((Filter) Filters.eq("", target))
                .update(UpdateOperators.pullAll("friends", List.of(player)));
    }

    public List<PlayerObject> getFriends(Player player) {
        var document = _datastore.find(Friend.class)
                .filter((Filter) Filters.eq("uuid", player.getUniqueId())).first();

        if (document == null) return null;

        return document.getFriends();
    }
}
