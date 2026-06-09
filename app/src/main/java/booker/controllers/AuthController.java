package booker.controllers;

import booker.dtos.LoginRequest;
import booker.dtos.SignUpRequest;
import booker.entities.User;
import booker.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and authentication")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Register a new user", description = "Creates a new user with hashed password and unique user ID")
    public ResponseEntity<User> signUp(@Valid @RequestBody SignUpRequest request) {
        User registeredUser = userService.signUp(request.getName(), request.getPassword());
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Validates credentials and returns user details")
    public ResponseEntity<User> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request.getName(), request.getPassword());
        return ResponseEntity.ok(user);
    }
}
