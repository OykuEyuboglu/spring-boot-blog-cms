package com.oyku.blog.Security.ratelimit;

public class RateLimitingException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public RateLimitingException(String message) {
		super(message);
	}
}
