package nl.createment.stuga.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import nl.createment.stuga.db.config.PasswordRepository;
import nl.createment.stuga.db.entities.User;

public class MyUserDetails implements UserDetails {

	private static final long serialVersionUID = -3333517047593572822L;
	
	private User user;
	private String password;
	private Set<GrantedAuthority> authorities;

    public MyUserDetails(User user, PasswordRepository passwordRepository) {
        this.user = user;
        this.password = passwordRepository.getPassword(this.user);	
        this.authorities = new HashSet<GrantedAuthority>();
        String newAuthorities = passwordRepository.getAuthorities(this.user);
        GrantedAuthority userRole = new SimpleGrantedAuthority("USER");
		GrantedAuthority adminRole = new SimpleGrantedAuthority("ADMIN");
        switch(newAuthorities) {
        case "ADMIN":
        	this.authorities.add(adminRole);   
        case "USER": 
        	this.authorities.add(userRole);   
        }	
    }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
