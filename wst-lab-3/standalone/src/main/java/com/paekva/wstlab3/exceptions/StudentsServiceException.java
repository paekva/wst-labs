package com.paekva.wstlab3.exceptions;

import lombok.Getter;

import javax.xml.ws.WebFault;

@WebFault(faultBean = "com.paekva.wstlab3.exceptions.UserServiceFault")
public class StudentsServiceException extends Exception {
    @Getter
    private final StudentsServiceFault faultInfo;

    public StudentsServiceException(String message, StudentsServiceFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public StudentsServiceException(String message, Throwable cause, StudentsServiceFault faultInfo) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }
}
