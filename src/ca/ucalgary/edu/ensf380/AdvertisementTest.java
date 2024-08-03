package ca.ucalgary.edu.ensf380;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


import org.junit.Test;

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
