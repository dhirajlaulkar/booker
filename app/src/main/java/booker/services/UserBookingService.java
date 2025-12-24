package booker.services;

import booker.entities.User;
import booker.util.UserServiceUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private User user;
    private List<User> userList;
    private ObjectMapper objectMapper = new ObjectMapper();

    private final String USERS_PATH = "app/src/main/java/booker/localDb/users.json";

    public UserBookingService(User user1) throws IOException {
        this.user = user1;
        loadUsers();
    }

    public UserBookingService() throws IOException {
        loadUsers();
    }

    public List<User> loadUsers() throws IOException {
        File users = new File(USERS_PATH);
        if (users.exists()) {
            userList = objectMapper.readValue(users, new TypeReference<List<User>>() {
            });
        } else {
            userList = new ArrayList<>();
        }
        return userList;
    }

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream().filter(user1 -> user1.getName().equals(user.getName())
                && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword())).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1) {
        try {
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException ex) {
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USERS_PATH);
        objectMapper.writeValue(usersFile, userList);
    }

    public void fetchBooking() {
        if (user != null) {
            user.printTickets();
        }
    }

    public Boolean cancelBooking(String ticketId) {
        try {
            boolean ticketFound = userList.stream()
                    .filter(u -> u.getTicketsBooked() != null)
                    .anyMatch(u -> u.getTicketsBooked()
                            .removeIf(ticket -> ticket.getTicketId() != null && ticket.getTicketId().equals(ticketId)));

            if (ticketFound) {
                saveUserListToFile();
                return Boolean.TRUE;
            }
            return Boolean.FALSE;

        } catch (IOException e) {
            System.err.println("Error saving user list to file: " + e.getMessage());
            return Boolean.FALSE;
        }
    }

    public List<Train> getTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.getTrains(source, destination);
        } catch (IOException e) {
            System.err.println("Error getting trains: " + e.getMessage());
            return null;
        }
    }
}
