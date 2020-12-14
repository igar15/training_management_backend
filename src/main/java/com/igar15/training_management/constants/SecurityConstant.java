package com.igar15.training_management.constants;

public class SecurityConstant {
    public static final String TRAINING_MANAGEMENT_LLC = "Training management, LLC"; // this is optional, it is like description of our company that sends these tokens
    public static final String TRAINING_MANAGEMENT_ADMINISTRATION = "Training management Portal"; // this is also optional stuff
    public static final long EMAIL_VERIFICATION_TOKEN_EXPIRATION_TIME = 86_400_000; // 1 day
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final long PASSWORD_RESET_TOKEN_EXPIRATION_TIME = 3600000; // 1 hour
    public static final long AUTHORIZATION_TOKEN_EXPIRATION_TIME = 432_000_000; // 5 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_AUTHORIZATION_TOKEN_HEADER = "Authorization-Token";
    public static final String ROLES = "roles";
    public static final String LOGIN_URL = "/users/login";
    public static final String SIGN_UP_URL = "/users";
    public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";
    public static final String PASSWORD_RESET_REQUEST_URL = "/users/password-reset-request/**";
    public static final String PASSWORD_RESET_URL = "/users/resetPassword";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String OPTIONS_HTTP_METHOD = "Options";
    public static final String ACCOUNT_DISABLED = "Your account is disabled. If this is an error, please contact administration";
    public static final String INCORRECT_CREDENTIALS = "Username / password incorrect. Please try again";
    public static final String NOT_ENOUGH_PERMISSION = "You do not have enough permission";
    public static final String ACCOUNT_LOCKED = "Your account has been locked. Please contact administration";








}
