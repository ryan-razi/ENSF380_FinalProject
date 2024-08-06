package ca.ucalgary.edu.ensf380;

/**
 * Represents a station with its code, name, coordinates, line code, and common stations.
 * @author JindJeet Cheema
 * @version 1.0
 * @since 1.0
 */
 
public class Station {
    private final String stationCode;
    private final int stationNumber;
    private final String stationName;
    private final double x;
    private final double y;
    private final String lineCode;
    private final String commonStations;

    public Station(String lineCode, int stationNumber, String stationCode, String stationName, double x, double y, String commonStations) {
        this.stationCode = stationCode;
        this.stationNumber = stationNumber;
        this.stationName = stationName;
        this.x = x;
        this.y = y;
        this.lineCode = lineCode;
        this.commonStations = commonStations;
    }

    // Getters for the station properties
    public String getStationCode() {
        return stationCode;
    }

    public int getStationNumber() {
        return stationNumber;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getLineCode() {
        return lineCode;
    }

    public String getCommonStations() {
        return commonStations;
    }

    public String getStationName() {
        return stationName;
    }

    public String toString() {
        return "Station{" +
                "stationCode='" + stationCode + '\'' +
                ", stationNumber=" + stationNumber +
                ", stationName='" + stationName + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", lineCode='" + lineCode + '\'' +
                ", commonStations='" + commonStations + '\'' +
                '}';
    }
}
