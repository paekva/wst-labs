package com.paekva.wstlab6;

import com.paekva.wstlab6.database.dto.StudentDTO;
import com.paekva.wstlab6.database.entity.Student;
import com.paekva.wstlab6.util.StudentsResourceIntegration;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Client {
    public static void main(String... args) throws IOException {
        StudentsResourceIntegration studentsResourceIntegration = new StudentsResourceIntegration();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int currentState = 0;

        while (true) {
            try {
                switch (currentState) {
                    case 0:
                        System.out.println("\nВыберите один из пунктов:");
                        System.out.println("1. Вывести всех пользователей");
                        System.out.println("2. Применить фильтры");
                        System.out.println("3. Удалить информацию о студенте");
                        System.out.println("4. Изменить информацию о студенте");
                        System.out.println("5. Добавить информацию о студенте");
                        System.out.println("6. Выйти");
                        currentState = readState(currentState, reader);
                        break;
                    case 1:
                        System.out.println("Найдено:");
                        studentsResourceIntegration.findAll().stream().map(Client::studentToString).forEach(System.out::println);
                        currentState = 0;
                        break;
                    case 2:
                        System.out.println("\nЧтобы не применять фильтр, оставьте значение пустым");
                        System.out.println("id:");
                        Long id = readLong(reader);
                        System.out.println("email:");
                        String email = readString(reader);
                        System.out.println("group number:");
                        String groupNumber = readString(reader);
                        System.out.println("is local:");
                        Boolean isLocal = readBoolean(reader);
                        System.out.println("birthDate(yyyy-mm-dd):");
                        XMLGregorianCalendar birthDate = readDate(reader);
                        System.out.println("Найдено:");
                        studentsResourceIntegration.findWithFilters(id, email, groupNumber, isLocal, birthDate)
                                .stream()
                                .map(Client::studentToString)
                                .forEach(System.out::println);
                        currentState = 0;
                        break;
                    case 3:
                        System.out.print("Введите id пользователя, которого хотите удалить: ");
                        id = readLong(reader);
                        System.out.println(studentsResourceIntegration.delete(id));
                        currentState = 0;
                        break;
                    case 4:
                        System.out.println("id:");
                        id = readLong(reader);
                        System.out.println("\nЧтобы не изменять значение поля, оставьте значение пустым");
                        System.out.println("email:");
                        email = readString(reader);
                        System.out.println("password:");
                        String password = readString(reader);
                        System.out.println("group number:");
                        groupNumber = readString(reader);
                        System.out.println("is local:");
                        isLocal = readBoolean(reader);
                        System.out.println("birthDate(yyyy-mm-dd):");
                        birthDate = readDate(reader);
                        StudentDTO studentDTO = new StudentDTO(email, password, groupNumber, isLocal, birthDate != null ? birthDate.toString() : null);
                        System.out.println(String.format(
                                "Обновлено %s пользователь", studentsResourceIntegration.update(id, studentDTO)
                                )
                        );
                        currentState = 0;
                        break;
                    case 5:
                        System.out.println("email:");
                        email = readString(reader);
                        System.out.println("password:");
                        password = readString(reader);
                        System.out.println("group number:");
                        groupNumber = readString(reader);
                        System.out.println("is local:");
                        isLocal = readBoolean(reader);
                        System.out.println("birthDate(yyyy-mm-dd):");
                        birthDate = readDate(reader);
                        studentDTO = new StudentDTO(email, password, groupNumber, isLocal, birthDate != null ? birthDate.toString() : null);
                        System.out.println(String.format(
                                "Обновлено %s пользователей", studentsResourceIntegration.insert(studentDTO)
                                )
                        );
                        currentState = 0;
                        break;
                    case 6:
                        return;
                    default:
                        currentState = 0;
                        break;
                }
            } catch (Exception ex) {
                System.out.println("Ошибка: " + ex.getMessage());
            }
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
                ", group number='" + student.getGroupNumber() + '\'' +
                ", email='" + student.getEmail() + '\'' +
                ", is local=" + student.getIsLocal() +
                ", birth date=" + student.getBirthDate() +
                '}';
    }
}
