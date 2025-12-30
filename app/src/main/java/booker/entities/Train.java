package booker.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Train {

    @JsonProperty("train_id")
    private String trainId;
    @JsonProperty("train_no")
    private String trainNo;
    private List<List<Integer>> seats;
    private Map<String, String> stationTimes;
    private List<String> stations;

    public Train(String trainId, String trainNo, List<List<Integer>> seats, Map<String, String> stationTimes,
            List<String> stations) {
        this.trainId = trainId;
        this.trainNo = trainNo;
        this.seats = seats;
        this.stationTimes = stationTimes;
        this.stations = stations;
    }

    public Train() {
    }

    public Map<String, String> getStationTimes() {
        return stationTimes;
    }

    public List<String> getStations() {
        return stations;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public String getTrainId() {
        return trainId;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    @JsonProperty("stations")
    public void setStationTimes(Map<String, String> stationTimes) {
        this.stationTimes = stationTimes;
        this.stations = new ArrayList<>(stationTimes.keySet());
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }

    public String getTrainInfo() {
        return String.format("Train ID : %s Train No: %s", trainId, trainNo);
    }
}