package ca.ucalgary.edu.ensf380;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * News class that contains a topic and a whole bunch of titles of articles based on that topic.
 * Gets its articles from newapi.org.
 * @author Ryan Razi
 * @version 1.0
 * @since 1.0
 */

public class News {
    
    private String topic;
    private String[] titles;
    private final static String apiKey = "517e93b4e94f45dcb955dca5b3da677d";
    
    public static void main(String[] args) {
        News news = new News("Calgary");
        String[] titles = news.getTitles();
        
        for (String title : titles) {
            System.out.println(title);
        }
    }
    
    // Constructor for generating news based on your topic
    public News(String topic) {
        this.topic = topic;
        this.titles = generateNews();
    }

    // setters and getters
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }
    
    /**
     * Generates news based on a topic that is given.
     * @return an array of Strings containing news article titles about the topic given
     * @throws IllegalArgumentException 
     */
    
    public String[] generateNews() throws IllegalArgumentException {
    	
    	ArrayList<String> validTitles = new ArrayList<>();
    	
        try {
            // Create the url for the news we want to get information about
            URL url = new URL("https://newsapi.org/v2/everything?q=" + topic + "&language=en&apiKey=" + apiKey);

            // Open connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Get the response code of the connection
            int responseCode = conn.getResponseCode();

            // 200 means good
            if (responseCode != 200) {
                // If we get a bad response code, print it to the screen
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                // Get information from the website and put it in StringBuilder variable since theres so much text
                StringBuilder informationString = new StringBuilder();

                // Scanner scans the json file it gets from the API and puts in the informationString variable
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }

                // Close scanner
                scanner.close();

                // Parse through the JSON response to get the articles
                JSONObject jsonResponse = new JSONObject(informationString.toString());
                JSONArray articles = jsonResponse.getJSONArray("articles");
                
                // If we can't find an articles, throw an exception
                if (articles.length() == 0) {
                    throw new IllegalArgumentException("No articles found for the given topic.");
                }
                
                // From the articles, we get up to 10 of the titles, and put it into a string array called titles
                for (int i = 0; i < articles.length() && validTitles.size() < 10; i++) {
                    JSONObject article = articles.getJSONObject(i);
                    String title = article.getString("title");
                    
                    // sometimes it gave "[Removed]" for some reason so here we make sure "[Removed]" doesn't get added to the ArrayList
                    if (!title.equals("[Removed]")) {
                        validTitles.add(title);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return validTitles.toArray(new String[0]);
    }
}
