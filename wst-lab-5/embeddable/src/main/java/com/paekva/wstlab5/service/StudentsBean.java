package com.paekva.wstlab5.service;

import com.paekva.wstlab5.database.StudentDAO;
import lombok.Data;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

@Data
@ManagedBean
public class StudentsBean {
    @Resource(mappedName = "jdbc/students")
    private DataSource dataSource;

    @Produces
    public StudentDAO studentDAO() {
        return new StudentDAO(dataSource);
    }
}
