package com.paekva.wstlab6.service;

import com.paekva.wstlab6.database.StudentDAO;
import com.paekva.wstlab6.database.dto.StudentDTO;
import com.paekva.wstlab6.database.entity.Student;
import com.paekva.wstlab6.service.exception.*;
import com.paekva.wstlab6.standalone.App;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Data
@Slf4j
@Path("/students")
public class StudentsService {
    private StudentDAO studentDAO;

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public StudentsService() {
        InputStream dsPropsStream = App.class.getClassLoader()
                .getResourceAsStream("application.properties");
        Properties dsProps = new Properties();
        try {
            dsProps.load(dsPropsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HikariConfig hikariConfig = new HikariConfig(dsProps);
        DataSource ds = new HikariDataSource(hikariConfig);
        studentDAO = new StudentDAO(ds);
    }

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Student> findAll() throws SQLException {
        return studentDAO.findAll();
    }

    @GET
    @Path("/filter")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Student> findWithFilters(
            @QueryParam("id") Long id,
            @QueryParam("email") String email,
            @QueryParam("password") String password,
            @QueryParam("groupNumber") String groupNumber,
            @QueryParam("isLocal") Boolean isLocal,
            @QueryParam("birthDate") String birthDate
    ) throws SqlException {
        Date date;
        if (birthDate != null) {
            try {
                date = sdf.parse(birthDate);
            } catch (ParseException e) {
                date = null;
            }
        } else {
            date = null;
        }
        try {
            return studentDAO.findWithFilters(id, groupNumber, password, email, isLocal, date);
        } catch (SQLException ex) {
            throw new SqlException(ex.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete(@PathParam("id") Long id) throws StudentServiceException, SqlException {
        try {
            if (id == null) {
                String message = "Id can't be null";
                throw new StudentServiceException(message);
            }
            int delete = studentDAO.delete(id);
            if (delete <= 0) {
                String message = String.format("По данному id: %s никого не найдено", id);
                throw new StudentServiceException(message);
            }
            return String.valueOf(delete);
        } catch (SQLException e) {
            String message = "SQL exception: " + e.getMessage() + ". State: " + e.getSQLState();
            throw new SqlException("SQL exception: " + e.getMessage() + ". State: " + e.getSQLState());
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String update(@PathParam("id") Long id, StudentDTO studentDTO) throws StudentServiceException, SqlException {
        int update = 0;
        try {
            Date parse = studentDTO.getBirthDate() != null ? new SimpleDateFormat("yyyy-MM-dd").parse(studentDTO.getBirthDate()) : null;
            update = studentDAO.update(id, studentDTO.getEmail(), studentDTO.getPassword(), studentDTO.getGroupNumber(), studentDTO.getIsLocal(), parse);
            if (update < 0) {
                String message = String.format("По данному id: %s никого не найдено", id);
                throw new StudentServiceException(message);
            }
        } catch (SQLException e) {
            String message = "SQL exception: " + e.getMessage() + ". State: " + e.getSQLState();
            throw new SqlException(message);
        } catch (ParseException e) {
            e.printStackTrace();
            String message = "Cannot parse date " + studentDTO.getBirthDate();
            throw new StudentServiceException(message);
        }
        return String.valueOf(update);
    }


    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String insert(StudentDTO studentDTO) throws StudentServiceException, SqlException {
        if (studentDTO.getEmail() == null || studentDTO.getPassword() == null || studentDTO.getGroupNumber() == null || studentDTO.getIsLocal() == null || studentDTO.getBirthDate() == null) {
            String message = "Все поля должны быть заполнены.";
            throw new StudentServiceException(message);
        }
        try {
            Date parse = new SimpleDateFormat("yyyy-MM-dd").parse(studentDTO.getBirthDate());
            return String.valueOf(studentDAO.insert(studentDTO.getPassword(),
                    studentDTO.getEmail(), studentDTO.getGroupNumber(), studentDTO.getIsLocal(), parse)
            );
        } catch (SQLException e) {
            throw new SqlException("SQL exception: " + e.getMessage() + ". State: " + e.getSQLState());
        } catch (ParseException e) {
            String message = String.format("Cannot parse date " + studentDTO.getBirthDate());
            throw new StudentServiceException(message);
        }
    }

}
