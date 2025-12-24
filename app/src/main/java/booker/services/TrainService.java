package booker.services;

import booker.entities.Train;
import java.util.List;
import java.util.stream.Collectors;

public class TrainService {

    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream().filter(train -> validateTrain(train, source, destination))
                .collect(Collectors.toList());
    }

}
