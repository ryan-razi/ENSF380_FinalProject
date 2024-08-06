package ca.ucalgary.edu.ensf380;

import java.util.List;
import java.util.ArrayList;

/**
 * @author JindJeet Cheema
 * @version 1.0
 * @since 1.0
 */

public class Train {
    private String line;
    private char direction; // 'F' for forward, 'B' for backward
    private final List<Station> stations;
    private Station currentStation;
    private String name;

    public Train(String name, List<Station> stations) {
        this.stations = new ArrayList<>(stations);
        this.currentStation = null;
        this.name = name;
    }

    public void setDirection(char direction) {
        if (direction != 'F' && direction != 'B') {
            throw new IllegalArgumentException("Direction must be either 'F' (forward) or 'B' (backward)");
        }
        this.direction = direction;
    }

    public void updateTrainData(String stationCode, String direction) {
        line = stationCode.substring(0, 1);
        Station targetStation = stations.stream().filter(station -> station.getStationCode().equals(stationCode)).findFirst().orElse(null);
        assert targetStation != null;
        currentStation = targetStation;
        setDirection(direction.charAt(0));
    }

    public void updateCurrentStation(Station station) {
        if (!stations.contains(station)) {
            throw new IllegalArgumentException("The provided station is not on this train's line");
        }
        this.currentStation = station;
    }

    public List<Station> getNextStops() {
        // returns all the next stops. Caller can crop the list if needed.
        if (currentStation == null) {
            return new ArrayList<>(stations);
        }

        int currentIndex = stations.indexOf(currentStation);
        List<Station> nextStops = new ArrayList<>();

        if (direction == 'F') {
            for (int i = currentIndex + 1; i < stations.size(); i++) {
                nextStops.add(stations.get(i));
            }
        } else { // direction == 'B'
            for (int i = currentIndex - 1; i >= 0; i--) {
                nextStops.add(stations.get(i));
            }
        }

        return nextStops;
    }

    public Station getPrevStop() {
        if (currentStation == null) {
            return null;
        }

        int currentIndex = stations.indexOf(currentStation);

        if (direction == 'F') {
            return (currentIndex > 0) ? stations.get(currentIndex - 1) : null;
        } else { // direction == 'B'
            return (currentIndex < stations.size() - 1) ? stations.get(currentIndex + 1) : null;
        }
    }

    // Getters
    public String getLine() { return line; }
    public char getDirection() { return direction; }
    public Station getCurrentStation() { return currentStation; }

    @Override
    public String toString() {
        return "Train{" +
               "line='" + line + '\'' +
               ", direction=" + direction +
               ", currentStation=" + (currentStation != null ? currentStation.getStationName() : "null") +
               '}';
    }
}