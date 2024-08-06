package ca.ucalgary.edu.ensf380;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import org.openqa.selenium.Dimension;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.util.data.audio.AudioPlayer;

import javax.sound.sampled.AudioInputStream;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;


/**
 * Main application used to display the subway screen to the user.
 * Uses JFrame in order to create a GUI for us to see everything.
 * @author Ryan Razi
 * @author Jindjeet Cheema
 * @version 1.0
 * @since 1.0
 */

public class Application extends JFrame {

	private News news;
	private Weather weather;
	private ArrayList<Advertisement> ads;
	private String myTrain;

	private JPanel advertisementPanel;
	private JPanel weatherPanel;
	private JPanel newsPanel;
	private JPanel trainStationPanel;
	private JPanel trainsMapPanel;
	private List<Station> stationDatabase;
	private Train myTrainObject;
	


	private JLabel previousStationLabel;
	private JLabel currentStationLabel;
	private JLabel nextStationLabel1;
	private JLabel nextStationLabel2;
	private JLabel nextStationLabel3;

	private JLabel mapLabel; // not necessarily used
	private ImageIcon mapImageIcon; // will be used somewhere always

	HashMap<String, String> allTrains;
	private List<String[]> csvData;
	
	/**
	 * Our main method. Used to create a JFrame that displays a SubwayScreen.
	 * @param args, the command line arguements
	 * @throws SQLException when we can't access our database
	 **/
	public static void main(String[] args) throws SQLException {

        // Runs the simulator
		Process process = null;
        try {
        	String[] command = {"java", "-jar", "./exe/SubwaySimulator.jar", "--in", "./data/subway.csv", "--out", "./out"};
        	process = new ProcessBuilder(command).start();
        } catch (IOException e) {
        	System.err.println(e);
            e.printStackTrace();
        }
        final Process finalProcess = process;

        // It will destroy the simulator process at the end
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (finalProcess != null) {
                finalProcess.destroy();
            }
        }));
		
		
		// gets all the arguements so we can use them to create an instance of our
		// application
		String username = args[0];
		String password = args[1];
		String newsTopic = args[2];
		String cityCode = args[3];
		String myTrain = args[4]; // must be one of T01-T12
		// String StationDatabaseCsvPath = "../data/subway.csv";
		String StationDatabaseCsvPath = "./data/subway.csv";
		

		// connect to the database to get our ads
		Database db = new Database(username, password);
		ArrayList<Advertisement> ads = db.retrieveAllAdvertisements();

		// create an instance of our application
		Application app = new Application(newsTopic, cityCode, ads, myTrain, StationDatabaseCsvPath);
		app.setVisible(true);
		
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String regex = "(T[0-9]+)\\(([A-Z][0-9]+), ([A-Z])\\)";
        
        Pattern pattern = Pattern.compile(regex);

		app.allTrains = new HashMap<>();
        
        try {
            while ((line = reader.readLine()) != null) {
            	System.out.println(line);
            	Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
               	// System.out.println("Match found train: " + matcher.group(1));
               	// System.out.println("Match found stationName: " + matcher.group(2));
               	// System.out.println("Match found direction: " + matcher.group(3));
					String train = matcher.group(1);
					String stationCode = matcher.group(2);
					String direction = matcher.group(3);

					if (app.myTrain.equals(train)) {
						app.myTrainObject.updateTrainData(stationCode, direction);
						// call the method to speak the current station
						speakText("Current stop is " + app.myTrainObject.getCurrentStation().getStationName());
					}

					app.allTrains.put(train, stationCode);

                }
				app.refresh();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

		// set our application to be visibile

	}

	public Application(String newsTopic, String cityCode, ArrayList<Advertisement> ads, String myTrain, String StationDatabaseCsvPath) {

		this.news = new News(newsTopic);
		this.ads = ads;
		this.weather = new Weather(cityCode);

		// make the 4 panels for each section
		this.advertisementPanel = createAdvertisementPanel(ads);
		this.weatherPanel = createWeatherPanel(weather);
		this.newsPanel = createNewsPanel(news.getTitles());
		this.myTrain = myTrain;
		// this.trainStationPanel = new TrainPanel();
		this.trainsMapPanel = createTrainMapPanel();
		this.trainStationPanel = createTrainStationPanel();

		this.stationDatabase = CSVReader.createStationDatabase(StationDatabaseCsvPath);
		this.myTrainObject = new Train(myTrain, stationDatabase);
		this.csvData = TrainMapPlotter.readCsv(StationDatabaseCsvPath);

		

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
		// add(trainsMapPanel, gbc); // add the panel to the frame // NO LONGER USED> advertisement panel will rather use image icon in alternating fashion

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
		gbc.weighty = 1.5;
		gbc.fill = GridBagConstraints.BOTH;
		add(trainStationPanel, gbc);
	

	}
	
	public void refresh() throws IOException {
		// refresh all panels
		String prevStop = myTrainObject.getPrevStop() == null ? "" : myTrainObject.getPrevStop().getStationName();
		String currStop = myTrainObject.getCurrentStation() == null ? "" : myTrainObject.getCurrentStation().getStationName();
		String nextStop1 = myTrainObject.getNextStops().size() > 0 ? myTrainObject.getNextStops().get(0).getStationName() : "";
		String nextStop2 = myTrainObject.getNextStops().size() > 1 ? myTrainObject.getNextStops().get(1).getStationName() : "";	
		String nextStop3 = myTrainObject.getNextStops().size() > 2 ? myTrainObject.getNextStops().get(2).getStationName() : "";

		previousStationLabel.setText(prevStop);
		currentStationLabel.setText(currStop);
		nextStationLabel1.setText(nextStop1);
		nextStationLabel2.setText(nextStop2);
		nextStationLabel3.setText(nextStop3);

		System.out.println(allTrains);

		TrainMapPlotter.plotAndSavePNG(csvData, allTrains, 1200, 700, "output_graph.png");
		Image scaledImage = new ImageIcon("output_graph.png").getImage().getScaledInstance(800, 400,  java.awt.Image.SCALE_SMOOTH);
		mapImageIcon = new ImageIcon(scaledImage);
		mapLabel.setIcon(mapImageIcon);

	}

	public JPanel createTrainMapPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // create the label for our ads
        mapLabel = new JLabel();
        panel.add(mapLabel);

		return panel;
	}

	/**
     * Gets data from the train station simulator and puts it on top of an image that
     * displays where we currently are.
     * 
     * @param stations List of station names where the first is the previous station,
     *                 the second is the current station, and the next three are the
     *                 upcoming stations.
     * @return a JPanel with image and text over it, that is continuously updating
     */
     public JPanel createTrainStationPanel() {
        // Create the main panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Set the stroke for solid lines (thicker lines)
                g2d.setStroke(new BasicStroke(6)); // Set thickness of the lines
                g2d.setColor(Color.GRAY); // Line color

                // Define the positions for the dots and lines
                int[] xPositions = {100, 300, 500, 700, 900}; // X positions for the dots
                int dotY = 100; // Y position for the dots

                // Draw dots and lines between them
                for (int i = 0; i < xPositions.length; i++) {
                    // Draw the dot
                    if (i == 1) { // Current stop index
                        g2d.setColor(Color.RED); // Highlight current dot in red
                    } else {
                        g2d.setColor(Color.LIGHT_GRAY); // Dot color for other stops
                    }
                    g2d.fill(new Ellipse2D.Double(xPositions[i] - 10, dotY - 10, 20, 20)); // Draw larger dot (20x20)

                    // Draw line to the next dot
                    if (i < xPositions.length - 1) {
                        g2d.draw(new Line2D.Double(xPositions[i], dotY, xPositions[i + 1], dotY));
                    }
                }
            }
        };

        panel.setBackground(Color.WHITE); // Background color
        panel.setPreferredSize(new java.awt.Dimension(950, 250));
        panel.setLayout(null); // Use null layout for absolute positioning

        // Style the labels
        Font stationFont = new Font("Arial", Font.PLAIN, 12); // Smaller font size
        Color[] colors = {Color.BLACK, Color.RED, Color.BLACK, Color.BLACK, Color.BLACK}; // Text color

        // Define the Y position for the labels below the dots
        int labelY = 130; // Ensure this is defined before use

        previousStationLabel = new JLabel("...", SwingConstants.CENTER);
        currentStationLabel = new JLabel("...", SwingConstants.CENTER);
        nextStationLabel1 = new JLabel("...", SwingConstants.CENTER);
        nextStationLabel2 = new JLabel("...", SwingConstants.CENTER);
        nextStationLabel3 = new JLabel("...", SwingConstants.CENTER);

		JLabel[] labels = {previousStationLabel, currentStationLabel, nextStationLabel1, nextStationLabel2, nextStationLabel3};
		for (int i = 0; i < labels.length; i++) {
            JLabel stationLabel = labels[i];
            stationLabel.setFont(stationFont);
            stationLabel.setForeground(colors[i]); // Set text color
            stationLabel.setBounds(50 + i * 200, labelY, 150, 30); // Position labels below the dots
            panel.add(stationLabel);
        }


        return panel;
    }
	
    /**
     * Gets a string of text and audibly says it out loud for the user to hear.
     * 
     * @param text The text to be spoken.
     */
    public static void speakText(String text) {
        try {
            // create new instance of MaryInterface and set the voice to American English
            MaryInterface marytts = new LocalMaryInterface();
            marytts.setVoice("cmu-slt-hsmm");

            // generate audio from the text
            AudioInputStream audio = marytts.generateAudio(text);
            // create player to play the audio
            AudioPlayer player = new AudioPlayer(audio);
            // start the player
            player.start();

        } catch (MaryConfigurationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	

	/**
     * creates a panel that reads the ArrayList of advertisements and continuously displays all the ads in the list,
     * as well as the current location of the trains every once in a while
     * @param ads the ArrayList of Advertisements
     * @return a JPanel with images/gifs of ads continuously looping
     */

	public JPanel createAdvertisementPanel(ArrayList<Advertisement> ads) {

		// create new panel
        JPanel panel = new JPanel(new BorderLayout());

        // create the label for our ads
        JLabel adLabel = new JLabel();
        panel.add(adLabel);

        // timer to switch between advertisements every 10 seconds

        Timer timer = new Timer(3000, new ActionListener() {
        	// start from the second ad
        	int index = 0;
        	// continuously loop through the advertisements and continuously show them
			boolean switchAdMap = false;
        	@Override
            public void actionPerformed(ActionEvent e) {
				ImageIcon imageIcon;
				if (switchAdMap) {
        		index = index % ads.size();
        		byte[] data = ads.get(index).getData(); // get the data from the advertisement
        		imageIcon = new ImageIcon(data); // set the imageIcon with the image data
                index++;
				}
				else {
					imageIcon = mapImageIcon;
				}

        		adLabel.setIcon(imageIcon); // add the image icon to the label
				// alternate between ads and map
				switchAdMap = !switchAdMap;
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
