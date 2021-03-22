package com.paekva.wstlab2.service;

import com.paekva.wstlab2.database.StudentDAO;
import com.paekva.wstlab2.database.entity.Student;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.SQLException;
import java.util.List;

@WebService(serviceName = "students", targetNamespace = "students_namespace")
@AllArgsConstructor
@NoArgsConstructor
public class StudentsService {
    private StudentDAO studentDAO;

    @WebMethod
    public List<Student> findAll() throws SQLException {
        return studentDAO.findAll();
    }

    @WebMethod
    public List<Student> findWithFilters(
            @WebParam(name = "id") Long id,
            @WebParam(name = "email") String email,
            @WebParam(name = "password") String password,
            @WebParam(name = "groupNumber") String groupNumber,
            @WebParam(name = "isLocal") Boolean isLocal,
            @WebParam(name = "birthDate") XMLGregorianCalendar birthDate
    ) throws SQLException {
        return studentDAO.findWithFilters(id, groupNumber, password, email, isLocal, birthDate);
    }

    @WebMethod
    public boolean delete(@WebParam(name = "id") Long id) throws SQLException {
        return studentDAO.delete(id);
    }

    @WebMethod
    public Long insert(
            @WebParam(name = "email") String email, @WebParam(name = "password") String password,
            @WebParam(name = "groupNumber") String groupNumber, @WebParam(name = "isLocal") Boolean isLocal,
            @WebParam(name = "birthDate") XMLGregorianCalendar birthDate
    ) throws SQLException {
        return studentDAO.insert(email, password, groupNumber, isLocal, birthDate);
    }

    @WebMethod
    public boolean update(
            @WebParam(name = "id") Long id,
            @WebParam(name = "email") String email, @WebParam(name = "password") String password,
            @WebParam(name = "groupNumber") String groupNumber, @WebParam(name = "isLocal") Boolean isLocal,
            @WebParam(name = "birthDate") XMLGregorianCalendar birthDate
    ) throws SQLException {
        return studentDAO.update(id, email, password, groupNumber, isLocal, birthDate);
    }
}
