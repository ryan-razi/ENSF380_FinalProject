package ca.ucalgary.edu.ensf380;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Reads station data from a CSV file and monitors output files for train data updates.
 * @author Jindjeet Cheema
 * @version 1.0
 */
public class CSVReader {
    public static List<Station> createStationDatabase(String csvFile) {
        // String csvFile = "C:\\Users\\jindjeet singh\\Desktop\\ENSF_380\\2nd\\Final_Project\\ENSF380_FinalProject - Copy\\data\\subway.csv"; // Path to your CSV file
        String line;
        String csvSplitBy = ",";

        List<Station> stations = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Read the header line
            br.readLine();

            // Read each line of the CSV file
            while ((line = br.readLine()) != null) {
                // Use comma as separator
                String[] stationData = line.split(csvSplitBy);

                // Parse the data: +1 since first column is row number
                String trainLine = stationData[0+1];
                int stationNumber = Integer.parseInt(stationData[1+1]);
                String stationCode = stationData[2+1];
                String stationName = stationData[3+1];
                double x = Double.parseDouble(stationData[4+1]);
                double y = Double.parseDouble(stationData[5+1]);
                String commonStations = null;
                if (stationData.length >= 8) {
                    commonStations = stationData[6+1];

                }

                // Create a TrainStation object and add it to the list
                Station station = new Station(trainLine, stationNumber, stationCode, stationName, x, y, commonStations);
                stations.add(station);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // Print the list of stations
        // for (Station station : stations) {
        //     System.out.println(station);
        // }

        return stations;
    }
}