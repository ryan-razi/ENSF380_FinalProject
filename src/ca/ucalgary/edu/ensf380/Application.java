package ca.ucalgary.edu.ensf380;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

/**
 * Main application used to display the subway screen to the user.
 * Uses JFrame in order to create a GUI for us to see everything.
 * @author Ryan Razi
 * @version 1.0
 * @since 1.0
 */

public class Application extends JFrame {

	private News news;
	private Weather weather;
	private ArrayList<Advertisement> ads;

	private JPanel advertisementPanel;
	private JPanel weatherPanel;
	private JPanel newsPanel;
	private JPanel trainStationPanel;
	
	/**
	 * Our main method. Used to create a JFrame that displays a SubwayScreen.
	 * @param args, the command line arguements
	 * @throws SQLException when we can't access our database
	 **/
	public static void main(String[] args) throws SQLException {

		// gets all the arguements so we can use them to create an instance of our
		// application
		String username = args[0];
		String password = args[1];
		String newsTopic = args[2];
		String cityCode = args[3];

		// connect to the database to get our ads
		Database db = new Database(username, password);
		ArrayList<Advertisement> ads = db.retrieveAllAdvertisements();

		// create an instance of our application
		Application app = new Application(newsTopic, cityCode, ads);

		// set our application to be visibile
		app.setVisible(true);

	}

	public Application(String newsTopic,  String cityCode, ArrayList<Advertisement> ads) {

		this.news = new News(newsTopic);
		this.ads = ads;
		this.weather = new Weather(cityCode);

		// make the 4 panels for each section
		this.advertisementPanel = createAdvertisementPanel(ads);
		this.weatherPanel = createWeatherPanel(weather);
		this.newsPanel = createNewsPanel(news.getTitles());
		this.trainStationPanel = createTrainStationPanel();

		// set the title as SubwayScreen
		setTitle("SubwayScreen");

		// program terminate when we close the frame
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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
		gbc.weightx = 1.5;
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

	/**
     * Gets data from the train station simulator, and puts it on top of an image that displays where we currently are
     * @return a JPanel with image and text over it, that is continuously updating
     */

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

	/**
     * creates a panel that reads the ArrayList of advertisements and continuously displays all the ads in the list,
     * as well as the current location of the trains every once in a while
     * @param ads the ArrayList of Advertisements
     * @return a JPanel with images/gifs of ads continuously looping
     */

	public static JPanel createAdvertisementPanel(ArrayList<Advertisement> ads) {

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
        		index = index % ads.size();
        		byte[] data = ads.get(index).getData(); // get the data from the advertisement
        		ImageIcon imageIcon = new ImageIcon(data); // set the imageIcon with the image data
        		adLabel.setIcon(imageIcon); // add the image icon to the label
                index++;
            }
        });

        // start the timer
        timer.start();

        return panel;
    }

	/**
     * creates a weather panel that displays the current temperature of the city, as well as the current time
     * @param weather, contains the city code and current weather of that city in °C
     * @return a JPanel that contains the current weather of the city in °C, as well as current time
     */

	// might add little icons to display how hot or cold it is
	public static JPanel createWeatherPanel(Weather weather) {

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

		// create a label for displaying the weather
		JLabel tempLabel = new JLabel(weather.getTemp());
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

	/**
     * creates a weather panel that continuously scrolls with text of news relating to a certain topic
     * @param titles, a String array of news titles relating to a topic generated from an instance of the news class
     * @return a JPanel that continuously scrolls right to left displaying new titles of a topic
     */
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

	public ArrayList<Advertisement> getAds() {
		return ads;
	}

	public void setAds(ArrayList<Advertisement> ads) {
		this.ads = ads;
	}

	public JPanel getAdvertisementPanel() {
		return advertisementPanel;
	}

	public void setAdvertisementPanel(JPanel advertisementPanel) {
		this.advertisementPanel = advertisementPanel;
	}

	public JPanel getWeatherPanel() {
		return weatherPanel;
	}

	public void setWeatherPanel(JPanel weatherPanel) {
		this.weatherPanel = weatherPanel;
	}

	public JPanel getNewsPanel() {
		return newsPanel;
	}

	public void setNewsPanel(JPanel newsPanel) {
		this.newsPanel = newsPanel;
	}

	public JPanel getTrainStationPanel() {
		return trainStationPanel;
	}

	public void setTrainStationPanel(JPanel trainStationPanel) {
		this.trainStationPanel = trainStationPanel;
	}



}
