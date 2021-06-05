package com.paekva.wstlab4.service;

import com.paekva.wstlab4.database.StudentDAO;
import com.paekva.wstlab4.database.entity.Student;
import com.paekva.wstlab4.standalone.App;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
}
