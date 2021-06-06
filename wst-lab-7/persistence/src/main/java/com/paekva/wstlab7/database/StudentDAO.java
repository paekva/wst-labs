package com.paekva.wstlab7.database;

import com.paekva.wstlab7.database.entity.Student;
import com.paekva.wstlab7.database.util.CriteriaBuilder;
import com.paekva.wstlab7.database.util.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import javax.sql.DataSource;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class StudentDAO {
    private final DataSource dataSource;

    private final String TABLE_NAME = "students";

    private final String ID = "id";

    private final String PASSWORD = "password";

    private final String EMAIL = "email";

    private final String GROUP_NUMBER = "group_number";

    private final String IS_LOCAL = "is_local";

    private final String BIRTH_DATE = "birth_date";

    private final String[] columnNames = {ID, PASSWORD, EMAIL, GROUP_NUMBER, IS_LOCAL, BIRTH_DATE};

    public List<Student> findAll() throws SQLException {
        log.info("Find all");
        try (Connection connection = dataSource.getConnection()) {
            java.sql.Statement statement = connection.createStatement();
            CriteriaBuilder cb = new CriteriaBuilder();
            String query = cb.select()
                    .selectors(columnNames)
                    .from(TABLE_NAME).toString();
            log.debug("Query string {}", query);
            statement.execute(query);
            return resultSetToList(statement.getResultSet());
        }
    }

    public List<Student> findWithFilters(
            Long id,
            String groupNumber,
            String password,
            String email,
            Boolean isLocal,
            XMLGregorianCalendar birthDate
    ) throws SQLException {
        Date bDate = birthDate == null ? null : birthDate.toGregorianCalendar().getTime();
        log.debug("Find with filters: {} {} {} {} {} {}", id, password, email, groupNumber, isLocal, bDate);
        Stream<? extends Serializable> params = Stream.of(id, password, email, groupNumber, isLocal, bDate);
        if (params.allMatch(Objects::isNull)) {
            return findAll();
        }

        CriteriaBuilder cb = new CriteriaBuilder();
        cb = cb.select()
                .selectors(columnNames)
                .from(TABLE_NAME);

        Predicate where = new Predicate();
        Predicate predicate = addEqualsPredicate(where, ID, id);
        predicate = addEqualsPredicate(predicate, PASSWORD, password);
        predicate = addEqualsPredicate(predicate, EMAIL, email);
        predicate = addEqualsPredicate(predicate, GROUP_NUMBER, groupNumber);
        predicate = addEqualsPredicate(predicate, IS_LOCAL, isLocal);
        if (birthDate != null) {
            predicate = addEqualsPredicate(predicate, BIRTH_DATE, new SimpleDateFormat("yyyy.MM.dd")
                    .format(bDate));
        }

        cb = cb.where(predicate);

        String c = cb.toString();
        log.debug("Query string {}", cb);
        try (Connection connection = dataSource.getConnection()) {
            java.sql.Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(c);
            return resultSetToList(rs);
        }
    }

    private Predicate addEqualsPredicate(Predicate where, String columnName, Object value) {
        if (value != null) {
            where.and(columnName + " = '" + value.toString() + "'");
        }
        return where;
    }

    private List<Student> resultSetToList(ResultSet rs) throws SQLException {
        List<Student> result = new ArrayList<>();
        while (rs.next()) {
            result.add(toEntity(rs));
        }
        return result;
    }

    private Student toEntity(ResultSet rs) throws SQLException {
        long id = rs.getLong(ID);
        String password = rs.getString(PASSWORD);
        String email = rs.getString(EMAIL);
        String groupNumber = rs.getString(GROUP_NUMBER);
        Boolean isLocal = rs.getBoolean(IS_LOCAL);
        Date birthDate = rs.getDate(BIRTH_DATE);
        return new Student(id, password, email, groupNumber, isLocal, birthDate);
    }

    public boolean update(
            Long id, String email, String password, String groupNumber,
            Boolean isLocal, XMLGregorianCalendar birthDate
    ) throws SQLException {
        Date bDate = birthDate == null ? null : birthDate.toGregorianCalendar().getTime();
        log.debug(
                "Update entity with id: {}. New values: {} {} {} {} {} ",
                id,
                email,
                password,
                groupNumber,
                isLocal,
                bDate);

        CriteriaBuilder cb = new CriteriaBuilder();
        cb = cb.update(TABLE_NAME);

        final ArrayList<Map.Entry<String, String>> toUpdate = new ArrayList<>();
        if (email != null) {
            var emailColumn = new AbstractMap.SimpleImmutableEntry<String, String>(EMAIL, email);
            toUpdate.add(emailColumn);
        }
        if (password != null) {
            var passwordColumn = new AbstractMap.SimpleImmutableEntry<String, String>(PASSWORD, password);
            toUpdate.add(passwordColumn);
        }
        if (groupNumber != null) {
            var groupNumberColumn = new AbstractMap.SimpleImmutableEntry<String, String>(GROUP_NUMBER, groupNumber);
            toUpdate.add(groupNumberColumn);
        }
        if (isLocal != null) {
            var isLocalColumn = new AbstractMap.SimpleImmutableEntry<String, String>(
                    IS_LOCAL,
                    isLocal.toString());
            toUpdate.add(isLocalColumn);
        }
        if (bDate != null) {
            var birthDateColumn = new AbstractMap.SimpleImmutableEntry<String, String>(
                    BIRTH_DATE,
                    new SimpleDateFormat("yyyy.MM.dd")
                            .format(bDate));
            toUpdate.add(birthDateColumn);
        }

        cb.setColumns(toUpdate);

        Predicate predicate = new Predicate();
        cb.where(predicate.and(ID + " = " + id.toString()));

        String c = cb.toString();
        log.debug("Query string {}", cb);
        try (Connection connection = dataSource.getConnection()) {
            java.sql.PreparedStatement s = connection.prepareStatement(c);
            int update = s.executeUpdate();
            return (update > 0);
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean delete(Long id) throws SQLException {
        CriteriaBuilder cb = new CriteriaBuilder();
        cb = cb.delete()
                .from(TABLE_NAME);
        Predicate where = new Predicate();
        Predicate predicate = addEqualsPredicate(where, ID, id);
        cb = cb.where(predicate);

        String c = cb.toString();
        log.debug("Query string {}", cb);
        try (Connection connection = dataSource.getConnection()) {
            java.sql.Statement s = connection.createStatement();
            int update = s.executeUpdate(c);
            return (update > 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Long insert(
            String email,
            String password,
            String groupNumber,
            Boolean isLocal,
            XMLGregorianCalendar birthDate
    )
            throws SQLException {
        Date bDate = birthDate == null ? null : birthDate.toGregorianCalendar().getTime();
        log.debug("Insert new entity: {} {} {} {} {} ", email, password, groupNumber, isLocal, bDate);

        CriteriaBuilder cb = new CriteriaBuilder();
        cb = cb.insert(TABLE_NAME);

        cb.columns(EMAIL, PASSWORD, GROUP_NUMBER, IS_LOCAL, BIRTH_DATE);

        Predicate where = new Predicate();
        where = where.comma(email);
        where = where.comma(password);
        where = where.comma(groupNumber);
        where = where.comma(isLocal.toString());
        if (birthDate != null) {
            where = where.comma(new SimpleDateFormat("yyyy.MM.dd")
                    .format(bDate));
        }

        cb.values(where);

        String c = cb.toString();
        log.debug("Query string {}", cb);
        try (Connection connection = dataSource.getConnection()) {
            java.sql.PreparedStatement s = connection.prepareStatement(
                    c,
                    Statement.RETURN_GENERATED_KEYS);
            int update = s.executeUpdate();
            ResultSet rs = s.getGeneratedKeys();
            if (rs != null && rs.next()) {
                return rs.getLong(1);
            }
        }
        return -1L;
    }
}
