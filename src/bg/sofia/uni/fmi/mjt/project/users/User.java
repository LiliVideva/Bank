package bg.sofia.uni.fmi.mjt.project.users;

import java.io.PrintWriter;
import java.io.Serializable;

class User implements Cloneable, Serializable {
	private static final long serialVersionUID = 1357924680L;

	private String username;
	private int password;
	private String email;
	private Status status;

	User(String username, int password, String email, Status status) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.status = status;
	}

	User(String username, String passwordString, String email, Status status) {
		this(username, passwordString.hashCode(), email, status);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + password;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		User other = (User) obj;
		return (((email == null && other.email == null) || email.equals(other.email)) && (status != other.status)
				&& ((username == null && other.username == null) || username.equals(other.username)));
	}

	@Override
	public User clone() {
		User clonedUser = new User(this.username, this.password, this.email, this.status);
		return clonedUser;
	}

	void printUserDetails(PrintWriter printWriter) {
		printWriter.printf("Username: %s%n", username);
		printWriter.printf("Email: %s%n", email);
		printWriter.printf("Status: %s%n", status);
	}

	String getUsername() {
		return username;
	}

	int getPassword() {
		return password;
	}

	String getEmail() {
		return email;
	}

	Status getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return String.format("User[Name: %s, Email: %s, Status: %s]", username, email, status.toString());
	}
}