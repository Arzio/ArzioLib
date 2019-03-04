package com.arzio.arziolib.api.exception;

public class FinderException extends ReflectiveOperationException{

	private static final long serialVersionUID = 1L;
	
	public FinderException(String message) {
		super(message);
	}
	
	public FinderException(Exception e) {
		super(e);
	}
}
