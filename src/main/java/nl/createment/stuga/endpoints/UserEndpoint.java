package nl.createment.stuga.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nl.createment.stuga.businesslogica.UserLogica;
import nl.createment.stuga.db.config.MainRepository;
import nl.createment.stuga.db.config.PasswordRepository;
import nl.createment.stuga.db.entities.User;
import nl.createment.stuga.security.BasicConfiguration;

@RestController
public class UserEndpoint {

	@Autowired
	private MainRepository mainRepository;
	
	@Autowired
	private PasswordRepository passwordRepository;

	@PostMapping("/user")
	public void addUser(@RequestBody NewUserData newUserData) {
		UserLogica.checkUsernameValidity(newUserData.getUsername());
		UserLogica.checkPasswordValidity(newUserData.getPassword());
		User newUser = new User(newUserData.getUsername());
		mainRepository.saveUser(newUser);
		passwordRepository.setPassword(newUser, BasicConfiguration.getPasswordEncoder().encode(newUserData.getPassword()));
		if(mainRepository.listUsers().size()==1) {
			this.passwordRepository.setAuthorities(newUser, "ADMIN");
		}
	}
	
	@GetMapping("/user/exists/{username}")
	public boolean userExists(@PathVariable String username) {
		return mainRepository.userExists(username);
	}
}
