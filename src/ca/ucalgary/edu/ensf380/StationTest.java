package ca.ucalgary.edu.ensf380;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class StationTest {

    @Test
    public void testStationCreation() {
        Station station = new Station(1, "R", 1, "R01", "Maplewood Station", 8.756969, 14.790169, "");

        assertEquals(1, station.getRow());
        assertEquals("R", station.getLine());
        assertEquals(1, station.getStationNumber());
        assertEquals("R01", station.getStationCode());
        assertEquals("Maplewood Station", station.getStationName());
        assertEquals(8.756969, station.getX());
        assertEquals(14.790169, station.getY());
        assertEquals("", station.getCommonStations());
    }

    @Test
    public void testToString() {
        Station station = new Station(1, "R", 1, "R01", "Maplewood Station", 8.756969, 14.790169, "");
        String expected = "Station{row=1, line='R', stationNumber=1, stationCode='R01', stationName='Maplewood Station', x=8.756969, y=14.790169, commonStations=''}";
        assertEquals(expected, station.toString());
    }
}
