package nl.createment.stuga.db.entities;

import java.util.List;

import javax.persistence.*;

@Entity
public class Channel {
	
	@Id
	@GeneratedValue
	private int oid;
	
	@Column(nullable=false, unique=true)
	private String name;
	
	@OneToMany(mappedBy="channel", orphanRemoval=true)
	private List<Message> messages;
	
	public Channel() {
		
	}
	
	public Channel(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getOid() {
		return this.oid;
	}

}
