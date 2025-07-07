package com.example.TODO.Utils;

import com.example.TODO.DB.UserRepository;
import com.example.TODO.Models.User;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;
import java.util.UUID;

public class AuthUtil {

    public static void register(String name, String email) {
        if (UserRepository.existsByEmail(email)) {
            System.out.println("Email already exists.");
            return;
        }

        String rawPassword = UUID.randomUUID().toString().substring(0, 10);
        String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        User user = new User(name, email, hashed);
        UserRepository.save(user);

        new Thread(() -> EmailUtil.send(email, rawPassword)).start();
        System.out.println("User registered. Password sent via email.");
    }

    public static User login(String email, String password) {
        Optional<User> userOpt = UserRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }
        }
        System.out.println("Invalid credentials.");
        return null;
    }
}
