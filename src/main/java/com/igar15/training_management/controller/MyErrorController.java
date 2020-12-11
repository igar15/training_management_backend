package com.igar15.training_management.controller;

import com.igar15.training_management.to.MyHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
@ApiIgnore
public class MyErrorController implements ErrorController {

    private final Logger log = LoggerFactory.getLogger(MyErrorController.class);

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public ResponseEntity<MyHttpResponse> notFound404(HttpServletRequest request) {
        Object forwardRequestUrl = request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        log.warn("Not mapping for URL : " + forwardRequestUrl);
        MyHttpResponse myHttpResponse = new MyHttpResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(), "There is no mapping for this URL: " + forwardRequestUrl);
        return new ResponseEntity<>(myHttpResponse, HttpStatus.NOT_FOUND);
    }
}
