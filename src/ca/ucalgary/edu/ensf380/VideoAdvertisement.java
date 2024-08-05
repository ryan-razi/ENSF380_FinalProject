package ca.ucalgary.edu.ensf380;

/**
 * An extension of the advertisement class that inherits all of its attributes, but more specifically used for videos.
 * @author Ryan Razi
 * @version 1.0
 * @since 1.0
 */

public class VideoAdvertisement extends Advertisement {

	private double videoLength;

	public VideoAdvertisement(String name, String type, byte[] data, double videoLength) {
		super(name, type, data);
		this.videoLength = videoLength;
	}

	public double getVideoLength() {
		return videoLength;
	}

	public void setVideoLength(double videoLength) {
		this.videoLength = videoLength;
	}
	
	
}
