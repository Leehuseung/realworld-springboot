package com.kr.realworldspringboot.exception;


public class DuplicateException extends RuntimeException {
    public DuplicateException(String message){
        super(message);
    }
}

