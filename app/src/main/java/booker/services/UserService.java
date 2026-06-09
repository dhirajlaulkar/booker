package booker.services;

import booker.entities.Ticket;
import booker.entities.Train;
import booker.entities.User;
import booker.exceptions.BadRequestException;
import booker.exceptions.ResourceNotFoundException;
import booker.repositories.TrainRepository;
import booker.repositories.UserRepository;
import booker.util.UserServiceUtil;
import booker.dtos.BookSeatRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TrainRepository trainRepository;

    public UserService(UserRepository userRepository, TrainRepository trainRepository) {
        this.userRepository = userRepository;
        this.trainRepository = trainRepository;
    }

    public User signUp(String name, String password) {
        Optional<User> existingUser = userRepository.findByName(name);
        if (existingUser.isPresent()) {
            throw new BadRequestException("Username '" + name + "' already exists");
        }
        
        String hashedPassword = UserServiceUtil.hashPassword(password);
        User user = new User(
                name,
                password,
                hashedPassword,
                new ArrayList<>(),
                UUID.randomUUID().toString()
        );
        return userRepository.save(user);
    }

    public User login(String name, String password) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        
        if (!UserServiceUtil.checkPassword(password, user.getHashedPassword())) {
            throw new BadRequestException("Invalid credentials");
        }
        
        return user;
    }

    public List<Ticket> getBookings(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getTicketsBooked() != null ? user.getTicketsBooked() : new ArrayList<>();
    }

    public boolean cancelBooking(String name, String ticketId) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getTicketsBooked() == null) {
            throw new BadRequestException("No bookings found for user");
        }

        boolean removed = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(ticketId));
        if (removed) {
            userRepository.save(user);
            return true;
        } else {
            throw new ResourceNotFoundException("Ticket ID '" + ticketId + "' not found for this user");
        }
    }

    public Ticket bookSeat(String trainId, BookSeatRequest request) {
        User user = userRepository.findByName(request.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new ResourceNotFoundException("Train not found"));

        List<List<Integer>> seats = train.getSeats();
        int row = request.getRow();
        int col = request.getCol();

        if (row < 0 || row >= seats.size() || col < 0 || col >= seats.get(row).size()) {
            throw new BadRequestException("Invalid seat coordinates");
        }

        if (seats.get(row).get(col) != 0) {
            throw new BadRequestException("Seat already booked");
        }

        seats.get(row).set(col, 1);
        train.setSeats(seats);
        trainRepository.save(train);

        Ticket ticket = new Ticket(
                UUID.randomUUID().toString(),
                user.getName(),
                request.getSource(),
                request.getDestination(),
                request.getDateOfTravel(),
                train
        );

        if (user.getTicketsBooked() == null) {
            user.setTicketsBooked(new ArrayList<>());
        }
        user.getTicketsBooked().add(ticket);
        userRepository.save(user);

        return ticket;
    }
}
