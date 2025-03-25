package la1;

import java.io.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

public class UserManager {
    private final Map<String, User> users = new HashMap<>();
    private final String userFile = "users.txt";  // Save the user

    // Initialize the load user
    public UserManager() {
        loadUsers();
    }

    // Register (salt + hash)
    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) return false;

        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        users.put(username, new User(username, salt, hashedPassword));
        saveUsers();
        return true;
    }

    // Login authentication
    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null) {
            String hashedInput = hashPassword(password, user.getSalt());
            if (hashedInput.equals(user.getHashedPassword())) {
                return user;
            }
        }
        return null;
    }

    // Load users
    private void loadUsers() {
        File file = new File(userFile);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    users.put(parts[0], new User(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load users.");
        }
    }

    // Save users
    private void saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(userFile))) {
            for (User user : users.values()) {
                pw.println(user.getUsername() + "," + user.getSalt() + "," + user.getHashedPassword());
            }
        } catch (IOException e) {
            System.out.println("Failed to save users.");
        }
    }

    // Generate salt
    private String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Generate hash
    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String salted = password + salt;
            byte[] hash = md.digest(salted.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Hash error");
        }
    }
}