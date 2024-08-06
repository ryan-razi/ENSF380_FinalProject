package ca.ucalgary.edu.ensf380;

import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrainTest {

    private Train train;
    private List<Station> stations;

    @BeforeEach
    public void setUp() {
        // Create a list of stations for testing
        stations = new ArrayList<>();
        stations.add(new Station("R", 1, "R1", "Station One", 1, 2, null));
        stations.add(new Station("R", 2, "R2", "Station Two", 3, 4, null));
        stations.add(new Station("R", 3, "R3", "Station Three", 5, 6, null));

        // Initialize the Train object
        train = new Train("T1", stations);
    }

    @Test
    public void testSetDirection() {
        train.setDirection('F');
        assertEquals('F', train.getDirection());

        train.setDirection('B');
        assertEquals('B', train.getDirection());
    }

    @Test
    public void testSetInvalidDirection() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            train.setDirection('X');
        });
    }

    @Test
    public void testUpdateTrainData() {
        train.updateTrainData("R1", "F");
        assertEquals("R", train.getLine());
        assertEquals("Station One", train.getCurrentStation().getStationName());
        assertEquals('F', train.getDirection());
    }

    @Test
    public void testUpdateCurrentStation() {
        Station newStation = stations.get(1); // Station Two
        train.updateCurrentStation(newStation);
        assertEquals("Station Two", train.getCurrentStation().getStationName());
    }

    @Test
    public void testUpdateCurrentStationInvalid() {
        Station invalidStation = new Station("Line 1", 4, "S4", "Station Four", 34.3, -118.3, null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            train.updateCurrentStation(invalidStation);
        });
        assertEquals("The provided station is not on this train's line", exception.getMessage());
    }

    @Test
    public void testGetNextStops() {
        train.updateTrainData("R1", "F");
        List<Station> nextStops = train.getNextStops();
        assertEquals(2, nextStops.size());
        assertEquals("Station Two", nextStops.get(0).getStationName());
        assertEquals("Station Three", nextStops.get(1).getStationName());
    }

    @Test
    public void testGetPrevStop() {
        train.updateTrainData("R2", "F");
        Station prevStop = train.getPrevStop();
        assertEquals("Station One", prevStop.getStationName());
    }

    @Test
    public void testGetPrevStopWhenAtFirstStation() {
        train.updateTrainData("R1", "F");
        Station prevStop = train.getPrevStop();
        assertNull(prevStop);
    }

}