package ca.ucalgary.edu.ensf380;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class News {
    
    private String topic;
    private String[] titles;
    private final static String apiKey = "517e93b4e94f45dcb955dca5b3da677d";

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

    // function to help generate the news titles based on the topic
    private String[] generateNews() {
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

                // From the articles, we get up to 10 of the titles, and put it into a string array called titles
                int numArticles = Math.min(10, articles.length());
                titles = new String[numArticles];

                for (int i = 0; i < numArticles; i++) {
                    JSONObject article = articles.getJSONObject(i);
                    String title = article.getString("title");
                    titles[i] = title;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return titles;
    }

}