package nl.createment.stuga.db.config;

public class UserNotFoundException extends EntityNotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException(String username) {
		super(String.format("User: %s not found!", username));
	}

}
