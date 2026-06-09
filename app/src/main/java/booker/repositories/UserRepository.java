package booker.repositories;

import booker.entities.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final String usersPath;
    private final ObjectMapper objectMapper;

    public UserRepository(@Value("${booker.db.users-path:app/src/main/java/booker/localDb/user.json}") String usersPath,
                          ObjectMapper objectMapper) {
        this.usersPath = usersPath;
        this.objectMapper = objectMapper;
    }

    public List<User> findAll() {
        try {
            File file = new File(usersPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<User>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read users from file database", e);
        }
    }

    public Optional<User> findByName(String name) {
        return findAll().stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public Optional<User> findById(String id) {
        return findAll().stream()
                .filter(user -> user.getUserId().equals(id))
                .findFirst();
    }

    public synchronized User save(User user) {
        List<User> users = findAll();
        users.removeIf(u -> u.getUserId().equals(user.getUserId()) || u.getName().equalsIgnoreCase(user.getName()));
        users.add(user);
        saveAll(users);
        return user;
    }

    public synchronized void saveAll(List<User> users) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(usersPath), users);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write users to file database", e);
        }
    }
}
