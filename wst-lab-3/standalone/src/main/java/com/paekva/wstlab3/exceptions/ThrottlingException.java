package com.paekva.wstlab3.exceptions;

import javax.xml.ws.WebFault;

@WebFault(faultBean = "com.paekva.wstlab3.exceptions.ThrottlingException")
public class ThrottlingException extends Exception {
    public ThrottlingException(String message) {
        super(message);
    }
}