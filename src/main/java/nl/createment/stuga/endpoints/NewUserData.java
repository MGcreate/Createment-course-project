package nl.createment.stuga.endpoints;

public class NewUserData {

	private String username;
	
	private String password;
	
	public NewUserData() {
		
	}
	
	public NewUserData(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
