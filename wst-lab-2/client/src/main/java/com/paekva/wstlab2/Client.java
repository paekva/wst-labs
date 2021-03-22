package com.paekva.wstlab2;

import com.paekva.wstlab2.client.service.SQLException_Exception;
import com.paekva.wstlab2.client.service.Student;
import com.paekva.wstlab2.client.service.Students;
import com.paekva.wstlab2.client.service.StudentsService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Client {
    public static void main(String... args) throws SQLException_Exception, IOException {
        URL url = new URL("http://localhost:8080/students?wsdl");
        Students studentsService = new Students(url);
        StudentsService studentsServicePort = studentsService.getStudentsServicePort();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int currentState = 0;
        ConsoleOption command;
        StudentDTO studentDTO;
        Long id;


        while (true) {
            printOutHelpMessage();
            currentState = readState(currentState, reader);
            if (currentState < 0 || currentState > ConsoleOption.values().length) {
                System.out.print(">");
                continue;
            } else if (currentState == 0) {
                //printOutHelpMessage();
                continue;
            }
            command = ConsoleOption.values()[currentState - 1];
            switch (command) {
                case FIND_ALL:
                    studentsServicePort.findAll().stream().map(Client::studentToString).forEach(System.out::println);
                    break;
                case FIND_BY_FILTERS:
                    System.out.println("\nЧтобы не применять фильтр, оставьте значение пустым");
                    id = readLong(reader);
                    studentDTO = readUser(reader);
                    studentsServicePort.findWithFilters(id, studentDTO.getEmail(), studentDTO.getPassword(),
                            studentDTO.getGroupNumber(), studentDTO.getIsLocal(), studentDTO.getBirthDate())
                            .stream().map(Client::studentToString).forEach(System.out::println);
                    break;
                case INSERT:
                    studentDTO = readUser(reader);
                    System.out.println(studentsServicePort.insert(studentDTO.getEmail(), studentDTO.getPassword(),
                            studentDTO.getGroupNumber(), studentDTO.getIsLocal(), studentDTO.getBirthDate()));
                    break;
                case UPDATE:
                    System.out.println("\nВведите id:");
                    id = readLong(reader);
                    System.out.println("\nЧтобы не изменять значение поля, оставьте значение пустым");
                    studentDTO = readUser(reader);
                    System.out.println(studentsServicePort.update(id, studentDTO.getEmail(), studentDTO.getPassword(),
                            studentDTO.getGroupNumber(), studentDTO.getIsLocal(), studentDTO.getBirthDate()));
                    break;
                case DELETE:
                    System.out.println("\nВведите id:");
                    id = readLong(reader);
                    System.out.println(studentsServicePort.delete(id));
                    break;
                case QUIT:
                    return;
            }
        }
    }

    private static StudentDTO readUser(BufferedReader reader) throws IOException {
        System.out.println("email:");
        String email = readString(reader);
        System.out.println("password:");
        String password = readString(reader);
        System.out.println("group number:");
        String groupNumber = readString(reader);
        System.out.println("is local:");
        Boolean isLocal = readBoolean(reader);
        System.out.println("birthDate(yyyy-mm-dd):");
        XMLGregorianCalendar birthDate = readDate(reader);
        return new StudentDTO(email, password, groupNumber, isLocal, birthDate);
    }

    private static void printOutHelpMessage() {
        System.out.println("\nВыберите один из пунктов:");
        System.out.println("0. Вывести help");
        for (ConsoleOption value : ConsoleOption.values()) {
            System.out.println(1 + value.ordinal() + ". " + value.getHelp());
        }
    }

    private static String readString(BufferedReader reader) throws IOException {
        String trim = reader.readLine().trim();
        if (trim.isEmpty()) {
            return null;
        }
        return trim;
    }

    private static XMLGregorianCalendar readDate(BufferedReader reader) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date rd = sdf.parse(reader.readLine());

            GregorianCalendar c = new GregorianCalendar();

            if (rd != null) {
                c.setTime(rd);
                XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance()
                        .newXMLGregorianCalendar(c);
                xmlGregorianCalendar.setTimezone(0);
                return xmlGregorianCalendar;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static Long readLong(BufferedReader reader) {
        try {
            return Long.parseLong(reader.readLine());
        } catch (Exception e) {
            return null;
        }
    }

    private static Boolean readBoolean(BufferedReader reader) {
        try {
            String s = reader.readLine();
            if (s.equals("")) {
                return null;
            }
            return Boolean.parseBoolean(s);
        } catch (Exception e) {
            return null;
        }
    }

    private static int readState(int current, BufferedReader reader) {
        try {
            return Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            return current;
        }
    }

    private static String studentToString(Student student) {
        return "Student{" +
                "id=" + student.getId() +
                ", email='" + student.getEmail() + '\'' +
                ", group number='" + student.getGroupNumber() + '\'' +
                ", is local=" + student.isIsLocal() +
                ", birthDate=" + student.getBirthDate() +
                '}';
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class StudentDTO {
        private String email;
        private String password;
        private String groupNumber;
        private Boolean isLocal;
        private XMLGregorianCalendar birthDate;

    }
}
