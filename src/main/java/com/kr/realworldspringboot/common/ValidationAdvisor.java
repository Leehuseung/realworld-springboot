package com.kr.realworldspringboot.common;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@ControllerAdvice
public class ValidationAdvisor extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();

        JSONObject json = new JSONObject();
        JSONObject errors = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        jsonArray.add("can't be blank");

        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        for (int i = 0; i < allErrors.size(); i++) {
            errors.put(allErrors.get(i).getDefaultMessage(), jsonArray);
            break;
        }

        json.put("errors",errors);
        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }
}
