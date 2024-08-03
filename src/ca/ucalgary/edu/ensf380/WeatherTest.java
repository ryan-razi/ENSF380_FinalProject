package ca.ucalgary.edu.ensf380;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class WeatherTest {
	
	@Test
	public void testNewsConstructor() {
		Weather weather = new Weather("5913490");
		assertNotNull(weather);
	}
	
	@Test
	public void testGettersAndSetters() {
		Weather weather = new Weather("5913490");
		weather.setCityCode("12345");
		assertEquals(weather.getCityCode(), "12345");
	}
	
	// can't really test the exceptions for invalid city code URL, because every city code will default to London and just get the weather there
	// only time an error can really happen is when the page takes too long to load, or if user doesn't have chrome
	
}
