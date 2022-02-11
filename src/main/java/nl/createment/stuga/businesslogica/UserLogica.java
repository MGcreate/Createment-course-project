package nl.createment.stuga.businesslogica;

public class UserLogica {
	
	public static void checkUsernameValidity(String username) {
		boolean usernameIsValid	= username.matches("^[a-zA-Z0-9]{3,13}$");	
		if (!usernameIsValid) {
			throw new ValidationException("Invalid username");
		}
	}
	
	public static void checkPasswordValidity(String password) {
		String AtLeastOneSpecialCharOneCapitalOneSmallOneNumberAndBetweenEightToTwentyCharacters = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&*$]).{8,20}$";
		boolean passwordIsValid = password.matches(AtLeastOneSpecialCharOneCapitalOneSmallOneNumberAndBetweenEightToTwentyCharacters);
		if (!passwordIsValid) {
			throw new ValidationException("Invalid password");
		}
	}
	
}
