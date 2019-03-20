package bg.sofia.uni.fmi.mjt.project.exceptions;

public class ViolationException extends Exception {
	private static final long serialVersionUID = 2143658709L;

	public ViolationException() {
		super();
	}

	public ViolationException(String message) {
		super(message);
	}

	public ViolationException(Throwable cause) {
		super(cause);
	}

	public ViolationException(String message, Throwable cause) {
		super(message, cause);
	}

}