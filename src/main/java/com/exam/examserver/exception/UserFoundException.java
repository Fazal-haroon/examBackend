package com.exam.examserver.exception;

public class UserFoundException extends Exception{
    public UserFoundException() {
        super("user with this username is already there in DB !! try with another username");
    }

    public UserFoundException(String msg){
        super(msg);
    }
}
