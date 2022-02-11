package nl.createment.stuga.db.entities;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.*;

@Entity
public class Message {

	// message contents
	// timestamp
	// author
	// channel where message was posted

	@Id
	@GeneratedValue
	private int oid;

	@Column(nullable = false, length = 1000)
	private String content;

	@Column(nullable = false, name="sent_at")
	private LocalDateTime timestamp;

	@ManyToOne(optional = false)
	private User user;

	@ManyToOne(optional = false)
	private Channel channel;

	public Message() {

	}

	public Message(User user, String content, Channel channel) {
		this.content = content;
		this.user = user;
		this.channel = channel;

		this.timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}

	public String getContent() {
		return this.content;
	}

	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}

	public User getUser() {
		return this.user;
	}

	public Channel getChannel() {
		return this.channel;
	}

	public int getOid() {
		return this.oid;
	}
}