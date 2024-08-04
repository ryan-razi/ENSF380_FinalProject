package ca.ucalgary.edu.ensf380;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class TrainPositionTest {

    @Test
    public void testTrainPositionCreation() {
        List<String> pastStations = Arrays.asList("Station1", "Station2");
        List<String> futureStations = Arrays.asList("Station4", "Station5");
        TrainPosition trainPosition = new TrainPosition(1, "Red", "North", "Station3", pastStations, futureStations);

        assertEquals(1, trainPosition.getTrainId());
        assertEquals("Red", trainPosition.getLine());
        assertEquals("North", trainPosition.getDirection());
        assertEquals("Station3", trainPosition.getCurrentStation());
        assertEquals(pastStations, trainPosition.getPastStations());
        assertEquals(futureStations, trainPosition.getFutureStations());
    }

    @Test
    public void testUpdatePosition() {
        List<String> pastStations = Arrays.asList("Station1", "Station2");
        List<String> futureStations = Arrays.asList("Station4", "Station5");
        TrainPosition trainPosition = new TrainPosition(1, "Red", "North", "Station3", pastStations, futureStations);

        List<String> newPastStations = Arrays.asList("Station1", "Station2", "Station3");
        List<String> newFutureStations = Arrays.asList("Station5", "Station6");
        trainPosition.updatePosition("Station4", newPastStations, newFutureStations);

        assertEquals("Station4", trainPosition.getCurrentStation());
        assertEquals(newPastStations, trainPosition.getPastStations());
        assertEquals(newFutureStations, trainPosition.getFutureStations());
    }

    @Test
    public void testToString() {
        List<String> pastStations = Arrays.asList("Station1", "Station2");
        List<String> futureStations = Arrays.asList("Station4", "Station5");
        TrainPosition trainPosition = new TrainPosition(1, "Red", "North", "Station3", pastStations, futureStations);
        String expected = "TrainPosition{trainId=1, line='Red', direction='North', currentStation='Station3', pastStations=[Station1, Station2], futureStations=[Station4, Station5]}";
        assertEquals(expected, trainPosition.toString());
    }
}
