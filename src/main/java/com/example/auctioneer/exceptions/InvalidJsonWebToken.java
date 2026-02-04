package com.example.auctioneer.exceptions;

import java.io.Serial;

public class InvalidJsonWebToken extends RuntimeException {

	@Serial
    private static final long serialVersionUID = -3920798081682558937L;

	public InvalidJsonWebToken() {
		super("Invalid JWT");
	}
}
