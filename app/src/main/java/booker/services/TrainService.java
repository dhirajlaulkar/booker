package booker.services;

import booker.entities.Train;
import booker.exceptions.ResourceNotFoundException;
import booker.repositories.TrainRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainService {

    private final TrainRepository trainRepository;

    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public List<Train> searchTrains(String source, String destination) {
        return trainRepository.findAll().stream()
                .filter(train -> validTrain(train, source, destination))
                .collect(Collectors.toList());
    }

    public Train getTrainById(String trainId) {
        return trainRepository.findById(trainId)
                .orElseThrow(() -> new ResourceNotFoundException("Train with ID '" + trainId + "' not found"));
    }

    public Train addTrain(Train newTrain) {
        return trainRepository.save(newTrain);
    }

    public Train updateTrain(Train updatedTrain) {
        return trainRepository.save(updatedTrain);
    }

    private boolean validTrain(Train train, String source, String destination) {
        List<String> stations = train.getStations();
        if (stations == null) {
            return false;
        }
        int sourceIndex = -1;
        int destinationIndex = -1;

        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).equalsIgnoreCase(source)) {
                sourceIndex = i;
            }
            if (stations.get(i).equalsIgnoreCase(destination)) {
                destinationIndex = i;
            }
        }

        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }
}
