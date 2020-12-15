package com.igar15.training_management;

import com.igar15.training_management.constants.SecurityConstant;
import com.igar15.training_management.to.MyHttpResponse;
import org.springframework.http.HttpStatus;

public class ControllerTestData {

    public static final MyHttpResponse FORBIDDEN_RESPONSE = new MyHttpResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN,
            HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase(), SecurityConstant.FORBIDDEN_MESSAGE);

    public static final MyHttpResponse BAD_CREDENTIALS_RESPONSE = new MyHttpResponse(HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(),
            SecurityConstant.INCORRECT_CREDENTIALS);

    public static final MyHttpResponse DISABLED_RESPONSE = new MyHttpResponse(HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(),
            SecurityConstant.ACCOUNT_DISABLED);

    public static final MyHttpResponse LOCKED_RESPONSE = new MyHttpResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED,
            HttpStatus.UNAUTHORIZED.getReasonPhrase().toUpperCase(), SecurityConstant.ACCOUNT_LOCKED);

    public static final MyHttpResponse NOT_VALID_BLANK_USER_NAME_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),"PLEASE CORRECT THESE ERRORS: NAME MUST NOT BE BLANK");

    public static final MyHttpResponse NOT_VALID_SIZE_USER_NAME_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "PLEASE CORRECT THESE ERRORS: NAME LENGTH MUST BE BETWEEN 2 AND 100 CHARACTERS");

    public static final MyHttpResponse NOT_VALID_BLANK_USER_PASSWORD_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "PLEASE CORRECT THESE ERRORS: PASSWORD MUST NOT BE BLANK");

    public static final MyHttpResponse NOT_VALID_SIZE_USER_PASSWORD_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "PLEASE CORRECT THESE ERRORS: PASSWORD LENGTH MUST BE BETWEEN 5 AND 32 CHARACTERS");

    public static final MyHttpResponse NOT_VALID_BLANK_USER_EMAIL_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "PLEASE CORRECT THESE ERRORS: EMAIL MUST NOT BE BLANK");

    public static final MyHttpResponse NOT_VALID_PATTERN_USER_EMAIL_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "PLEASE CORRECT THESE ERRORS: EMAIL MUST ACCORD EMAIL PATTERN");

    public static final MyHttpResponse USER_MUST_BE_NEW_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "USERTO{ID=2000, NAME='NEW USER', EMAIL='NEWEMAIL@TEST.COM'} MUST BE NEW (ID=NULL)");





}
