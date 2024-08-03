package ca.ucalgary.edu.ensf380;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public static List<Station> readStations(String csvFile) {
        List<Station> stations = new ArrayList<>();
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] stationData = line.split(csvSplitBy);
                int row = Integer.parseInt(stationData[0]);
                String lineId = stationData[1];
                int stationNumber = Integer.parseInt(stationData[2]);
                String stationCode = stationData[3];
                String stationName = stationData[4];
                double x = Double.parseDouble(stationData[5]);
                double y = Double.parseDouble(stationData[6]);
                String commonStations = stationData.length > 7 ? stationData[7] : "";

                stations.add(new Station(row, lineId, stationNumber, stationCode, stationName, x, y, commonStations));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stations;
    }
}
