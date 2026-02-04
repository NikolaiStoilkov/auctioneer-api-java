package com.example.auctioneer.constants;

public final class SecurityConstants {

    private SecurityConstants() {}

    public static final String LOGIN_URL = "/api/login";
    public static final String LOGOUT_URL = "/api/logout";
    public static final String JWT_SECRET = "n2r5u8x/A%D*G-KaPdSgVkYp3s6v9y$B&E(H+MbQeThWmZq4t7w!z%C*F-J@NcRf";
    public static final String JWT_COOKIE_NAME = "AUCTIONEER-JWT";
    public static final int JWT_EXPIRATION_TIME_SECONDS = 60 * 60;
    public static final int JWT_EXPIRATION_TIME_MILISECONDS = 1000 * JWT_EXPIRATION_TIME_SECONDS;

}
