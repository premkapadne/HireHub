package com.hirehub.contants;

public class ApplicationConstants {

    private ApplicationConstants() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";


    public static final String ACTIVE_STATUS = "ACTIVE";

    public static final String  NEW_MESSAGE = "NEW";
    public static final String  CLOSED_MESSAGE = "CLOSED";

    public static final String  SYSTEM = "SYSTEM";

    public static final String ROLE_JOB_SEEKER = "ROLE_JOB_SEEKER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_EMPLOYER = "ROLE_EMPLOYER";

    public static final String PENDING = "PENDING";

}
