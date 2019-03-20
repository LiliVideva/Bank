package bg.sofia.uni.fmi.mjt.project.exceptions;

public class NotLoggedException extends Exception {
	private static final long serialVersionUID = 1920345678L;

	public NotLoggedException() {
		super("Not logged in the system!");
	}

	public NotLoggedException(String message) {
		super(message);
	}

	public NotLoggedException(Throwable cause) {
		super(cause);
	}

	public NotLoggedException(String message, Throwable cause) {
		super(message, cause);
	}

}
