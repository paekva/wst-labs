package com.paekva.wstlab6.service.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class StudentExceptionMapper implements ExceptionMapper<StudentServiceException> {
    @Override
    public Response toResponse(StudentServiceException e) {
        return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
}