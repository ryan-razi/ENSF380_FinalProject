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
    private final String USER;
    private final String PASSWORD;
    private final String DATABASE = "jdbc:mysql://localhost:3306/advertisements";

    public Database(String user, String password) {
        this.USER = user;
        this.PASSWORD = password;
    }

    /**
     * Retrieves all advertisements from the MySQL database.
     * @return an ArrayList of advertisements
     * @throws SQLException 
     */
    public ArrayList<Advertisement> retrieveAllAdvertisements() throws SQLException {
        // create an arraylist of advertisements
        ArrayList<Advertisement> advertisements = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
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

                    if (type.equalsIgnoreCase("jpeg") || type.equalsIgnoreCase("jpg")) {
                        advertisements.add(new ImageAdvertisement(name, type, data));
                    } else if (type.equalsIgnoreCase("gif")) {
                        advertisements.add(new VideoAdvertisement(name, type, data, 10.0));
                    }
                }
            }
        } catch (SQLException e) {
        	// have to rethrow e because it makes my test fail if it doesn't for some reason
        	throw e;
        }

        return advertisements;
    }

	public String getUSER() {
		return USER;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public String getDATABASE() {
		return DATABASE;
	}
    
    
}
