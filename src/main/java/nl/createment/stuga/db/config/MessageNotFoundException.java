package nl.createment.stuga.db.config;

public class MessageNotFoundException extends EntityNotFoundException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MessageNotFoundException(String messageContent) {
		super(String.format("Message: %s not found!", messageContent));
	}

}
