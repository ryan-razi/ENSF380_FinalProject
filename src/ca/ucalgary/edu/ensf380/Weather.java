package ca.ucalgary.edu.ensf380;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.regex.*;

/**
 * Weather class that holds the weather for a city, as well as the city code
 * related to that city. Uses the Selenium Web Driver, as well as google chrome
 * in order to retrieve the dynamic HTML code, and then parses through it using
 * RegEx to get the temperature
 * 
 * @author Ryan Razi
 * @version 1.0
 * @since 1.0
 */

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

	/**
	 * Uses Selenium Web Driver and chrome to get the HTML code, so we can parse it
	 * and get the temperature.
	 */

	public void findTemperature() {

		try {
			// get the path to the chrome driver
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

			// chrome driver option to make it so that it doesnt bring up the chrome tab
			// everytime it gets the information
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
			String regex = "class=\"heading\">([-]{0,1}\\d{1,2}°C)";

			// compile the pattern
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(pageSource);

			// find the temperature
			if (matcher.find()) {
				String temp = matcher.group(1);
				this.temp = temp;
			} else {
				throw new RuntimeException("Temperature not found in the page source");
			}

			// closer the web driver
			driver.quit();
		} catch (Exception e) {
			System.out.println("Error occurred: " + e.getMessage());
		}
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
