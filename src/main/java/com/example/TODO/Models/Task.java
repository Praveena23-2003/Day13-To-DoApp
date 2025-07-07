package com.example.TODO.Models;

import org.bson.Document;
import java.time.LocalDateTime;

public class Task {
    private String id;
    private String userEmail;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String priority;
    private boolean isCompleted;
    private LocalDateTime createdAt;
    private boolean reminderSent;

    public Task() {}

    public Task(String userEmail, String title, String description, LocalDateTime dueDate, String priority) {
        this.userEmail = userEmail;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.isCompleted = false;
        this.createdAt = LocalDateTime.now();
        this.reminderSent = false;
    }

    // Getters & Setters...

    public static Task fromDocument(Document doc) {
        Task t = new Task();
        t.id = doc.getObjectId("_id").toHexString();
        t.userEmail = doc.getString("userEmail");
        t.title = doc.getString("title");
        t.description = doc.getString("description");
        t.dueDate = LocalDateTime.parse(doc.getString("dueDate"));
        t.priority = doc.getString("priority");
        t.isCompleted = doc.getBoolean("isCompleted", false);
        t.createdAt = LocalDateTime.parse(doc.getString("createdAt"));
        t.reminderSent = doc.getBoolean("reminderSent", false);
        return t;
    }

    public String getId() { return id; }
    public boolean isReminderSent() { return reminderSent; }
    public void setReminderSent(boolean reminderSent) { this.reminderSent = reminderSent; }
    public boolean isCompleted() { return isCompleted; }
    public String getUserEmail() { return userEmail; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getDueDate() { return dueDate; }
    public String getPriority() { return priority; }
    public LocalDateTime getCreatedAt() { return createdAt; }

}
