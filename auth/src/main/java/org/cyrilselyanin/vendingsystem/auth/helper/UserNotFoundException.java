package org.cyrilselyanin.vendingsystem.auth.helper;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(String message) {
		super(message);
	}

}
