package nl.createment.stuga.endpoints;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nl.createment.stuga.businesslogica.MessageLogica;
import nl.createment.stuga.db.config.MainRepository;
import nl.createment.stuga.db.entities.Channel;
import nl.createment.stuga.db.entities.Message;
import nl.createment.stuga.db.entities.User;

@RestController
public class MessageEndpoint {

	@Autowired
	private MainRepository mainRepository;

	@PostMapping("/message")
	public void addMessage(@RequestBody NewMessageData newMessageData, Principal principal) {
    		MessageLogica.checkMessageLength(newMessageData.getContent());
		User user = mainRepository.findUser(principal.getName());
		Channel channel = mainRepository.findChannel(newMessageData.getChannelName());
		mainRepository.saveMessage(new Message(user, newMessageData.getContent(), channel));
	}
	
	@GetMapping("/channel/{channelName}")
	public List<Message> getAllMessages(@PathVariable String channelName) {
		return mainRepository.listMessages(mainRepository.findChannel(channelName));
	}
	
	@GetMapping("/channel/{channelName}/message")
	public List<Message> getMessagesAfter(@PathVariable String channelName, @RequestParam int last) {
		return mainRepository.listMessages(mainRepository.findChannel(channelName),last);
	}
}
