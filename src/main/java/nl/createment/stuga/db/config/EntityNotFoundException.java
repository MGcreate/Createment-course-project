package nl.createment.stuga.db.config;

public abstract class EntityNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EntityNotFoundException(String notFoundString) {
		super(notFoundString);
	}

}
