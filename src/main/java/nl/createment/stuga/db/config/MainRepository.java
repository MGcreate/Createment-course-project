package nl.createment.stuga.db.config;

import java.util.List;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nl.createment.stuga.db.entities.Channel;
import nl.createment.stuga.db.entities.Message;
import nl.createment.stuga.db.entities.User;

@Service
@Transactional
public class MainRepository {

	@Autowired
	private EntityManager entityManager;
	
	public List<User> listUsers() {
		TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
		return query.getResultList();
	}
	
	private TypedQuery<User> lookForUser(String username){
		TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username",
				User.class);
		query.setParameter("username", username);
		return query;
	}
	
	private TypedQuery<Channel> lookForChannel(String channelname) {
		TypedQuery<Channel> query = entityManager.createQuery("SELECT c FROM Channel c WHERE c.name = :channelname",
				Channel.class);
		query.setParameter("channelname", channelname);
		return query;
	}

	public User findUser(String username) {
		TypedQuery<User> query = lookForUser(username);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			throw new UserNotFoundException(username);
		}
	}
	
	public boolean userExists(String username) {
		try {
			lookForUser(username).getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}
	
	public boolean channelExists(String channelname) {
		try {
			lookForChannel(channelname).getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}

	public Channel findChannel(String name) {
		TypedQuery<Channel> query = entityManager.createQuery("Select c FROM Channel c WHERE c.name = :name",
				Channel.class);
		query.setParameter("name", name);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			throw new ChannelNotFoundException(name);
		}
	}

	public Message findMessage(String content) {
		TypedQuery<Message> query = entityManager.createQuery("SELECT m from Message m where m.content = :content",
				Message.class);
		query.setParameter("content", content);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			throw new MessageNotFoundException(content);
		}
	}

	public void saveUser(User user) {
		entityManager.persist(user);
	}

	public void saveMessage(Message message) {
		entityManager.persist(message);
	}

	public void saveChannel(Channel channel) {
		entityManager.persist(channel);
	}

	public List<Message> listMessages(Channel channel) {
		TypedQuery<Message> query = entityManager.createQuery("SELECT m FROM Message m WHERE m.channel = :channel ORDER BY sent_at",
				Message.class);
		query.setParameter("channel", channel);
		return query.getResultList();
	}

	public List<Channel> listChannels() {
		TypedQuery<Channel> query = entityManager.createQuery("Select c from Channel c", Channel.class);
		return query.getResultList();
	}
	
	public void clearAllChannels() {
		Query query = entityManager.createQuery("DELETE from Channel c");
		query.executeUpdate();
	}
	
	public void clearAllMessages() {
		Query query = entityManager.createQuery("DELETE from Message m");
		query.executeUpdate();
	}
	
	public void clearAllMessages(Channel channel) {
		Query query = entityManager.createQuery("DELETE from Message m WHERE m.channel = :channel");
		query.setParameter("channel", channel);
		query.executeUpdate();	
	}
	
	public void clearAllUsers() {
		Query query = entityManager.createQuery("DELETE from User m");
		query.executeUpdate();
	}

	public List<Message> listMessages(Channel channel, int oid) {
		TypedQuery<Message> query = entityManager.createQuery("SELECT m FROM Message m, Message n WHERE m.channel = :channel AND n.oid = :oid AND m.timestamp >= n.timestamp ORDER BY m.timestamp",
				Message.class);
		query.setParameter("channel", channel);
		query.setParameter("oid", oid);
		return query.getResultList();
	}
	
	public void deleteChannel(String channelName) {
		this.clearAllMessages(this.findChannel(channelName));
		Query query = entityManager.createQuery("DELETE from Channel c WHERE c.name = :channelName");
		query.setParameter("channelName", channelName);
		query.executeUpdate();
	}
}
