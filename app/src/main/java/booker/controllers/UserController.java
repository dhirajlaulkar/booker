package booker.controllers;

import booker.entities.Ticket;
import booker.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Operations", description = "Endpoints for managing user bookings")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}/bookings")
    @Operation(summary = "Get user bookings", description = "Retrieves all booked tickets for a given username")
    public ResponseEntity<List<Ticket>> getBookings(@PathVariable String username) {
        List<Ticket> bookings = userService.getBookings(username);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping("/{username}/bookings/{ticketId}/cancel")
    @Operation(summary = "Cancel booking", description = "Cancels a booked ticket by ID for the specified username")
    public ResponseEntity<String> cancelBooking(@PathVariable String username, @PathVariable String ticketId) {
        userService.cancelBooking(username, ticketId);
        return ResponseEntity.ok("Ticket successfully cancelled");
    }
}
