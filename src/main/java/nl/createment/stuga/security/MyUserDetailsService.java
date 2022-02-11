package nl.createment.stuga.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import nl.createment.stuga.db.config.MainRepository;
import nl.createment.stuga.db.config.PasswordRepository;
import nl.createment.stuga.db.entities.User;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private MainRepository mainRepository;
	
	@Autowired
	private PasswordRepository passwordRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = mainRepository.findUser(username);
		MyUserDetails myUD = new MyUserDetails(user, passwordRepository);
		return myUD;
	}

}
