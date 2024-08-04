package ca.ucalgary.edu.ensf380;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CSVReaderTest {

    @Test
    public void testReadStations() {
        String csvFile = "path/to/your/test/subway.csv";
        List<Station> stations = CSVReader.readStations(csvFile);

        assertNotNull(stations);
        assertFalse(stations.isEmpty());

        // Check the first station
        Station firstStation = stations.get(0);
        assertEquals(1, firstStation.getRow());
        assertEquals("R", firstStation.getLine());
        assertEquals(1, firstStation.getStationNumber());
        assertEquals("R01", firstStation.getStationCode());
        assertEquals("Maplewood Station", firstStation.getStationName());
        assertEquals(8.756969, firstStation.getX());
        assertEquals(14.790169, firstStation.getY());
        assertEquals("", firstStation.getCommonStations());
    }
}
