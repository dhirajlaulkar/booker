package booker.controllers;

import booker.dtos.BookSeatRequest;
import booker.entities.Ticket;
import booker.entities.Train;
import booker.services.TrainService;
import booker.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainController.class)
public class TrainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainService trainService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void searchTrains_Success() throws Exception {
        Train train = new Train("train1", "123", new ArrayList<>(), new HashMap<>(), new ArrayList<>());
        List<Train> trains = Collections.singletonList(train);

        when(trainService.searchTrains("StationA", "StationB")).thenReturn(trains);

        mockMvc.perform(get("/api/v1/trains/search")
                .param("source", "StationA")
                .param("destination", "StationB"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].train_id").value("train1"));
    }

    @Test
    public void getTrainById_Success() throws Exception {
        Train train = new Train("train1", "123", new ArrayList<>(), new HashMap<>(), new ArrayList<>());

        when(trainService.getTrainById("train1")).thenReturn(train);

        mockMvc.perform(get("/api/v1/trains/{trainId}", "train1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.train_id").value("train1"));
    }

    @Test
    public void bookSeat_Success() throws Exception {
        BookSeatRequest request = new BookSeatRequest("testUser", 0, 1, "StationA", "StationB", "2026-06-09");
        Ticket ticket = new Ticket("ticket1", "testUser", "StationA", "StationB", "2026-06-09", null);

        when(userService.bookSeat(eq("train1"), any(BookSeatRequest.class))).thenReturn(ticket);

        mockMvc.perform(post("/api/v1/trains/{trainId}/book", "train1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticketId").value("ticket1"));
    }

    @Test
    public void bookSeat_ValidationError_NullRow() throws Exception {
        BookSeatRequest request = new BookSeatRequest("testUser", null, 1, "StationA", "StationB", "2026-06-09");

        mockMvc.perform(post("/api/v1/trains/{trainId}/book", "train1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.row").exists());

        verifyNoInteractions(userService);
    }
}
