package nl.createment.stuga.endpoints;

public class NewMessageData {
	
	private String channelName;
	private String content;

	public NewMessageData() {
	}
	
	public NewMessageData(String content, String channelName) {
		this.content = content;
		this.channelName = channelName;
	}

	public String getContent() {
		return this.content;
	}
	
	public String getChannelName() {
		return this.channelName;
	}

}
