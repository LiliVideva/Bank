package bg.sofia.uni.fmi.mjt.project.exceptions;

public class NoSuchAccountException extends Exception {
	private static final long serialVersionUID = 1728394056L;

	public NoSuchAccountException() {
		super("No such account!");
	}

	public NoSuchAccountException(String message) {
		super(message);
	}

	public NoSuchAccountException(Throwable cause) {
		super(cause);
	}

	public NoSuchAccountException(String message, Throwable cause) {
		super(message, cause);
	}
}
