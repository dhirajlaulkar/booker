package booker.services;

import booker.entities.Train;
import booker.exceptions.ResourceNotFoundException;
import booker.repositories.TrainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainServiceTest {

    @Mock
    private TrainRepository trainRepository;

    @InjectMocks
    private TrainService trainService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getTrainById_Success() {
        Train train = new Train("train1", "123", new ArrayList<>(), new HashMap<>(), new ArrayList<>());
        when(trainRepository.findById("train1")).thenReturn(Optional.of(train));

        Train result = trainService.getTrainById("train1");

        assertNotNull(result);
        assertEquals("train1", result.getTrainId());
    }

    @Test
    public void getTrainById_NotFound_ThrowsResourceNotFound() {
        when(trainRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> trainService.getTrainById("nonexistent"));
    }

    @Test
    public void searchTrains_Success() {
        Map<String, String> stations = new LinkedHashMap<>();
        stations.put("StationA", "10:00");
        stations.put("StationB", "11:00");
        stations.put("StationC", "12:00");

        Train train1 = new Train("train1", "123", new ArrayList<>(), stations, new ArrayList<>(stations.keySet()));
        
        Map<String, String> stations2 = new LinkedHashMap<>();
        stations2.put("StationB", "13:00");
        stations2.put("StationC", "14:00");
        Train train2 = new Train("train2", "456", new ArrayList<>(), stations2, new ArrayList<>(stations2.keySet()));

        List<Train> allTrains = Arrays.asList(train1, train2);
        when(trainRepository.findAll()).thenReturn(allTrains);

        List<Train> result = trainService.searchTrains("StationA", "StationC");
        assertEquals(1, result.size());
        assertEquals("train1", result.get(0).getTrainId());

        List<Train> resultBoth = trainService.searchTrains("StationB", "StationC");
        assertEquals(2, resultBoth.size());

        List<Train> resultNone = trainService.searchTrains("StationC", "StationA");
        assertEquals(0, resultNone.size());
    }

    @Test
    public void addTrain_Success() {
        Train train = new Train("train1", "123", new ArrayList<>(), new HashMap<>(), new ArrayList<>());
        when(trainRepository.save(train)).thenReturn(train);

        Train result = trainService.addTrain(train);

        assertNotNull(result);
        verify(trainRepository, times(1)).save(train);
    }
}
