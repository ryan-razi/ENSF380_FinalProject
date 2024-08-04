package ca.ucalgary.edu.ensf380;

import org.junit.Test;
import static org.junit.Assert.*;

public class NewsTest {
	
	@Test
	public void testNewsConstructor() {
		News news = new News("Calgary");
		assertNotNull(news);
	}
	
	@Test
	public void testGettersAndSetters() {
		News news = new News("Calgary");
		news.setTopic("Canada");
		assertEquals(news.getTopic(), "Canada");
	}
	
	@Test
	public void testResponseCodeException() {
	    try {
	        News news = new News(""); 
	        fail("Expected RuntimeException to be thrown");
	    } catch (RuntimeException e) {
	        assertTrue(e.getMessage().startsWith("HttpResponseCode:")); 
	    }
	}

	@Test
	public void testIllegalArgumentException() {
	    try {
	        News news = new News("ascfafcac"); 
	        fail("Expected IllegalArgumentException to be thrown");
	    } catch (IllegalArgumentException e) {
	        assertEquals("No articles found for the given topic.", e.getMessage());
	    }
	}
	
}
