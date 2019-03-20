package bg.sofia.uni.fmi.mjt.project.exceptions;

public class NoSuchUserException extends Exception {
	private static final long serialVersionUID = 1829304567L;

	public NoSuchUserException() {
		super();
	}

	public NoSuchUserException(String message) {
		super(message);
	}

	public NoSuchUserException(Throwable cause) {
		super(cause);
	}

	public NoSuchUserException(String message, Throwable cause) {
		super(message, cause);
	}

}