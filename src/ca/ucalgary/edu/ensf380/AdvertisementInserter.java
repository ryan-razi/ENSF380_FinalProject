package ca.ucalgary.edu.ensf380;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdvertisementInserter {
	
    public static void main(String[] args) {
    	
    	// put name of ad, datatype extension of ad (gif, jpg/jpeg), path to ad
        insertMedia("Disney", "jpg", "\\path");
    }
	
    public static void insertMedia(String name, String mediaType, String filePath) {
    	
        String url = "jdbc:mysql://localhost:3306/advertisements";
        String user = ""; // put user here
        String password = ""; // put password here

        String query = "INSERT INTO ads (name, type, data) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query);
             FileInputStream inputStream = new FileInputStream(filePath)) {

            statement.setString(1, name);
            statement.setString(2, mediaType);
            statement.setBlob(3, inputStream);

            statement.executeUpdate();
            
            System.out.println("Advertisment added successfully");
            
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        
        
    }

}

