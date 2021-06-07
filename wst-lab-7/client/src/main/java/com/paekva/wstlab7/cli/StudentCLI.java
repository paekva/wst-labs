package com.paekva.wstlab7.cli;

import com.paekva.wstlab7.client.service.SQLException_Exception;
import com.paekva.wstlab7.client.service.Student;
import com.paekva.wstlab7.client.service.StudentsServiceException;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedReader;
import java.io.IOException;

import static com.paekva.wstlab7.Client.*;

public class StudentCLI {
    public static void studentLoop(final BufferedReader reader) throws SQLException_Exception, IOException {
        StudentCommand command;
        int currentState = 0;
        StudentDTO studentDTO;
        Long id;

        while (true) {
            try {
                writeHelp();
                currentState = readState(currentState, reader);
                if (currentState < 0 || currentState > StudentCommand.values().length) {
                    System.out.print(">");
                    continue;
                } else if (currentState == 0) {
                    //printOutHelpMessage();
                    continue;
                }
                command = StudentCommand.values()[currentState - 1];
                switch (command) {
                    case FIND_ALL:
                        studentsServicePort.findAll().stream().map(StudentCLI::studentToString).forEach(System.out::println);
                        break;
                    case FIND_BY_FILTERS:
                        System.out.println("\nЧтобы не применять фильтр, оставьте значение пустым");
                        id = readLong(reader);
                        studentDTO = readStudent(reader);
                        studentsServicePort.findWithFilters(id, studentDTO.getEmail(), studentDTO.getPassword(),
                                studentDTO.getGroupNumber(), studentDTO.getIsLocal(), studentDTO.getBirthDate())
                                .stream().map(StudentCLI::studentToString).forEach(System.out::println);
                        break;
                    case INSERT:
                        studentDTO = readStudent(reader);
                        System.out.println(studentsServicePort.insert(studentDTO.getEmail(), studentDTO.getPassword(),
                                studentDTO.getGroupNumber(), studentDTO.getIsLocal(), studentDTO.getBirthDate()));
                        break;
                    case UPDATE:
                        System.out.println("\nВведите id:");
                        id = readLong(reader);
                        System.out.println("\nЧтобы не изменять значение поля, оставьте значение пустым");
                        studentDTO = readStudent(reader);
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
            } catch (StudentsServiceException e) {
                System.out.println(e.getFaultInfo().getMessage());
                System.out.println("Пожалуйста, попробуйте снова!");
            }
        }
    }

    private static StudentDTO readStudent(BufferedReader reader) throws IOException {
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

    private static String studentToString(Student student) {
        return "Student{" +
                "id=" + student.getId() +
                ", email='" + student.getEmail() + '\'' +
                ", group number='" + student.getGroupNumber() + '\'' +
                ", is local=" + student.isIsLocal() +
                ", birthDate=" + student.getBirthDate() +
                '}';
    }
}
