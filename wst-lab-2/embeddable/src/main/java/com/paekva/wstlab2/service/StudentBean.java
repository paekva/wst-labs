package com.paekva.wstlab2.service;

import com.paekva.wstlab2.database.StudentDAO;
import lombok.Data;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

@Data
@ApplicationScoped
public class StudentBean {
  @Resource(lookup = "jdbc/students")
  private DataSource dataSource;

  @Produces
  public StudentDAO studentDAO() {
    return new StudentDAO(dataSource);
  }
}
