package nl.createment.stuga.endpoints;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nl.createment.stuga.businesslogica.ChannelLogica;
import nl.createment.stuga.db.config.MainRepository;
import nl.createment.stuga.db.entities.Channel;

@RestController
public class ChannelEndpoint {

	@Autowired
	private MainRepository mainRepository;
	
	@Autowired
	ChannelLogica channelLogica;

	@PostMapping("/channel")
	public void addChannel(@RequestBody NewChannelData newChannelData) {
		channelLogica.checkChannelNameValidity(newChannelData.getName());
		channelLogica.checkChannelAlreadyExists(newChannelData.getName());
		mainRepository.saveChannel(new Channel(newChannelData.getName()));
	}
	
	@GetMapping("/channels")
	public List<Channel> getAllChannels() {
		return mainRepository.listChannels();
	}
	
	@PostMapping("/delete-channel")
	public void deleteChannel(@RequestBody String channelName) {
		channelName = channelName.substring(1, channelName.length()-1);
		mainRepository.deleteChannel(channelName);		
	}
}
