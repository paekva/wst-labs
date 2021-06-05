package com.paekva.wstlab4.service;

import com.paekva.wstlab4.database.StudentDAO;
import com.paekva.wstlab4.database.dto.StudentDTO;
import com.paekva.wstlab4.database.entity.Student;
import com.paekva.wstlab4.standalone.App;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.jws.WebMethod;
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
    ) throws SQLException {
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
        return studentDAO.findWithFilters(id, groupNumber, password, email, isLocal, date);
    }

    @DELETE
    @Path("/{id}")
    @WebMethod
    @Produces(MediaType.TEXT_PLAIN)
    public String delete(@PathParam("id") Long id) {
        try {
            if (id == null) {
                return "Id can't be null";
            }
            int delete = studentDAO.delete(id);
            if (delete <= 0) {
                return String.format("Can't delete User. User with specified id: %s not found ", id);
            }
            return String.valueOf(delete);
        } catch (SQLException e) {
            return "SQL exception: " + e.getMessage() + ". State: " + e.getSQLState();
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String update(@PathParam("id") Long id, StudentDTO studentDTO) {
        int update = 0;
        try {
            Date parse = studentDTO.getBirthDate() != null ? new SimpleDateFormat("yyyy-MM-dd").parse(studentDTO.getBirthDate()) : null;
            update = studentDAO.update(id, studentDTO.getEmail(), studentDTO.getPassword(), studentDTO.getGroupNumber(), studentDTO.getIsLocal(), parse);
            if (update < 0) {
                return String.format("Can't update User. User with specified id: %s not found ", id);
            }
        } catch (SQLException e) {
            return "SQL exception: " + e.getMessage() + ". State: " + e.getSQLState();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(update);
    }
}
