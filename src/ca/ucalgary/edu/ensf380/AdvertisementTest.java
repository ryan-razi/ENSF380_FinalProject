package ca.ucalgary.edu.ensf380;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.Test;

/**
 * AdvertisementTest class to help us test the Advertisement and Database classes
 * @author Ryan Razi
 * @version 1.0
 * @since 1.0
 */

public class AdvertisementTest {

	@Test
	public void testNewsConstructor() {
		byte[] data = { 1 };
		Advertisement ad = new Advertisement("McDonalds", "jpg", data);
		assertNotNull(ad);
	}

	@Test
	public void testGettersAndSetters() {
		byte[] data = { 1 };
		Advertisement ad = new Advertisement("McDonalds", "jpg", data);
		ad.setName("Burger King");
		assertEquals(ad.getName(), "Burger King");
	}

	@Test
	public void testInvalidCredentials() {

		Database db = new Database("invalid_user", "invalid_password");

		try {
			db.retrieveAllAdvertisements();
			fail("Expected Exception to be thrown");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("Access denied"));
		}

	}

}
