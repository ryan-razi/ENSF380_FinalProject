package ca.ucalgary.edu.ensf380;

/**
 * Advertisement class used for storing the information about the ads.
 * @author Ryan Razi
 * @version 1.0
 * @since 1.0
 */

public class Advertisement {

    private String name;
    private String type;
    private byte[] data;

    public Advertisement(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
