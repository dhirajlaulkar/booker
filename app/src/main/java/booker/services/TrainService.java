package booker.services;

import booker.entities.Train;
import java.util.List;
import java.util.stream.Collectors;

public class TrainService {

    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream().filter(train -> validateTrain(train, source, destination))
                .collect(Collectors.toList());
    }

    private boolean validateTrain(Train train, String source, String destination) {
        List<String> stations = train.getStations();
        int sourceIndex = stations.indexOf(source.toLowerCase());
        int destinationIndex = stations.indexOf(destination.toLowerCase());
        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }
}
