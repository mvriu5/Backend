package de.noque.backend.service;

import com.mongodb.client.model.Filters;
import de.noque.backend.Network;
import de.noque.backend.model.PlayerDocument;
import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filter;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerService {

    private final Datastore _datastore;

    public PlayerService(Network network) {
        _datastore = network.getMongoManager().getDatastore();
    }

    public void add(Player player) {
        var query = _datastore.find(PlayerDocument.class)
                .filter((Filter) Filters.eq("uuid", player.getUniqueId()));

        if (query == null) {
            var document = new PlayerDocument(player.getUniqueId());
            _datastore.save(document);
        }
    }

    public void remove(Player player) {
        var document = _datastore.find(PlayerDocument.class)
                .filter((Filter) Filters.eq("uuid", player.getUniqueId()));

        if (document != null) _datastore.delete(document.delete());
    }

    public PlayerDocument get(UUID uuid) {
        return _datastore.find(PlayerDocument.class)
                .filter((Filter) Filters.eq("uuid", uuid)).first();
    }

    public UUID getUUID(String name) {
        return _datastore.find(PlayerDocument.class)
                .filter((Filter) Filters.eq("name", name)).first().getUuid();
    }
}
