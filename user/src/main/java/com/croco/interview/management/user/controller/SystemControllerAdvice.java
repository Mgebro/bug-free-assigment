package com.croco.interview.management.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class SystemControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        var map = new HashMap<String, String>();
        map.put("message", exception.getLocalizedMessage());
        return ResponseEntity.badRequest().body(map);
    }
}
