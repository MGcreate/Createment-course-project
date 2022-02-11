package nl.createment.stuga.db.config;

public class ChannelNotFoundException extends EntityNotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChannelNotFoundException(String channelname) {
		super(String.format("Channel: %s not found!", channelname));
	}

}
