package com.paekva.wstlab4.service;

import com.paekva.wstlab4.database.StudentDAO;
import com.paekva.wstlab4.database.entity.Student;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@Slf4j
@Path("/students")
@ManagedBean
public class StudentsService {

    @Resource(mappedName = "jdbc/students")
    private DataSource dataSource;

    private StudentDAO studentDAO;

    @PostConstruct
    public void init() {
        studentDAO = new StudentDAO(dataSource);
    }

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
