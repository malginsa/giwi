package giwi.shared;

import java.io.Serializable;

public class SecurityViolationException extends Exception implements Serializable{

	private static final long serialVersionUID = 6190680750542575828L;

	private String message;
	
	private SecurityViolationException() {
		this("Security violation");
	}
	
	public SecurityViolationException(String message) {
//		super(message);
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
