package com.example.TODO.DB;

import com.example.TODO.Models.User;
import com.mongodb.client.*;
import org.bson.Document;
import java.util.Optional;

public class UserRepository {
    private static final MongoClient client = MongoClients.create("mongodb://localhost:27017");
    private static final MongoDatabase db = client.getDatabase("todo_app");
    private static final MongoCollection<Document> users = db.getCollection("users");

    public static void save(User user) {
        Document doc = new Document("name", user.getName())
                .append("email", user.getEmail())
                .append("password", user.getPassword());
        users.insertOne(doc);
    }

    public static Optional<User> findByEmail(String email) {
        Document query = new Document("email", email);
        Document result = users.find(query).first();
        if (result != null) {
            return Optional.of(new User(
                    result.getString("name"),
                    result.getString("email"),
                    result.getString("password")
            ));
        }
        return Optional.empty();
    }

    public static boolean existsByEmail(String email) {
        return users.countDocuments(new Document("email", email)) > 0;
    }
}
