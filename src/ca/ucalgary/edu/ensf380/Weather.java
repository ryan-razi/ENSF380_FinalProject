// not finished yet

package ca.ucalgary.edu.ensf380;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.regex.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Weather {
	
	private String temp;
	private String cityCode;
	
    public Weather(String cityCode) {
		this.cityCode = cityCode;
		findTemperature();
	}

	public static void main(String[] args) {
    	
    	Weather weather = new Weather("5913490");
    	System.out.println(weather.getTemp());
    	
    	
    }
    
    public void findTemperature() {
    	
    	// get the path to the chrome driver
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        
        // chrome driver option to make it so that it doesnt bring up the chrome tab everytime it gets the information
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); 
        
        // initialize the chromedriver
        WebDriver driver = new ChromeDriver(options);

        // create the url and makes the driver go to the desired url
        String url = "https://openweathermap.org/city/" + cityCode;
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
        String temp = matcher.group(1);
        this.temp = temp;
        
        // closer the web driver
        driver.quit();
    	
    }
    

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
    
    
}
