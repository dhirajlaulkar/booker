package booker.repositories;

import booker.entities.Train;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainRepository {

    private final String trainsPath;
    private final ObjectMapper objectMapper;

    public TrainRepository(@Value("${booker.db.trains-path:app/src/main/java/booker/localDb/trains.json}") String trainsPath,
                           ObjectMapper objectMapper) {
        this.trainsPath = trainsPath;
        this.objectMapper = objectMapper;
    }

    public List<Train> findAll() {
        try {
            File file = new File(trainsPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Train>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read trains from file database", e);
        }
    }

    public Optional<Train> findById(String trainId) {
        return findAll().stream()
                .filter(train -> train.getTrainId().equalsIgnoreCase(trainId))
                .findFirst();
    }

    public synchronized Train save(Train train) {
        List<Train> trains = findAll();
        trains.removeIf(t -> t.getTrainId().equalsIgnoreCase(train.getTrainId()));
        trains.add(train);
        saveAll(trains);
        return train;
    }

    public synchronized void saveAll(List<Train> trains) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(trainsPath), trains);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write trains to file database", e);
        }
    }
}
