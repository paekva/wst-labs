package com.paekva.wstlab3.service;

import com.paekva.wstlab3.database.StudentDAO;
import com.paekva.wstlab3.database.entity.Student;
import com.paekva.wstlab3.exceptions.StudentsServiceException;
import com.paekva.wstlab3.exceptions.StudentsServiceFault;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@WebService(serviceName = "students", targetNamespace = "students_namespace")
@NoArgsConstructor
public class StudentsService {
    @Resource
    WebServiceContext wsctx;

    private StudentDAO studentDAO;

    public StudentsService(StudentDAO studentDAO){
        this.studentDAO = studentDAO;
    }

    @WebMethod
    public List<Student> findAll() throws Exception {
        checkAuth();

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
    ) throws Exception {
        checkAuth();

        return studentDAO.findWithFilters(id, groupNumber, password, email, isLocal, birthDate);
    }

    @WebMethod
    public boolean delete(@WebParam(name = "id") Long id) throws StudentsServiceException {
        checkAuth();

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
        checkAuth();

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
        checkAuth();

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

    private void checkAuth() throws StudentsServiceException {
        MessageContext mctx = wsctx.getMessageContext();

        //get detail from request headers
        Map http_headers = (Map) mctx.get(MessageContext.HTTP_REQUEST_HEADERS);

        List authList = (List) http_headers.get("Authorization");

        if (authList == null) {
            throw new StudentsServiceException("Нет заголовка авторизации", new StudentsServiceFault());
        }

        String header = authList.get(0).toString();
        String base64 = header.split(" ")[1];
        String[] creds = (new String(Base64.getDecoder().decode(base64))).split(":");

        for(int i = 0; i < creds.length; i++) {
            System.out.println("cred " + creds[i]);
        }

        String username = creds[0];
        String password = creds[1];

        //Should validate username and password with database
        if (!(username.equals("admin") && password.equals("123456"))) {
            throw new StudentsServiceException("Не авторизован", new StudentsServiceFault());
        }
    }
}
