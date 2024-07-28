package ca.ucalgary.edu.ensf380;

public class VideoAdvertisement extends Advertisement {
	
	private double videoLength;
	
	public VideoAdvertisement(String name, String type, byte[] data, double videoLength) {
		super(name, type, data);
		this.videoLength = videoLength;
	}

}
