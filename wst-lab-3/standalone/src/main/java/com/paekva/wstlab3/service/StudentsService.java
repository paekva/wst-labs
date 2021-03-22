package com.paekva.wstlab3.service;

import com.paekva.wstlab3.database.StudentDAO;
import com.paekva.wstlab3.database.entity.Student;
import com.paekva.wstlab3.exceptions.StudentsServiceException;
import com.paekva.wstlab3.exceptions.StudentsServiceFault;
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
    public boolean delete(@WebParam(name = "id") Long id) throws StudentsServiceException {
        try {
            if (id == null) {
                String message = "Id can't be null";
                throw new StudentsServiceException(message, new StudentsServiceFault(message));
            }
            boolean delete = studentDAO.delete(id);
            if (!delete) {
                String message = String.format("По данному id: %s никого не найдено", id);
                throw new StudentsServiceException(message, new StudentsServiceFault(message));
            }
            return delete;
        } catch (SQLException e) {
            String message = "SQL exception: " + e.getMessage() + ". State: " + e.getSQLState();
            throw new StudentsServiceException(message, e, new StudentsServiceFault(message));
        }
    }

    @WebMethod
    public Long insert(
            @WebParam(name = "email") String email, @WebParam(name = "password") String password,
            @WebParam(name = "groupNumber") String groupNumber, @WebParam(name = "isLocal") Boolean isLocal,
            @WebParam(name = "birthDate") XMLGregorianCalendar birthDate
    ) throws StudentsServiceException {
        if (email == null || password == null || groupNumber == null || isLocal == null || birthDate == null) {
            String message = "Все поля должны быть заполнены.";
            throw new StudentsServiceException(message, null, new StudentsServiceFault(message));
        }

        try {
            return studentDAO.insert(email, password, groupNumber, isLocal, birthDate);
        } catch (SQLException e) {
            String message = "SQL exception: " + e.getMessage() + ". State: " + e.getSQLState();
            throw new StudentsServiceException(message, e, new StudentsServiceFault(message));
        }
    }

    @WebMethod
    public boolean update(
            @WebParam(name = "id") Long id,
            @WebParam(name = "email") String email, @WebParam(name = "password") String password,
            @WebParam(name = "groupNumber") String groupNumber, @WebParam(name = "isLocal") Boolean isLocal,
            @WebParam(name = "birthDate") XMLGregorianCalendar birthDate
    ) throws StudentsServiceException {
        try {
            boolean update = studentDAO.update(id, email, password, groupNumber, isLocal, birthDate);
            if (!update) {
                String message = String.format("По данному id: %s никого не найдено", id);
                throw new StudentsServiceException(message, new StudentsServiceFault(message));
            }
            return update;
        } catch (SQLException e) {
            String message = "SQL exception: " + e.getMessage() + ". State: " + e.getSQLState();
            throw new StudentsServiceException(message, e, new StudentsServiceFault(message));
        }
    }
}
