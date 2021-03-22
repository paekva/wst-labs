package com.paekva.wstlab3.standalone;

import com.paekva.wstlab3.database.StudentDAO;
import com.paekva.wstlab3.service.StudentsService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import javax.sql.DataSource;
import javax.xml.ws.Endpoint;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class App {
  public static void main(String... args) {
    String url = "http://0.0.0.0:8080/students";

    DataSource dataSource = initDataSource();

    Endpoint.publish(url, new StudentsService(new StudentDAO(dataSource)));
    log.info("Application started");
  }

  @SneakyThrows
  private static DataSource initDataSource() {
    InputStream dsPropsStream = App.class.getClassLoader()
        .getResourceAsStream("application.properties");
    Properties dsProps = new Properties();
    dsProps.load(dsPropsStream);
    HikariConfig hikariConfig = new HikariConfig(dsProps);
    return new HikariDataSource(hikariConfig);
  }
}
