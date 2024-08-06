package ca.ucalgary.edu.ensf380;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author JindJeet Cheema
 * @version 1.0
 * @since 1.0
 */

public class CSVReaderTest {

    private static final String TEST_CSV_FILE = "test_stations.csv";

    @BeforeEach
    public void setUp() throws IOException {
        // Create a temporary CSV file with test data
        String csvContent = "Row,Line,StationNumber,StationCode,StationName,X,Y,Common Stations\n1,R,01,R01, Maplewood Station,8.756969333,14.79016876,\n2,R,02,R02, Lakeview Heights Station,35.30510521,38.3885107\n3,R,03,R03, Green Hills Station,64.06567669,56.37714005";


        Files.write(Paths.get(TEST_CSV_FILE), csvContent.getBytes());
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Delete the temporary CSV file
        Files.deleteIfExists(Paths.get(TEST_CSV_FILE));
    }

    @Test
    public void testCreateStationDatabase() {
        // Execute the method to test
        List<Station> stations = CSVReader.createStationDatabase(TEST_CSV_FILE);

        // Verify the size of the returned list
        assertEquals(3, stations.size(), "Expected 3 stations to be read from the CSV.");

        // Verify the first station
        Station station1 = stations.get(0);
        assertEquals("R", station1.getLineCode());
        assertEquals(1, station1.getStationNumber());
        assertEquals("R01", station1.getStationCode());
        assertEquals(" Maplewood Station", station1.getStationName());
        assertEquals(8.7569693330, station1.getX());
        assertEquals(14.79016876, station1.getY());
        assertEquals(null, station1.getCommonStations());

    }
}