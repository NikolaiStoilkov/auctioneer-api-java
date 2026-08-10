package com.auctioneer.exceptions;

import java.io.Serial;

/** Thrown when a JWT is malformed or fails verification; mapped to HTTP 401. */
public class InvalidJsonWebToken extends RuntimeException {

	@Serial
    private static final long serialVersionUID = -3920798081682558937L;

	public InvalidJsonWebToken() {
		super("Invalid JWT");
	}
}
