package nl.createment.stuga.endpoints;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.createment.stuga.businesslogica.ConflictException;
import nl.createment.stuga.businesslogica.ValidationException;
import nl.createment.stuga.db.config.MainRepository;
import nl.createment.stuga.db.config.PasswordRepository;
import nl.createment.stuga.db.entities.Channel;
import nl.createment.stuga.db.entities.Message;
import nl.createment.stuga.db.entities.User;


@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EndPointTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MainRepository mainRepository;
	
	@Autowired
	private PasswordRepository passwordRepository;


	@BeforeAll
	public void setup() {
		mainRepository.clearAllMessages();
		mainRepository.clearAllChannels();
		mainRepository.clearAllUsers();
		
		User bert = new User("Bert");
		Channel c2 = new Channel("2");
		Channel c3 = new Channel("3");

		mainRepository.saveUser(bert);
		passwordRepository.setPassword(bert, "berttest");
		mainRepository.saveChannel(c2);
		mainRepository.saveChannel(c3);
		mainRepository.saveMessage(new Message(bert, "message 1", c3));
		mainRepository.saveMessage(new Message(bert, "message 2", c3));
		}

	@Test
	@WithMockUser(username="Bert", password="berttest",authorities="USER")
	public void channelCreatedTest() throws Exception {
		this.mockMvc.perform(post("/channel").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Channel1\"}")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
		assertNotNull(mainRepository.findChannel("Channel1"));
	}
	
	@Test
	public void getAllChannelsTest() throws Exception {		
		assertEquals(mainRepository.listChannels().size(), 3);	
		}

	@Test
	@WithMockUser(username="Bert", password="berttest",authorities="USER")
	public void messageCreatedTest() throws Exception{
		Channel testChannel = mainRepository.findChannel("3");
		int beforeNewMessage = mainRepository.listMessages(testChannel).size();
		this.mockMvc.perform(post("/message").contentType(MediaType.APPLICATION_JSON).content("{\"content\":\"berttest\",\"channelName\":\"3\"}")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
		assertEquals(mainRepository.listMessages(testChannel).size()-beforeNewMessage,1);
	}
	
	@Test
	@WithMockUser(username="Bert", password="berttest",authorities="USER")
	public void getMessagesAfterTest() throws Exception {
		int messageOid = mainRepository.findMessage("message 1").getOid();
		this.mockMvc.perform(get("/channel/3/message?last="+messageOid)).andExpect(status().isOk());
	}
	
	@Test
	public void postUserTest() throws Exception {
		this.mockMvc.perform(get("/user/exists/Henk")).andExpect(content().string(containsString("false")));
		this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"Henk\",\"password\":\"Henktest1!\"}")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
		this.mockMvc.perform(get("/user/exists/Henk")).andExpect(content().string(containsString("true")));
	}
	
	@Test
	public void postInvalidUserTest() throws Exception {
		MvcResult result = this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"He\",\"password\":\"Henktest1!\"}")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest()).andReturn();
		assertTrue(result.getResolvedException() instanceof ValidationException);
		MvcResult result2 = this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"Henkjeiseenbaas\",\"password\":\"Henktest1!\"}")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest()).andReturn();
		assertTrue(result2.getResolvedException() instanceof ValidationException);
		MvcResult result3 = this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"Henk%u\",\"password\":\"Henktest1!\"}")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest()).andReturn();
		assertTrue(result3.getResolvedException() instanceof ValidationException);
		MvcResult result4 = this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"!henk3\",\"password\":\"Henktest1!\"}")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest()).andReturn();
		assertTrue(result4.getResolvedException() instanceof ValidationException);
	}
	
	@Test
	public void postInvalidPasswordTest() throws Exception {
		MvcResult result = this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"Henk\",\"password\":\"Henktest\"}")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest()).andReturn();
		assertTrue(result.getResolvedException() instanceof ValidationException);

	}
    
	@Test
	public void getAllMessagesTest() throws Exception{
		Channel testChannel = mainRepository.findChannel("3");
		assertEquals(mainRepository.listMessages(testChannel).size(),2);
	}
	
	@Test
	public void userCreatedTest() throws Exception {
		int before = mainRepository.listUsers().size();
		this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"Jan\",\"password\":\"Jantest1!\"}")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
		assertEquals(mainRepository.listUsers().size()-before, 1);		
	}
	
	@Test
	public void specificUserExist() throws Exception {
		this.mockMvc.perform(get("/user/exists/Bert")).andExpect(content().string(containsString("true")));
	}
	
	@Test
	public void passwordIsEncodedTest() throws Exception {
		this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"Bart\",\"password\":\"Barttest1!\"}")
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
		User returnedUser = mainRepository.findUser("Bart");
		String returnedPassword = passwordRepository.getPassword(returnedUser);
		assertNotEquals(returnedPassword ,"Barttest1!");
	}

	@Test
	@WithMockUser(username="Bert", password="berttest",authorities="USER")
	public void postChannelTest_longname() throws Exception {
		MvcResult result = this.mockMvc.perform(post("/channel").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Channel12345678901234567\"}")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest()).andReturn();
		assertTrue(result.getResolvedException() instanceof ValidationException);
	}
	
	@Test
	@WithMockUser(username="Bert", password="berttest",authorities="USER")
	public void postMessageTest_shortmessage() throws Exception{
		MvcResult result = this.mockMvc.perform(post("/message").contentType(MediaType.APPLICATION_JSON).content("{\"content\":\"\",\"channelName\":\"Channel1\"}")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest()).andReturn();
		assertTrue(result.getResolvedException() instanceof ValidationException);
	}
	
	@Test
	@WithMockUser(username="Bert", password="berttest",authorities="USER")
	public void postChannelTest_alreadyExists() throws Exception {
		this.mockMvc.perform(post("/channel").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Channel99\"}")
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
		MvcResult result = this.mockMvc.perform(post("/channel").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Channel99\"}")
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isConflict()).andReturn();
		assertTrue(result.getResolvedException() instanceof ConflictException);
	}

	@Test
	@WithMockUser(username="Bert", password="berttest",authorities="USER")
	public void blankMessageTest() throws Exception{
		MvcResult result = this.mockMvc.perform(post("/message").contentType(MediaType.APPLICATION_JSON).content("{\"content\":\"     \\n    \",\"channelName\":\"Channel1\"}")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest()).andReturn();
		assertTrue(result.getResolvedException() instanceof ValidationException);
	}
}
