package ca.ucalgary.edu.ensf380;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Application extends JFrame {

	private News news;
	private Weather weather;
	private ArrayList<Advertisement> ads;

	private JPanel advertisementPanel;
	private JPanel weatherPanel;
	private JPanel newsPanel;
	private JPanel trainStationPanel;

	private final String username;
	private final String password;
	private static final String DATABASE = "jdbc:mysql://localhost:3306/advertisements";

	public static void main(String[] args) {

		// gets all the arguements so we can use them to create an instance of our
		// application
		String username = args[0];
		String password = args[1];
		String newsTopic = args[2];
		String cityCode = args[3];

		// create an instance of our application
		Application app = new Application(newsTopic, username, password, cityCode);

		// set our application to be visibile
		app.setVisible(true);

	}

	public Application(String newsTopic, final String username, final String password, String cityCode) {

		this.news = new News(newsTopic);
		this.username = username;
		this.password = password;
		this.ads = retrieveAllAdvertisements(username, password);
		// this.weather = new Weather(cityCode);

		// make the 4 panels for each section
		this.advertisementPanel = createAdvertisementPanel(ads);
		this.weatherPanel = createWeatherPanel();
		this.newsPanel = createNewsPanel(news.getTitles());
		this.trainStationPanel = createTrainStationPanel();

		// set the title as SubwayScreen
		setTitle("SubwayScreen");

		// program terminate when we close the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// size of the screen
		setSize(945, 900);

		// gbc is created in order to help us deal with the layout and sizing of all
		// these panels
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// create the first panel
		gbc.gridx = 0; // grid.x is column number
		gbc.gridy = 0; // grid.y is row number
		gbc.weightx = 0.7; // weightx is how much horizontal space it should take up
		gbc.weighty = 0.3; // weighty is how much vertical space it should take up
		gbc.fill = GridBagConstraints.BOTH; // fills in the panel both vertically and horizontally
		add(advertisementPanel, gbc); // add the panel to the frame

		// create the second panel
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.1;
		gbc.weighty = 0.3;
		gbc.fill = GridBagConstraints.BOTH;
		add(weatherPanel, gbc);

		// create the third panel
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2; // gridwidth is for how many columns the panel should span across
		gbc.weightx = 1.0;
		gbc.weighty = 0.05;
		gbc.fill = GridBagConstraints.BOTH;
		add(newsPanel, gbc);

		// create the fourth pane;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.weightx = 1.0;
		gbc.weighty = 1.2;
		gbc.fill = GridBagConstraints.BOTH;
		add(trainStationPanel, gbc);

	}

	public static JPanel createTrainStationPanel() {

		// create the panel
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.BLACK);

		// create the layeredPane so we can have text and image at same time
		JLayeredPane layeredPane = new JLayeredPane();

		// get our image and turn it into a JLabel
		ImageIcon imageIcon = new ImageIcon("Train.jpg");
		JLabel imageLabel = new JLabel(imageIcon);
		imageLabel.setBounds(0, 0, 945, 225); // sets the location of where our image is and the dimensions of our label

		// create our text to showcase where the trains are
		JLabel textLabel = new JLabel("Temporary Text");
		textLabel.setForeground(Color.WHITE);
		textLabel.setFont(new Font("Arial", Font.BOLD, 24));
		textLabel.setBounds(20, 175, 300, 30); // sets the location of where the text is and the dimensions of our label

		// add the text and image to our layeredPane
		layeredPane.add(imageLabel, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(textLabel, JLayeredPane.PALETTE_LAYER);

		// add the layeredPane to our panel
		panel.add(layeredPane);

		return panel;
	}

	public static JPanel createAdvertisementPanel(List<Advertisement> advertisements) {
		
		// create new panel
        JPanel panel = new JPanel(new BorderLayout());
        
        // create the label for our ads
        JLabel adLabel = new JLabel();
        panel.add(adLabel);

        // timer to switch between advertisements every 10 seconds
		
        Timer timer = new Timer(1000, new ActionListener() {
        	// start from the second ad
        	int index = 0;
        	// continuously loop through the advertisements and continuously show them
        	@Override
            public void actionPerformed(ActionEvent e) {
        		index = index % advertisements.size();
        		byte[] data = advertisements.get(index).getData(); // get the data from the advertisement
        		ImageIcon imageIcon = new ImageIcon(data); // set the imageIcon with the image data
        		adLabel.setIcon(imageIcon); // add the image icon to the label
                index++;
            }
        });
        
        // start the timer
        timer.start();

        return panel;
    }
	
	public static JPanel createWeatherPanel() {

		// create the panel
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		
		// gbc created so that the text does not overlap one another
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		// create a label for displaying the time
		JLabel timeLabel = new JLabel();
		timeLabel.setForeground(Color.BLACK);
		Font font = new Font("Arial", Font.BOLD, 24);
		timeLabel.setFont(font);
		
		// create a label for displaying the weather // temporarily just uses 20 as default
		JLabel tempLabel = new JLabel("Current Temperature: 20°C");
		tempLabel.setForeground(Color.BLACK);
		tempLabel.setFont(font);
		
		// add the time and temperature label
		panel.add(timeLabel, gbc);
		gbc.gridy--; // put the temperature below the time
		panel.add(tempLabel, gbc);
		
		// timer to update the time every second
		Timer timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LocalDateTime currentTime = LocalDateTime.now(); // get the current time
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a"); // formats the time in something like 2:37 PM
				timeLabel.setText(currentTime.format(formatter)); // add the text to the label
			}
		});
		// start the timer
		timer.start();

		return panel;
	}
	
	
	public static JPanel createNewsPanel(String[] titles) {
		
		// create the panel
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.BLUE);
		
		// create a string builder to hold all our news title text with a big gap between them
		StringBuilder sb = new StringBuilder();
		for (String title : titles) {
			sb.append(title).append("     ");
		}
		
		// change back into string so we can display it
		String scrollingText = sb.toString();
		
		// create a label
		JLabel label = new JLabel(scrollingText);
		label.setForeground(Color.WHITE);
		
		// create font for the label
		Font largerFont = new Font("Arial", Font.PLAIN, 20);
		label.setFont(largerFont);
		
		// add the label to the panel
		panel.add(label, BorderLayout.CENTER);
		
		// timer to get the text to scroll left from right
		Timer timer = new Timer(20, new ActionListener() {
			
			int position = panel.getWidth();

			@Override
			public void actionPerformed(ActionEvent e) {
				
				position -= 2;
				
				// reset the position if we scroll for too long 
				if (position + label.getPreferredSize().getWidth() < 0) {
					position = panel.getWidth();
				}
				
				// continuously update the position of our text with the new position
				label.setBounds(position, 0, (int) label.getPreferredSize().getWidth(),
						(int) label.getPreferredSize().getHeight()); 
			}
		});
		
		// start the timer
		timer.start();

		return panel;
	}

	public static ArrayList<Advertisement> retrieveAllAdvertisements(final String user, final String password) {
		
		// create an arraylist of advertisements
		ArrayList<Advertisement> advertisements = new ArrayList<>();

		try {
			// create a connection to our database
			Connection connection = DriverManager.getConnection(DATABASE, user, password);

			// sql query to get all the data from our database
			String query = "SELECT name, type, data FROM Ads";
			PreparedStatement preparedStatement = connection.prepareStatement(query);

			// create a result set from what we got back from the query
			ResultSet resultSet = preparedStatement.executeQuery();

			// get all of the data from the result set
			while (resultSet.next()) {
				
				// create video and image advertisements based on the "type" field that it got back from the database
				String name = resultSet.getString("name");
				String type = resultSet.getString("type");
				byte[] data = resultSet.getBytes("data");
				
				if (type.equalsIgnoreCase("jpeg")) {
					advertisements.add(new ImageAdvertisement(name, type, data));
				} else if (type.equalsIgnoreCase("gif")) {
					advertisements.add(new VideoAdvertisement(name, type, data, 10.0));
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 

		return advertisements;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public Weather getWeather() {
		return weather;
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
	}

}
