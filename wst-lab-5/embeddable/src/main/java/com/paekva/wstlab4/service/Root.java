package com.paekva.wstlab4.service;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class Root extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>(Arrays.asList(StudentsService.class));
    }
}