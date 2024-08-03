package ca.ucalgary.edu.ensf380;

import java.util.List;

public class TrainPosition {
    private int trainId;
    private String line;
    private String direction;
    private String currentStation;
    private List<String> pastStations;
    private List<String> futureStations;

    public TrainPosition(int trainId, String line, String direction, String currentStation, List<String> pastStations, List<String> futureStations) {
        this.trainId = trainId;
        this.line = line;
        this.direction = direction;
        this.currentStation = currentStation;
        this.pastStations = pastStations;
        this.futureStations = futureStations;
    }

    public int getTrainId() {
        return trainId;
    }

    public String getLine() {
        return line;
    }

    public String getDirection() {
        return direction;
    }

    public String getCurrentStation() {
        return currentStation;
    }

    public List<String> getPastStations() {
        return pastStations;
    }

    public List<String> getFutureStations() {
        return futureStations;
    }

    public void updatePosition(String currentStation, List<String> pastStations, List<String> futureStations) {
        this.currentStation = currentStation;
        this.pastStations = pastStations;
        this.futureStations = futureStations;
    }

    @Override
    public String toString() {
        return "TrainPosition{" +
                "trainId=" + trainId +
                ", line='" + line + '\'' +
                ", direction='" + direction + '\'' +
                ", currentStation='" + currentStation + '\'' +
                ", pastStations=" + pastStations +
                ", futureStations=" + futureStations +
                '}';
    }
}
