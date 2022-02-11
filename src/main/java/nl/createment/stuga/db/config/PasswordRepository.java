package nl.createment.stuga.db.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Service;
import nl.createment.stuga.config.DatabaseProperties;
import nl.createment.stuga.db.entities.User;

@Service
public class PasswordRepository {
	
	private Connection con;
	
	public PasswordRepository(DatabaseProperties dbProperties) {
		
		String url = dbProperties.getUrl();
		String username = dbProperties.getUsername();
		String password = dbProperties.getPassword();
		
		try{
			this.con = DriverManager.getConnection(url,username,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getPassword(User user) {
		try {
			PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM Messenger WHERE oid = ?;");
			preparedStatement.setInt(1,user.getOid());
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			return resultSet.getString(3);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void setPassword(User user, String newPassword) {
		try {
			PreparedStatement preparedStatement = con.prepareStatement("UPDATE Messenger SET password = ? WHERE oid = ?");
			preparedStatement.setString(1, newPassword);
			preparedStatement.setInt(2,user.getOid());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getAuthorities(User user) {
		try {
			PreparedStatement preparedStatement = con.prepareStatement("SELECT authorities FROM Messenger WHERE oid = ?;");
			preparedStatement.setInt(1,user.getOid());
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			return resultSet.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void setAuthorities(User user, String newAuthorities) {
		try {
			PreparedStatement preparedStatement = con.prepareStatement("UPDATE Messenger SET authorities = ? WHERE oid = ?");
			preparedStatement.setString(1, newAuthorities);
			preparedStatement.setInt(2,user.getOid());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
