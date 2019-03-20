package bg.sofia.uni.fmi.mjt.project.exceptions;

public class InvalidFieldException extends Exception {
	private static final long serialVersionUID = 1627384950L;

	public InvalidFieldException() {
		super();
	}

	public InvalidFieldException(String message) {
		super(message);
	}

	public InvalidFieldException(Throwable cause) {
		super(cause);
	}

	public InvalidFieldException(String message, Throwable cause) {
		super(message, cause);
	}

}
