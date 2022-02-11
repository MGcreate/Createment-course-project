package nl.createment.stuga.businesslogica;

public class MessageLogica {
	
	public static void checkMessageLength(String content){
		content = content.trim();
		if (content.length() < 1 || content.length() > 1000) {
			throw new ValidationException("Message content is too short or too long. Please reconsider");
		} 
	}
}
