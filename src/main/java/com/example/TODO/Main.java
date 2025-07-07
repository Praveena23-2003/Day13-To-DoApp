package com.example.TODO;

import com.example.TODO.Models.User;
import com.example.TODO.Utils.AuthUtil;
import com.example.TODO.Utils.TaskUtil;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        User loggedInUser = null;

        // 🔁 Background Reminder Thread
        new Thread(() -> {
            while (true) {
                TaskUtil.checkReminders();
                try {
                    Thread.sleep(60000); // check every 60 seconds
                } catch (InterruptedException ignored) {
                }
            }
        }).start();

        while (true) {
            System.out.println("\n========= TO-DO MENU =========");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Create Task");
            System.out.println("4. List Tasks");
            System.out.println("5. Mark Task Complete/Incomplete");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            String input = sc.nextLine();
            if (input.isEmpty()) {
                System.out.println("❌ Invalid input. Please enter a number.");
                continue;
            }

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number. Please enter a valid menu option.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    AuthUtil.register(name, email);
                }
                case 2 -> {
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    System.out.print("Password: ");
                    String pwd = sc.nextLine();
                    loggedInUser = AuthUtil.login(email, pwd);
                    if (loggedInUser != null) {
                        System.out.println("✅ Login successful. Welcome, " + loggedInUser.getName() + "!");
                    }
                }
                case 3 -> {
                    if (loggedInUser != null) {
                        TaskUtil.createTask(loggedInUser.getEmail(), sc);
                    } else {
                        System.out.println("⚠️ Please log in first to create a task.");
                    }
                }
                case 4 -> {
                    if (loggedInUser != null) {
                        TaskUtil.listTasks(loggedInUser.getEmail(), sc);
                    } else {
                        System.out.println("⚠️ Please log in first to view tasks.");
                    }
                }
                case 5 -> {
                    if (loggedInUser != null) {
                        TaskUtil.toggleComplete(loggedInUser.getEmail(), sc);
                    } else {
                        System.out.println("⚠️ Please log in first to update tasks.");
                    }
                }
                case 6 -> {
                    System.out.println("👋 Goodbye!");
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }
}
