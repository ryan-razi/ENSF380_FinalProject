package ca.ucalgary.edu.ensf380;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * NewsTest class to help us test the News class
 * @author Ryan Razi
 * @version 1.0
 * @since 1.0
 */

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
	public void testIllegalArgumentException() {
	    try {
	        News news = new News("dasdasd");
	        fail("Expected IllegalArgumentException to be thrown");
	    } catch (IllegalArgumentException e) {
	        assertEquals("No articles found for the given topic.", e.getMessage());
	    }
	}

}
