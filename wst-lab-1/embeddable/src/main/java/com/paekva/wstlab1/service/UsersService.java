package com.paekva.wstlab1.service;

import com.paekva.wstlab1.database.UserDAO;
import com.paekva.wstlab1.database.entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.SQLException;
import java.util.List;

@WebService(serviceName = "users", targetNamespace = "users_namespace")
@AllArgsConstructor
@NoArgsConstructor
public class UsersService {
  @Inject
  private UserDAO userDAO;

  @WebMethod
  public List<User> findAll() throws SQLException {
    return userDAO.findAll();
  }

  @WebMethod
  public List<User> findWithFilters(
      @WebParam(name = "id") Long id,
      @WebParam(name = "login") String login,
      @WebParam(name = "password") String password,
      @WebParam(name = "email") String email,
      @WebParam(name = "gender") Boolean gender,
      @WebParam(name = "registerDate") XMLGregorianCalendar registerDate
  ) throws SQLException {
    return userDAO.findWithFilters(id, login, password, email, gender, registerDate);
  }
}
