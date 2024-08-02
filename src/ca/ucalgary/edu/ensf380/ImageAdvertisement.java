package ca.ucalgary.edu.ensf380;

/**
 * An extension of the advertisement class that inherits all of its attributes, but more specifically used for images.
 * @author Ryan Razi
 * @version 1.0
 * @since 1.0
 */
public class ImageAdvertisement extends Advertisement {

	public ImageAdvertisement(String name, String type, byte[] data) {
		super(name, type, data);
	}

}
