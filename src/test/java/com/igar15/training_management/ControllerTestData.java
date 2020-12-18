package com.igar15.training_management;

import com.igar15.training_management.constants.SecurityConstant;
import com.igar15.training_management.exceptions.handler.MyExceptionHandler;
import com.igar15.training_management.to.MyHttpResponse;
import org.springframework.http.HttpStatus;

public class ControllerTestData {

    public static final String USERS_URI = "/users";
    public static final String EXERCISE_TYPES_URI = "/exerciseTypes";
    public static final String WORKOUTS_URI = "/workouts";
    public static final String EXERCISES_URI = "/exercises";

    public static final MyHttpResponse FORBIDDEN_RESPONSE = new MyHttpResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN,
            HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase(), SecurityConstant.FORBIDDEN_MESSAGE);

    public static final MyHttpResponse ACCESS_DENIED_RESPONSE = new MyHttpResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN,
            HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase(), SecurityConstant.NOT_ENOUGH_PERMISSION);


    public static final MyHttpResponse BAD_CREDENTIALS_RESPONSE = new MyHttpResponse(HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(),
            SecurityConstant.INCORRECT_CREDENTIALS);

    public static final MyHttpResponse DISABLED_RESPONSE = new MyHttpResponse(HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(),
            SecurityConstant.ACCOUNT_DISABLED);

    public static final MyHttpResponse LOCKED_RESPONSE = new MyHttpResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED,
            HttpStatus.UNAUTHORIZED.getReasonPhrase().toUpperCase(), SecurityConstant.ACCOUNT_LOCKED);

    public static final MyHttpResponse EMAIL_ALREADY_EXIST_RESPONSE = new MyHttpResponse(HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT,
            HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(),
            "USER WITH EMAIL USER1@TEST.RU ALREADY EXISTS");


    public static final MyHttpResponse NOT_VALID_USER_NAME_BLANK_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),"PLEASE CORRECT THESE ERRORS: NAME MUST NOT BE BLANK");

    public static final MyHttpResponse NOT_VALID_USER_NAME_SIZE_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "PLEASE CORRECT THESE ERRORS: NAME LENGTH MUST BE BETWEEN 2 AND 100 CHARACTERS");

    public static final MyHttpResponse NOT_VALID_USER_PASSWORD_BLANK_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "PLEASE CORRECT THESE ERRORS: PASSWORD MUST NOT BE BLANK");

    public static final MyHttpResponse NOT_VALID_USER_PASSWORD_SIZE_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "PLEASE CORRECT THESE ERRORS: PASSWORD LENGTH MUST BE BETWEEN 5 AND 32 CHARACTERS");

    public static final MyHttpResponse NOT_VALID_USER_EMAIL_BLANK_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "PLEASE CORRECT THESE ERRORS: EMAIL MUST NOT BE BLANK");

    public static final MyHttpResponse NOT_VALID_USER_EMAIL_PATTERN_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "PLEASE CORRECT THESE ERRORS: EMAIL MUST ACCORD EMAIL PATTERN");

    public static final MyHttpResponse USER_MUST_BE_NEW_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "USERTO{ID=100, NAME='NEW USER', EMAIL='NEWEMAIL@TEST.COM'} MUST BE NEW (ID=NULL)");

    public static final MyHttpResponse USER_MUST_BE_WITH_ID_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "USERTO{ID=1001, NAME='USER1 UPDATED', EMAIL='USER1@TEST.RU UPDATED'} MUST BE WITH ID=1000");

    public static final MyHttpResponse USER_UPDATE_METHOD_NOT_ALLOWED_RESPONSE = new MyHttpResponse(HttpStatus.METHOD_NOT_ALLOWED.value(),
            HttpStatus.METHOD_NOT_ALLOWED,
            HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase().toUpperCase(),
            "This request method is not allowed on this endpoint. Please send a 'GET, POST' request");

    public static final MyHttpResponse USER_NOT_FOUND_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "NOT FOUND USER WITH ID: 10");

    public static final MyHttpResponse EMAIL_VERIFIED_RESPONSE = new MyHttpResponse(HttpStatus.OK.value(),
            HttpStatus.OK,
            HttpStatus.OK.getReasonPhrase().toUpperCase(),
            "Your email was successfully verified.");

    public static final MyHttpResponse BAD_REQUEST_DATA_RESPONSE = new MyHttpResponse(HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(),
            MyExceptionHandler.HTTP_MESSAGE_NOT_READABLE);

    public static final MyHttpResponse TOKEN_EXPIRED_RESPONSE = new MyHttpResponse(HttpStatus.UNAUTHORIZED.value(),
            HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase().toUpperCase(),
            "THE TOKEN HAS EXPIRED ON TUE DEC 15 19:16:27 MSK 2020.");

    public static final MyHttpResponse PASSWORD_RESET_REQUEST_RESPONSE = new MyHttpResponse(HttpStatus.OK.value(),
            HttpStatus.OK,
            HttpStatus.OK.getReasonPhrase().toUpperCase(),
            "The message with link to reset your password was sent to " + "user1@test.ru");

    public static final MyHttpResponse PASSWORD_RESET_REQUEST_EMAIL_NOT_FOUND_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "NOT FOUND USER WITH EMAIL: USER99@YANDEX.RU");

    public static final MyHttpResponse PASSWORD_RESET_SUCCESS_RESPONSE = new MyHttpResponse(HttpStatus.OK.value(),
            HttpStatus.OK,
            HttpStatus.OK.getReasonPhrase().toUpperCase(),
            "Your password was successfully reset");

    public static final MyHttpResponse PASSWORD_RESET_MODEL_TOKEN_BLANK_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "PLEASE CORRECT THESE ERRORS: PASSWORD RESET TOKEN MUST NOT BE BLANK");

    public static final MyHttpResponse PASSWORD_RESET_MODEL_PASSWORD_BLANK_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "PLEASE CORRECT THESE ERRORS: PASSWORD MUST NOT BE BLANK");

    public static final MyHttpResponse PASSWORD_RESET_MODEL_NOT_VALID_PASSWORD_SIZE_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "PLEASE CORRECT THESE ERRORS: PASSWORD LENGTH MUST BE BETWEEN 5 AND 32 CHARACTERS");






    public static final MyHttpResponse EXERCISE_TYPE_NOT_OWN_NOT_FOUND_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "NOT FOUND EXERCISE TYPE WITH ID: 1007");

    public static final MyHttpResponse EXERCISE_TYPE_NOT_FOUND_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "NOT FOUND EXERCISE TYPE WITH ID: 10");

    public static final MyHttpResponse EXERCISE_TYPE_ALREADY_EXIST_RESPONSE = new MyHttpResponse(HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT,
            HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(),
            "EXERCISE TYPE WITH NAME USER1 EXERCISE TYPE 1 ALREADY EXISTS");

    public static final MyHttpResponse NOT_VALID_EXERCISE_TYPE_NAME_BLANK_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),"PLEASE CORRECT THESE ERRORS: NAME MUST NOT BE BLANK");

    public static final MyHttpResponse NOT_VALID_EXERCISE_TYPE_NAME_SIZE_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "PLEASE CORRECT THESE ERRORS: NAME LENGTH MUST BE BETWEEN 2 AND 100 CHARACTERS");

    public static final MyHttpResponse NOT_VALID_EXERCISE_TYPE_MEASURE_BLANK_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "PLEASE CORRECT THESE ERRORS: MEASURE MUST NOT BE BLANK");

    public static final MyHttpResponse NOT_VALID_EXERCISE_TYPE_MEASURE_NOT_EXIST_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "PLEASE CORRECT THESE ERRORS: MEASURE XXX DOES NOT EXIST");

    public static final MyHttpResponse EXERCISE_TYPE_MUST_BE_WITH_ID_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "EXERCISETYPETO{ID=10, NAME='USER1 EXERCISE TYPE 1 UPDATED', MEASURE='KILOMETERS'} MUST BE WITH ID=1005");




    public static final MyHttpResponse WORKOUT_NOT_OWN_NOT_FOUND_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "NOT FOUND WORKOUT WITH ID: 1012");

    public static final MyHttpResponse WORKOUT_NOT_FOUND_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "NOT FOUND WORKOUT WITH ID: 10");

    public static final MyHttpResponse WORKOUT_ALREADY_EXIST_RESPONSE = new MyHttpResponse(HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT,
            HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(),
            "WORKOUT WITH DATE 2020-11-28 AND TIME 13:00 ALREADY EXISTS");

    public static final MyHttpResponse NOT_VALID_WORKOUT_DATETIME_NULL_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),"PLEASE CORRECT THESE ERRORS: DATETIME MUST NOT BE NULL");

    public static final MyHttpResponse WORKOUT_MUST_BE_WITH_ID_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "WORKOUTTO{ID=10, DATETIME=2020-12-04T13:40} MUST BE WITH ID=1009");



    public static final MyHttpResponse EXERCISE_NOT_OWN_NOT_FOUND_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "NOT FOUND EXERCISE WITH ID: 1020");

    public static final MyHttpResponse EXERCISE_NOT_FOUND_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "NOT FOUND EXERCISE WITH ID: 10");

    public static final MyHttpResponse EXERCISE_NOT_FOUND_FOR_DIFFERENT_WORKOUT_RESPONSE = new MyHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase().toUpperCase(),
            "NOT FOUND EXERCISE WITH ID: 1014");

}
