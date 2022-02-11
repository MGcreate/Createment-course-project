package nl.createment.stuga.endpoints;

public class NewChannelData {

	private String name;

	public NewChannelData() {

	}

	public NewChannelData(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
