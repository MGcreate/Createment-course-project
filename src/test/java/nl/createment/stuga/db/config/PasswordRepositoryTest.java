package nl.createment.stuga.db.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import nl.createment.stuga.db.entities.User;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class PasswordRepositoryTest {
	
	@Autowired
	private MainRepository mainRepository;
	
	@Autowired
	private PasswordRepository passwordRepository;
	
	@BeforeAll
	public void setup() {		
		mainRepository.clearAllMessages();
		mainRepository.clearAllChannels();
		mainRepository.clearAllUsers();
		
		User daan =new User("Daan"); 
		User laurens = new User("Laurens");
		
		mainRepository.saveUser(daan);
		mainRepository.saveUser(laurens);
		
	}
	
	@Test
	void testGetSetPassword() {
		User laurens = mainRepository.findUser("Laurens");
		assertNull(this.passwordRepository.getPassword(laurens));
		this.passwordRepository.setPassword(laurens, "testpassword");
		assertEquals(this.passwordRepository.getPassword(laurens),"testpassword");
	}
	
	@Test
	void testGetDefaultAuthorities() {
		User laurens = mainRepository.findUser("Laurens");
		assertEquals(this.passwordRepository.getAuthorities(laurens),"USER");
	}
	
	@Test
	void testSetAuthorities() {
		User laurens = mainRepository.findUser("Laurens");
		this.passwordRepository.setAuthorities(laurens, "ADMIN");
		assertEquals(this.passwordRepository.getAuthorities(laurens),"ADMIN");
	}

}
