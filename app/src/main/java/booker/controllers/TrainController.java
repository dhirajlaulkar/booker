package booker.controllers;

import booker.dtos.BookSeatRequest;
import booker.entities.Ticket;
import booker.entities.Train;
import booker.services.TrainService;
import booker.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trains")
@Tag(name = "Train Operations", description = "Endpoints for searching trains and booking seats")
public class TrainController {

    private final TrainService trainService;
    private final UserService userService;

    public TrainController(TrainService trainService, UserService userService) {
        this.trainService = trainService;
        this.userService = userService;
    }

    @GetMapping("/search")
    @Operation(summary = "Search trains", description = "Finds available trains running between source and destination stations")
    public ResponseEntity<List<Train>> searchTrains(
            @RequestParam String source,
            @RequestParam String destination) {
        List<Train> trains = trainService.searchTrains(source, destination);
        return ResponseEntity.ok(trains);
    }

    @GetMapping("/{trainId}")
    @Operation(summary = "Get train details", description = "Retrieves the details of a train by its ID")
    public ResponseEntity<Train> getTrainById(@PathVariable String trainId) {
        Train train = trainService.getTrainById(trainId);
        return ResponseEntity.ok(train);
    }

    @GetMapping("/{trainId}/seats")
    @Operation(summary = "Get seat layout", description = "Retrieves the 2D grid representing available and booked seats")
    public ResponseEntity<List<List<Integer>>> getSeats(@PathVariable String trainId) {
        Train train = trainService.getTrainById(trainId);
        return ResponseEntity.ok(train.getSeats());
    }

    @PostMapping("/{trainId}/book")
    @Operation(summary = "Book a train seat", description = "Books a specific seat (by row and col) on the train for the given user")
    public ResponseEntity<Ticket> bookSeat(
            @PathVariable String trainId,
            @Valid @RequestBody BookSeatRequest request) {
        Ticket ticket = userService.bookSeat(trainId, request);
        return new ResponseEntity<>(ticket, HttpStatus.CREATED);
    }

    @PostMapping
    @Operation(summary = "Add or update a train", description = "Registers a new train or updates an existing one")
    public ResponseEntity<Train> saveTrain(@RequestBody Train train) {
        Train saved = trainService.addTrain(train);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
