package de.noque.backend;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bson.Document;
import org.bukkit.Bukkit;

public class MongoManager {

    private final MongoClient client;
    private final @Getter Datastore datastore;

    @Getter
    private MongoCollection<Document> serverCollection, friendCollection, friendRequestCollection;

    public MongoManager(Network network) {

        client = new MongoClient();
        MongoDatabase _database = client.getDatabase("db_name");

        serverCollection = _database.getCollection("servers");
        friendCollection = _database.getCollection("friends");
        friendRequestCollection = _database.getCollection("friendrequests");

        datastore = Morphia.createDatastore(MongoClients.create(""), "");
        datastore.getMapper().mapPackage("com.mongodb.morphia.entities");
        datastore.ensureIndexes();

        Bukkit.getConsoleSender().sendMessage(Component.text("Connected to MongoDB!", NamedTextColor.GREEN));
    }

    public void disconnect() {
        if (client != null)
            client.close();

        Bukkit.getConsoleSender().sendMessage(Component.text("Disonnected from MongoDB!", NamedTextColor.RED));
    }
}
