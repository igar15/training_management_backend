package com.igar15.training_management.exceptions;

import com.igar15.training_management.to.MyHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestControllerAdvice
public class MyExceptionHandler {

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
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods().iterator().next());
        String message = "This request method is not allowed on this endpoint. Please send a '" + supportedMethod + "' request";
        MyHttpResponse myHttpResponse = new MyHttpResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase().toUpperCase(), message);
        return new ResponseEntity<>(myHttpResponse, HttpStatus.METHOD_NOT_ALLOWED);
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
