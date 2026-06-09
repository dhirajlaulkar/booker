package booker.controllers;

import booker.entities.Ticket;
import booker.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void getBookings_Success() throws Exception {
        String username = "testUser";
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket("ticket1", username, "StationA", "StationB", "2026-06-09", null));

        when(userService.getBookings(username)).thenReturn(tickets);

        mockMvc.perform(get("/api/v1/users/{username}/bookings", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticketId").value("ticket1"));
    }

    @Test
    public void cancelBooking_Success() throws Exception {
        String username = "testUser";
        String ticketId = "ticket1";

        when(userService.cancelBooking(username, ticketId)).thenReturn(true);

        mockMvc.perform(post("/api/v1/users/{username}/bookings/{ticketId}/cancel", username, ticketId))
                .andExpect(status().isOk())
                .andExpect(content().string("Ticket successfully cancelled"));

        verify(userService, times(1)).cancelBooking(username, ticketId);
    }
}
