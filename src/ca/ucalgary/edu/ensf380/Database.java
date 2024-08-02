package ca.ucalgary.edu.ensf380;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Database class that we use in order to establish a connection to our database so we can retrieve the advertisements.
 * Uses the MySQL database.
 * @author Ryan Razi
 * @version 1.0
 * @since 1.0
 */
public class Database {
    private final String user;
    private final String password;
    private final String DATABASE = "jdbc:mysql://localhost:3306/advertisements";

    public Database(String user, String password) {
        this.user = user;
        this.password = password;
    }

    /**
     * Retrieves all advertisements from the MySQL database.
     * @return an ArrayList of advertisements
     */
    public ArrayList<Advertisement> retrieveAllAdvertisements() {
        // create an arraylist of advertisements
        ArrayList<Advertisement> advertisements = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DATABASE, user, password)) {
            // sql query to get all the data from our database
            String query = "SELECT name, type, data FROM Ads";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                // get all of the data from the result set
                while (resultSet.next()) {
                    // create video and image advertisements based on the "type" field that it got
                    // back from the database
                    String name = resultSet.getString("name");
                    String type = resultSet.getString("type");
                    byte[] data = resultSet.getBytes("data");

                    if (type.equalsIgnoreCase("jpeg")) {
                        advertisements.add(new ImageAdvertisement(name, type, data));
                    } else if (type.equalsIgnoreCase("gif")) {
                        advertisements.add(new VideoAdvertisement(name, type, data, 10.0));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return advertisements;
    }
}
