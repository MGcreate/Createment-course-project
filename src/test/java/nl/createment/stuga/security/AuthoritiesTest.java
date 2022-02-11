package nl.createment.stuga.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import nl.createment.stuga.db.config.MainRepository;
import nl.createment.stuga.db.config.PasswordRepository;
import nl.createment.stuga.db.entities.User;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthoritiesTest {
	
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
		this.passwordRepository.setAuthorities(laurens, "ADMIN");
		
	}
	
	@Test
	public void getCollectionAuthoritiesTest() {
		MyUserDetails laurensUD = new MyUserDetails(mainRepository.findUser("Laurens"),this.passwordRepository);
		MyUserDetails daanUD = new MyUserDetails(mainRepository.findUser("Daan"),this.passwordRepository);
		Set<GrantedAuthority> laurensRefSet = new HashSet<GrantedAuthority>();
		Set<GrantedAuthority> daanRefSet = new HashSet<GrantedAuthority>();
		GrantedAuthority userRole = new SimpleGrantedAuthority("USER");
		GrantedAuthority adminRole = new SimpleGrantedAuthority("ADMIN");
		laurensRefSet.add(adminRole);
		laurensRefSet.add(userRole);
		daanRefSet.add(userRole);
		assertEquals(laurensUD.getAuthorities(), laurensRefSet);
		assertEquals(daanUD.getAuthorities(), daanRefSet);
	}

}
