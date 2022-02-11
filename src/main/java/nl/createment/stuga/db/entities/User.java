package nl.createment.stuga.db.entities;

import javax.persistence.*;

@Entity
@Table(name="messenger")
public class User {
	
	@Id
	@GeneratedValue
	private int oid;
	
	@Column(unique=true)
	private String username;
	
	
	public User() {
		
	}
	
	public User(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public int getOid() {
		return this.oid;
	}
	
	
}
