package giwi.shared;

import java.io.Serializable;

public class SecurityViolationException extends Exception implements Serializable{

	private static final long serialVersionUID = 6190680750542575828L;

	private String message;
	
	@SuppressWarnings("unused")
	private SecurityViolationException() {	}
	
	public SecurityViolationException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
