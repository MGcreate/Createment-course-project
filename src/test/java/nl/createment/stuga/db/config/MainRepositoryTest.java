package nl.createment.stuga.db.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import nl.createment.stuga.db.entities.Channel;
import nl.createment.stuga.db.entities.Message;
import nl.createment.stuga.db.entities.User;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class MainRepositoryTest {
	
	@Autowired
	private MainRepository mainRepository;
	
	@BeforeAll
	public void setup() {
		mainRepository.clearAllMessages();
		mainRepository.clearAllChannels();
		mainRepository.clearAllUsers();
		
		User bart =new User("Bart"); 
		User jan = new User("Jan");
		Channel c1 = new Channel("1");
		Channel c2 = new Channel("2");
		
		mainRepository.saveUser(bart);
		mainRepository.saveUser(jan);
		mainRepository.saveChannel(c1);
		mainRepository.saveChannel(c2);
		mainRepository.saveMessage(new Message(bart, "Dit is Bart in c1", c1));
		mainRepository.saveMessage(new Message(jan, "Dit is Jan in c2", c2));
		mainRepository.saveMessage(new Message(bart, "Dit is Bart in c2, hoi Jan!", c2));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mainRepository.saveMessage(new Message(bart, "Geef me alles hierna", c2));
		mainRepository.saveMessage(new Message(bart, "Dit is Bart weer in c2, hoi Jan!", c2));
		mainRepository.saveMessage(new Message(jan, "Dit is Jan weer in c2, hoi Bart!", c2));
		mainRepository.saveMessage(new Message(bart, "Zo is het wel weer genoeg", c2));
	}
	
	@Test
	public void MessageBartShouldExist() {	
		assertNotNull(mainRepository.findMessage("Dit is Bart in c1"));
	}
	
	@Test
	public void MessageHenkShouldNotExist() {
		assertThrows(MessageNotFoundException.class, () -> {mainRepository.findMessage("");});
	}
	
	@Test
	public void ChannelExistsTest() {
		assertTrue(mainRepository.channelExists("1"));
		assertFalse(mainRepository.channelExists("9"));
	}
	
	@Test
	public void findChannelTest() {
		assertNotNull(mainRepository.findChannel("1"));
	}
	
	@Test
	public void notFindChannelTest() {
		assertThrows(ChannelNotFoundException.class, () -> {mainRepository.findChannel("A");});
	}
	
	@Test
	public void notFindUserTest() {
		assertThrows(UserNotFoundException.class, () -> {mainRepository.findUser("Bob");});
	}
	
	@Test
	public void findUserTest() {
		assertTrue(mainRepository.userExists("Jan"));
		assertFalse(mainRepository.userExists("Otto"));
	}
	
	@Test
	public void userCreatedTest() {
		int before = mainRepository.listUsers().size();
		User henk = new User("Henk");
		mainRepository.saveUser(henk);
		assertEquals(mainRepository.listUsers().size()-before, 1);		
	}

	@Test
	public void listMessagesTest() {
		Message refMessage = mainRepository.findMessage("Geef me alles hierna");
		assertEquals(mainRepository.listMessages(mainRepository.findChannel("2")).size(),6);
		List<Message> messagesAfter = mainRepository.listMessages(mainRepository.findChannel("2"),refMessage.getOid());
		assertEquals(messagesAfter.size(),4);
	}
	
	@Test
	public void deleteMessagesFromChannelTest() {
		Channel c5 = new Channel("5");
		mainRepository.saveChannel(c5);
		mainRepository.saveMessage(new Message(mainRepository.findUser("Bart"), "Dit is Bart in c5", c5));
		mainRepository.saveMessage(new Message(mainRepository.findUser("Jan"), "Dit is Jan in c5", c5));
		mainRepository.clearAllMessages(c5);
		assertEquals(mainRepository.listMessages(c5).size(),0);
		assertEquals(mainRepository.listMessages(mainRepository.findChannel("2")).size(),6);
	}
	
	@Test
	public void deleteChannelTest() {
		int beforeNewChannel = mainRepository.listChannels().size();
		Channel c3 = new Channel("3");
		mainRepository.saveChannel(c3);
		assertEquals(mainRepository.listChannels().size()-beforeNewChannel, 1);
		mainRepository.deleteChannel("3");
		assertEquals(mainRepository.listChannels().size(),beforeNewChannel);
		assertThrows(ChannelNotFoundException.class, () -> {mainRepository.deleteChannel("bestaatniet");});
		assertEquals(mainRepository.listChannels().size(),beforeNewChannel);
	}
	
	@Test
	public void deleteChannelWithMessagesTest() {
		int beforeNewChannel = mainRepository.listChannels().size();
		Channel c4 = new Channel("4");
		mainRepository.saveChannel(c4);
		mainRepository.saveMessage(new Message(mainRepository.findUser("Bart"), "Dit is Bart in c4", c4));
		assertEquals(mainRepository.listChannels().size()-beforeNewChannel, 1);
		assertNotNull(mainRepository.findMessage("Dit is Bart in c4"));
		mainRepository.deleteChannel("4");
		assertEquals(mainRepository.listChannels().size(),beforeNewChannel);
		assertThrows(MessageNotFoundException.class, () -> {mainRepository.findMessage("Dit is Bart in c4");});
	}
}
