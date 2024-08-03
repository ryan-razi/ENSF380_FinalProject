package ca.ucalgary.edu.ensf380;

public class Station {
    private int row;
    private String line;
    private int stationNumber;
    private String stationCode;
    private String stationName;
    private double x;
    private double y;
    private String commonStations;

    public Station(int row, String line, int stationNumber, String stationCode, String stationName, double x, double y, String commonStations) {
        this.row = row;
        this.line = line;
        this.stationNumber = stationNumber;
        this.stationCode = stationCode;
        this.stationName = stationName;
        this.x = x;
        this.y = y;
        this.commonStations = commonStations;
    }

    public int getRow() {
        return row;
    }

    public String getLine() {
        return line;
    }

    public int getStationNumber() {
        return stationNumber;
    }

    public String getStationCode() {
        return stationCode;
    }

    public String getStationName() {
        return stationName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getCommonStations() {
        return commonStations;
    }

    @Override
    public String toString() {
        return "Station{" +
                "row=" + row +
                ", line='" + line + '\'' +
                ", stationNumber=" + stationNumber +
                ", stationCode='" + stationCode + '\'' +
                ", stationName='" + stationName + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", commonStations='" + commonStations + '\'' +
                '}';
    }
}
