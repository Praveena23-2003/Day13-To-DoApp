package com.example.TODO.Utils;

import com.example.TODO.DB.TaskRepository;
import com.example.TODO.Models.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class TaskUtil {

    public static void createTask(String userEmail, Scanner sc) {
        System.out.print("Title: ");
        String title = sc.nextLine();
        System.out.print("Description: ");
        String desc = sc.nextLine();
        System.out.print("Due Date (YYYY-MM-DD HH:MM): ");
        String dueInput = sc.nextLine();
        System.out.print("Priority (Low/Medium/High): ");
        String priority = sc.nextLine();

        LocalDateTime dueDate = LocalDateTime.parse(dueInput.replace(" ", "T"));

        Task task = new Task(userEmail, title, desc, dueDate, priority);
        TaskRepository.save(task);
        System.out.println("✅ Task created.");
    }

    public static void listTasks(String userEmail, Scanner sc) {
        System.out.print("Filter by Priority (Low/Medium/High) or leave blank: ");
        String priority = sc.nextLine();
        System.out.print("Sort by due date? (y/n): ");
        boolean sort = sc.nextLine().equalsIgnoreCase("y");

        System.out.print("Page number: ");
        int page = Integer.parseInt(sc.nextLine());
        System.out.print("Page size: ");
        int size = Integer.parseInt(sc.nextLine());

        List<Task> tasks = TaskRepository.listTasks(userEmail, priority, sort, page, size);
        if (tasks.isEmpty()) {
            System.out.println("⚠️ No tasks found.");
            return;
        }

        for (Task task : tasks) {
            System.out.printf("\n[%s] %s\n", task.isCompleted() ? "X" : " ", task.getTitle());
            System.out.println("  ID: " + task.getId());
            System.out.println("  Description: " + task.getDescription());
            System.out.println("  Due: " + task.getDueDate());
            System.out.println("  Priority: " + task.getPriority());
            System.out.println("  Created: " + task.getCreatedAt());
        }
    }

    public static void toggleComplete(String userEmail, Scanner sc) {
        System.out.print("Enter Task ID: ");
        String id = sc.nextLine().trim();

        // ✅ Validate ObjectId format
        if (id.length() != 24 || !id.matches("^[a-fA-F0-9]+$")) {
            System.out.println("❌ Invalid Task ID. Must be a 24-character hex string.");
            return;
        }

        System.out.print("Mark (1) Complete or (2) Incomplete? ");
        int choice = Integer.parseInt(sc.nextLine());
        boolean isComplete = (choice == 1);

        TaskRepository.markTask(id, isComplete);
        System.out.println("✅ Task status updated.");
    }

    public static void checkReminders() {
        List<Task> tasks = TaskRepository.findAll();
        for (Task task : tasks) {
            if (!task.isReminderSent()
                    && !task.isCompleted()
                    && task.getDueDate().minusMinutes(10).isBefore(LocalDateTime.now())) {

                EmailUtil.send(task.getUserEmail(),
                        "⏰ Reminder: Task '" + task.getTitle() + "' is due at " + task.getDueDate());
                TaskRepository.markReminderSent(task.getId());
            }
        }
    }
}
