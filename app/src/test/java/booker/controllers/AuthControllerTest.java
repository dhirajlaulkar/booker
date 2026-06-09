package booker.controllers;

import booker.dtos.LoginRequest;
import booker.dtos.SignUpRequest;
import booker.entities.User;
import booker.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void signUp_Success() throws Exception {
        SignUpRequest request = new SignUpRequest("newUser", "password123");
        User user = new User("newUser", "password123", "hashedPass", new ArrayList<>(), "userId");

        when(userService.signUp("newUser", "password123")).thenReturn(user);

        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("newUser"))
                .andExpect(jsonPath("$.user_id").value("userId"));
    }

    @Test
    public void signUp_ValidationError_BlankName() throws Exception {
        SignUpRequest request = new SignUpRequest("", "password123");

        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists());
        
        verifyNoInteractions(userService);
    }

    @Test
    public void login_Success() throws Exception {
        LoginRequest request = new LoginRequest("newUser", "password123");
        User user = new User("newUser", "password123", "hashedPass", new ArrayList<>(), "userId");

        when(userService.login("newUser", "password123")).thenReturn(user);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("newUser"));
    }
}
