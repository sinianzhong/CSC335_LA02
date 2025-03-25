public class User {
	private final String username;
	private final String salt;
	private final String hashedPassword;

	public User(String username, String salt, String hashedPassword) {
		this.username = username;
		this.salt = salt;
		this.hashedPassword = hashedPassword;
	}

	public String getUsername() {
		return username;
	}

	public String getSalt() {
		return salt;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}
}
