package com.springframework.exceptions;

public class UserServiceException extends RuntimeException{
 
	private static final long serialVersionUID = 1348771109171435607L;

    public UserServiceException() {
    }

    public UserServiceException(String message)
	{
		super(message);
	}

    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserServiceException(Throwable cause) {
        super(cause);
    }

    public UserServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
