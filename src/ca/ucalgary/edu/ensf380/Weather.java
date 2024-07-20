// not finished yet

package ca.ucalgary.edu.ensf380;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.regex.*;

import java.time.LocalDateTime;

public class Weather {
	
	private int temp;
	private String date;
	private String cityCode;
	
    public static void main(String[] args) {
    	
        // get the path to the chrome driver
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        
        // chrome driver option to make it so that it doesnt bring up the chrome tab everytime it gets the information
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); 
        
        // initialize the chromedriver
        WebDriver driver = new ChromeDriver(options);

        // create the url and makes the driver go to the desired url
        String url = "https://openweathermap.org/city/5913490";
        driver.get(url);

        // wait for the page to fully load
        try {
            Thread.sleep(5000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // get the HTML content of the page we are currently on
        String pageSource = driver.getPageSource();
        
        // regex pattern to find the weather
        String regex = "class=\"heading\">([0-9][0-9]°C|[0-9]°C)";
        
        // compile the pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pageSource);

        // find the temperature
        matcher.find();
        String temperature = matcher.group(1);
        System.out.println("Temperature: " + temperature);
        
        // closer the web driver
        driver.quit();
    }
}
