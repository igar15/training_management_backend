package com.igar15.training_management.exceptions.handler;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.igar15.training_management.constants.SecurityConstant;
import com.igar15.training_management.exceptions.*;
import com.igar15.training_management.to.MyHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class MyExceptionHandler {

    public static final String HTTP_MESSAGE_NOT_READABLE = "Error while reading request data";



    private final Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<MyHttpResponse> tokenExpiredException(HttpServletRequest request, TokenExpiredException exception) {
        return logAndCreateHttpResponse(HttpStatus.UNAUTHORIZED, request, exception);
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<MyHttpResponse> emailExistException(HttpServletRequest request, EmailExistException exception) {
        return logAndCreateHttpResponse(HttpStatus.CONFLICT, request, exception);
    }

    @ExceptionHandler(WorkoutExistException.class)
    public ResponseEntity<MyHttpResponse> workoutExistException(HttpServletRequest request, WorkoutExistException exception) {
        return logAndCreateHttpResponse(HttpStatus.CONFLICT, request, exception);
    }

    @ExceptionHandler(ExerciseTypeExistException.class)
    public ResponseEntity<MyHttpResponse> exerciseTypeExistException(HttpServletRequest request, ExerciseTypeExistException exception) {
        return logAndCreateHttpResponse(HttpStatus.CONFLICT, request, exception);
    }

    @ExceptionHandler(IllegalRequestDataException.class)
    public ResponseEntity<MyHttpResponse> illegalRequestDataException(HttpServletRequest request, IllegalRequestDataException exception) {
        return logAndCreateHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY, request, exception);
    }

    @ExceptionHandler(MyEntityNotFoundException.class)
    public ResponseEntity<MyHttpResponse> myEntityNotFoundException(HttpServletRequest request, MyEntityNotFoundException exception) {
        return logAndCreateHttpResponse(HttpStatus.UNPROCESSABLE_ENTITY, request, exception);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<MyHttpResponse> methodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException exception) {
        log.warn("Error at request {} : {}", request.getRequestURL(), exception.toString());
        Set<HttpMethod> supportedHttpMethods = exception.getSupportedHttpMethods();
        String supportedMethods = supportedHttpMethods.stream()
                .map(method -> method.toString())
                .collect(Collectors.joining(", "));
        String message = "This request method is not allowed on this endpoint. Please send a '" + supportedMethods + "' request";
        MyHttpResponse myHttpResponse = new MyHttpResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase().toUpperCase(), message);
        return new ResponseEntity<>(myHttpResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class, PropertyReferenceException.class, ValidationException.class, MissingServletRequestParameterException.class, JWTDecodeException.class})
    public ResponseEntity<MyHttpResponse> httpMessageNotReadableException(HttpServletRequest request, Exception exception) {
        log.warn("Error at request {} : {}", request.getRequestURL(), exception.toString());
        MyHttpResponse myHttpResponse = new MyHttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), HTTP_MESSAGE_NOT_READABLE);
        return new ResponseEntity<>(myHttpResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<MyHttpResponse> accountDisabledException(HttpServletRequest request, DisabledException exception) {
        log.warn("Error at request {} : {}", request.getRequestURL(), exception.toString());
        MyHttpResponse myHttpResponse = new MyHttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), SecurityConstant.ACCOUNT_DISABLED);
        return new ResponseEntity<>(myHttpResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<MyHttpResponse> badCredentialsException(HttpServletRequest request, BadCredentialsException exception) {
        log.warn("Error at request {} : {}", request.getRequestURL(), exception.toString());
        MyHttpResponse myHttpResponse = new MyHttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), SecurityConstant.INCORRECT_CREDENTIALS);
        return new ResponseEntity<>(myHttpResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<MyHttpResponse> accessDeniedException(HttpServletRequest request, AccessDeniedException exception) {
        log.warn("Error at request {} : {}", request.getRequestURL(), exception.toString());
        MyHttpResponse myHttpResponse = new MyHttpResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase(), SecurityConstant.NOT_ENOUGH_PERMISSION);
        return new ResponseEntity<>(myHttpResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<MyHttpResponse> lockedException(HttpServletRequest request, LockedException exception) {
        log.warn("Error at request {} : {}", request.getRequestURL(), exception.toString());
        MyHttpResponse myHttpResponse = new MyHttpResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase().toUpperCase(), SecurityConstant.ACCOUNT_LOCKED);
        return new ResponseEntity<>(myHttpResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MyHttpResponse> internalServerErrorException(HttpServletRequest request, Exception exception) {
        return logAndCreateHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, request, exception);
    }


    private ResponseEntity<MyHttpResponse> logAndCreateHttpResponse(HttpStatus httpStatus, HttpServletRequest request, Exception exception) {
        log.warn("Error at request {} : {}", request.getRequestURL(), exception.toString());
        MyHttpResponse myHttpResponse = new MyHttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), exception.getMessage().toUpperCase());
        return new ResponseEntity<>(myHttpResponse, httpStatus);
    }


}
