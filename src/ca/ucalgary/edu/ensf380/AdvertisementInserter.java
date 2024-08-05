package ca.ucalgary.edu.ensf380;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * AdvertisementInserter class used to help user insert ads to the MySQL database
 * @author Ryan Razi
 * @version 1.0
 * @since 1.0
 */

public class AdvertisementInserter {

    public static void main(String[] args) {

    	// put name of ad, filetype extension of ad (gif, jpg/jpeg), path to ad
        insertAd("Nike", "jpg", "C:\\Users\\ryanr\\OneDrive\\Desktop\\JavaProjects\\ENSF380_FinalProject\\Advertisements\\Nike.jpg");
    }
    
    /**
     * Gets data from the user and ads it to the database
     * @params name of ad, filetype extension of ad, filepath to the ad
     */
    public static void insertAd(String name, String fileType, String filePath) {

        String url = "jdbc:mysql://localhost:3306/advertisements";
        String user = ""; // put user here
        String password = ""; // put password here

        String query = "INSERT INTO ads (name, type, data) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query);
             FileInputStream inputStream = new FileInputStream(filePath)) {

            statement.setString(1, name);
            statement.setString(2, fileType);
            statement.setBlob(3, inputStream);

            statement.executeUpdate();

            System.out.println("Advertisment added successfully");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }


    }

}

