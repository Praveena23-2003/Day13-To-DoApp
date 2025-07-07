package com.example.TODO.DB;

import com.example.TODO.Models.Task;
import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static final MongoClient client = MongoClients.create("mongodb://localhost:27017");
    private static final MongoDatabase db = client.getDatabase("todo_app");
    private static final MongoCollection<Document> tasks = db.getCollection("tasks");

    public static void save(Task task) {
        Document doc = new Document("userEmail", task.getUserEmail())
                .append("title", task.getTitle())
                .append("description", task.getDescription())
                .append("dueDate", task.getDueDate().toString())
                .append("priority", task.getPriority())
                .append("isCompleted", task.isCompleted())
                .append("createdAt", task.getCreatedAt().toString())
                .append("reminderSent", false);
        tasks.insertOne(doc);
    }

    public static List<Task> findByUser(String email) {
        List<Task> result = new ArrayList<>();
        for (Document doc : tasks.find(new Document("userEmail", email))) {
            result.add(Task.fromDocument(doc));
        }
        return result;
    }

    public static List<Task> findAll() {
        List<Task> result = new ArrayList<>();
        for (Document doc : tasks.find()) {
            result.add(Task.fromDocument(doc));
        }
        return result;
    }

    public static void markTask(String id, boolean completed) {
        tasks.updateOne(new Document("_id", new ObjectId(id)),
                new Document("$set", new Document("isCompleted", completed)));
    }

    public static void markReminderSent(String id) {
        tasks.updateOne(new Document("_id", new ObjectId(id)),
                new Document("$set", new Document("reminderSent", true)));
    }

    public static void updateCompletion(String id, boolean completed) {
        tasks.updateOne(new Document("_id", new ObjectId(id)),
                new Document("$set", new Document("isCompleted", completed)));
    }

    public static List<Task> listTasks(String userEmail, String priority, boolean sortByDue, int page, int size) {
        Document query = new Document("userEmail", userEmail);
        if (priority != null && !priority.isEmpty()) {
            query.append("priority", priority);
        }

        FindIterable<Document> docs = tasks.find(query);
        if (sortByDue) docs = docs.sort(Sorts.ascending("dueDate"));
        docs = docs.skip(page * size).limit(size);

        List<Task> result = new ArrayList<>();
        for (Document doc : docs) {
            result.add(Task.fromDocument(doc));
        }
        return result;
    }
}
