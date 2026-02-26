package com.darkblue.DarkBlueHotel.exception;

import org.aspectj.bridge.IMessage;

public class OurException  extends RuntimeException{
    public OurException(String message) {
        super(message);
    }
}
