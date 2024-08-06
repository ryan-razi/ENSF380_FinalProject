package ca.ucalgary.edu.ensf380;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * @author JindJeet Cheema
 * @version 1.0
 * @since 1.0
 */


public class TrainMapPlotter {
    public static List<String[]> readCsv(String filePath) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static void plotAndSavePNG(List<String[]> csvData, HashMap<String,String> trainData, int width, int height, String outputPath)
            throws IOException {

        // reverse look up map for station code to train name
        Map<String, String> reverseTrainData = trainData.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Set white background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Variables to hold the previous point for line drawing
        int previousX = -1;
        int previousY = -1;
        String previousColor = "";

        // Plot lines
        for (String[] row : csvData) {
            if (row.length >= 7) {
                Color color = getColor(row[1]);
                int x = (int) Double.parseDouble(row[5]);
                int y = (int) Double.parseDouble(row[6]); // Invert y-axis if necessary

                // Invert Y-axis if the origin is at the top-left
                // Uncomment the following line if the origin is at the bottom-left
                // y = height - y;

                // Draw line to the previous point if it exists
                if (previousX != -1 && previousY != -1 && previousColor.equals(row[1])) {
                    g2d.setColor(color); // Set color for the line
                    g2d.drawLine(previousX, previousY, x, y); // Draw line between points
                }
                else {
                    previousColor = row[1];
                }

                // Draw dots/circles for locations of trains matching with station code
                if (reverseTrainData.containsKey(row[3])) {
                    String trainName = reverseTrainData.get(row[3]);
                    g2d.setColor(color); // Set color for the dot
                    g2d.fillOval(x - 5, y - 5, 10, 10); // Draw a circle at the point
                    g2d.drawString(trainName, x - 10, y - 10); // Label the train
                }

                // Update previous point
                previousX = x;
                previousY = y;
            }
        }

        g2d.dispose();

        // Save as PNG
        File outputFile = new File(outputPath);
        ImageIO.write(image, "png", outputFile);
    }

    private static Color getColor(String colorCode) {
        switch (colorCode) {
            case "R":
                return Color.RED;
            case "G":
                return Color.GREEN;
            case "B":
                return Color.BLUE;
            default:
                return Color.BLACK;
        }
    }

    public static void main(String[] args) {
        HashMap<String, String> trainData = new HashMap<>();
        trainData.put("T4", "R41");
        trainData.put("T5", "B16");
        trainData.put("T6", "B21");
        trainData.put("T10", "G11");
        trainData.put("T7", "B38");
        trainData.put("T8", "B42");
        trainData.put("T12", "G31");
        trainData.put("T9", "G06");
        trainData.put("T11", "G27");
        trainData.put("T1", "R15");
        trainData.put("T2", "R20");
        trainData.put("T3", "R37");


        String filePath = "C:\\Users\\jindjeet singh\\Desktop\\ENSF_380\\2nd\\Final_Project\\ENSF380_FinalProject - Copy\\data\\subway.csv";
        List<String[]> csvData = readCsv(filePath);

        try {
            plotAndSavePNG(csvData, trainData, 1200, 700, "output_graph.png");
            System.out.println("Graph saved as output_graph.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}