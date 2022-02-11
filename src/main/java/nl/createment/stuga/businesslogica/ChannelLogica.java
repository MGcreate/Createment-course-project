package nl.createment.stuga.businesslogica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nl.createment.stuga.db.config.MainRepository;

@Component
public class ChannelLogica {
	
	@Autowired
	private MainRepository mainRepository;
	
	public void checkChannelNameValidity(String name){
		boolean channelNameIsValid = name.matches("^[a-zA-Z0-9 ]{3,20}$");
		if (!channelNameIsValid) {
			throw new ValidationException("Invalid channelname");
		} 
	}
	
	public void checkChannelAlreadyExists(String channelname) {
		if (mainRepository.channelExists(channelname)) {
			throw new ConflictException("Channel already exists");
		}
	}
}
