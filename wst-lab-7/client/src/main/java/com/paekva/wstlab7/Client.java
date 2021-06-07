package com.paekva.wstlab7;

import com.paekva.wstlab7.cli.*;
import com.paekva.wstlab7.client.service.*;
import com.paekva.wstlab7.juddi.JUDDIClient;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Exception;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Client {

    public static JUDDIClient juddiClient;

    public static StudentsService studentsServicePort = null;

    public static CommandMode commandMode = null;

    public static void main(String... args) throws SQLException_Exception, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        constructJuddi(reader);
        commandMode = CommandMode.JUDDI;
        JuddiCLI.juddiLoop(reader);
    }

    private static void constructJuddi(BufferedReader reader) throws IOException {
        System.out.println("Enter JUDDI username (/tmp/juddi-distro-3.3.7/juddi-tomcat-3.3.7/conf/tomcat-users.xml), typically 'uddiadmin'");
        String username = reader.readLine().trim();
        System.out.println("Enter JUDDI user password (typically 'da_password1')");
        String password = reader.readLine().trim();
        juddiClient = new JUDDIClient("META-INF/uddi.xml");
        juddiClient.authenticate(username, password);
    }

    public static void writeHelp() {
        System.out.println("\nВыберите один из пунктов:");
        System.out.println("0. Вывести help");
        if (commandMode.equals(CommandMode.SERVICE)) {
            for (StudentCommand value : StudentCommand.values()) {
                System.out.println(1 + value.ordinal() + ". " + value.getHelp());
            }
        } else if (commandMode.equals(CommandMode.JUDDI)) {
            for (JUDDICommand value : JUDDICommand.values()) {
                System.out.println(1 + value.ordinal() + ". " + value.getHelp());
            }
        }
    }


    public static String readString(BufferedReader reader) throws IOException {
        String trim = reader.readLine().trim();
        if (trim.isEmpty()) {
            return null;
        }
        return trim;
    }

    public static XMLGregorianCalendar readDate(BufferedReader reader) {
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

    public static Long readLong(BufferedReader reader) {
        try {
            return Long.parseLong(reader.readLine());
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean readBoolean(BufferedReader reader) {
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

    public static int readState(int current, BufferedReader reader) {
        try {
            return Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            return current;
        }
    }
}
