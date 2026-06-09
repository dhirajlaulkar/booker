package booker.services;

import booker.entities.Ticket;
import booker.entities.Train;
import booker.entities.User;
import booker.exceptions.BadRequestException;
import booker.exceptions.ResourceNotFoundException;
import booker.repositories.TrainRepository;
import booker.repositories.UserRepository;
import booker.dtos.BookSeatRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainRepository trainRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void signUp_Success() {
        String username = "testUser";
        String password = "password123";
        when(userRepository.findByName(username)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.signUp(username, password);

        assertNotNull(user);
        assertEquals(username, user.getName());
        assertNotNull(user.getHashedPassword());
        assertNotNull(user.getUserId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void signUp_UsernameExists_ThrowsBadRequest() {
        String username = "existingUser";
        String password = "password123";
        User existingUser = new User(username, password, "hashedPass", new ArrayList<>(), "userId");
        when(userRepository.findByName(username)).thenReturn(Optional.of(existingUser));

        assertThrows(BadRequestException.class, () -> userService.signUp(username, password));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void login_Success() {
        String username = "testUser";
        String password = "password123";
        String hashedPassword = booker.util.UserServiceUtil.hashPassword(password);
        User user = new User(username, password, hashedPassword, new ArrayList<>(), "userId");
        
        when(userRepository.findByName(username)).thenReturn(Optional.of(user));

        User result = userService.login(username, password);

        assertNotNull(result);
        assertEquals(username, result.getName());
    }

    @Test
    public void login_InvalidPassword_ThrowsBadRequest() {
        String username = "testUser";
        String password = "password123";
        String hashedPassword = booker.util.UserServiceUtil.hashPassword("correctPassword");
        User user = new User(username, "correctPassword", hashedPassword, new ArrayList<>(), "userId");
        
        when(userRepository.findByName(username)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.login(username, password));
    }

    @Test
    public void login_UserNotFound_ThrowsBadRequest() {
        String username = "nonexistentUser";
        when(userRepository.findByName(username)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> userService.login(username, "pass"));
    }

    @Test
    public void getBookings_Success() {
        String username = "testUser";
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket("ticket1", username, "StationA", "StationB", "2026-06-09", null));
        User user = new User(username, "pass", "hash", tickets, "userId");

        when(userRepository.findByName(username)).thenReturn(Optional.of(user));

        List<Ticket> result = userService.getBookings(username);

        assertEquals(1, result.size());
        assertEquals("ticket1", result.get(0).getTicketId());
    }

    @Test
    public void cancelBooking_Success() {
        String username = "testUser";
        String ticketId = "ticket1";
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket(ticketId, username, "StationA", "StationB", "2026-06-09", null));
        User user = new User(username, "pass", "hash", tickets, "userId");

        when(userRepository.findByName(username)).thenReturn(Optional.of(user));

        boolean result = userService.cancelBooking(username, ticketId);

        assertTrue(result);
        assertTrue(user.getTicketsBooked().isEmpty());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void cancelBooking_TicketNotFound_ThrowsResourceNotFound() {
        String username = "testUser";
        List<Ticket> tickets = new ArrayList<>();
        User user = new User(username, "pass", "hash", tickets, "userId");

        when(userRepository.findByName(username)).thenReturn(Optional.of(user));

        assertThrows(ResourceNotFoundException.class, () -> userService.cancelBooking(username, "nonexistent"));
    }

    @Test
    public void bookSeat_Success() {
        String username = "testUser";
        User user = new User(username, "pass", "hash", new ArrayList<>(), "userId");
        
        List<List<Integer>> seats = new ArrayList<>();
        seats.add(new ArrayList<>(Arrays.asList(0, 0)));
        Train train = new Train("trainId", "123", seats, new HashMap<>(), new ArrayList<>());

        when(userRepository.findByName(username)).thenReturn(Optional.of(user));
        when(trainRepository.findById("trainId")).thenReturn(Optional.of(train));

        BookSeatRequest request = new BookSeatRequest(username, 0, 1, "StationA", "StationB", "2026-06-09");
        Ticket ticket = userService.bookSeat("trainId", request);

        assertNotNull(ticket);
        assertEquals(username, ticket.getUserId());
        assertEquals(1, train.getSeats().get(0).get(1));
        assertEquals(1, user.getTicketsBooked().size());
        verify(trainRepository, times(1)).save(train);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void bookSeat_AlreadyBooked_ThrowsBadRequest() {
        String username = "testUser";
        User user = new User(username, "pass", "hash", new ArrayList<>(), "userId");
        
        List<List<Integer>> seats = new ArrayList<>();
        seats.add(new ArrayList<>(Arrays.asList(0, 1)));
        Train train = new Train("trainId", "123", seats, new HashMap<>(), new ArrayList<>());

        when(userRepository.findByName(username)).thenReturn(Optional.of(user));
        when(trainRepository.findById("trainId")).thenReturn(Optional.of(train));

        BookSeatRequest request = new BookSeatRequest(username, 0, 1, "StationA", "StationB", "2026-06-09");

        assertThrows(BadRequestException.class, () -> userService.bookSeat("trainId", request));
        verify(trainRepository, never()).save(any(Train.class));
    }
}
