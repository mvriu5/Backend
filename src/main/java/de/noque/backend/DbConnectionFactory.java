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
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionFactory {

    public DbConnectionFactory(Network network) {}

    public Connection connect() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myDb", "user1", "pass");

        if (connection != null) {
            Bukkit.getConsoleSender().sendMessage(
                    MiniMessage.miniMessage().deserialize("<green>Connected to DB!"));
        }

        return connection;
    }

    public void disconnect() {
        Bukkit.getConsoleSender().sendMessage(
                MiniMessage.miniMessage().deserialize("<red>Disconnected from DB!"));
    }
}
