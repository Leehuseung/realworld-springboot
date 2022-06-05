package com.kr.realworldspringboot.common;

import com.kr.realworldspringboot.exception.DuplicateException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class MemberControllerAdvice {

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Object> duplicateCustomException(DuplicateException ce) {
        Map<String, Object> body = new LinkedHashMap<>();

        JSONObject json = new JSONObject();
        JSONObject errors = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        jsonArray.add("has already been taken");

        errors.put(ce.getMessage(),jsonArray);

        json.put("errors",errors);
        body.put("errors", errors);

        return new ResponseEntity<>(body,HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
