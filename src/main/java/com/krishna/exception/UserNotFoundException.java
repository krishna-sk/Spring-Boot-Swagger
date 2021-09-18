package com.krishna.exception;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private Date time;
	
	public UserNotFoundException(String message, Date time) {
		super(message);
		this.time=time;
	}


}
