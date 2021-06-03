package com.paekva.wstlab3;

import com.paekva.wstlab3.client.service.Student;
import com.paekva.wstlab3.client.service.Students;
import com.paekva.wstlab3.client.service.StudentsService;
import com.sun.xml.internal.ws.fault.ServerSOAPFaultException;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

class ServerThrottlingTest {
    @SneakyThrows
    public static void main(String... args) {
        URL url = new URL("http://localhost:8080/students?wsdl");
        Students studentsService = new Students(url);
        StudentsService studentsServicePort = studentsService.getStudentsServicePort();

        int n = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(n / 2);
        CountDownLatch latch = new CountDownLatch(n);
        Function<Integer, Runnable> threadBuilder = id -> () -> {
            try {
                List<Student> students = studentsServicePort.findAll();
                System.out.println(id + " ok, " + students.size() + " students in db");
            } catch (ServerSOAPFaultException ex) {
                System.out.println(id + " exception: " + ex.getMessage());
            } catch (Exception e) {
                System.out.println(id + " unexpected exception: " + e.getMessage());
            }
        };
        for (int i = 0; i < n; i++) {
            executorService.submit(threadBuilder.apply(i));
            latch.countDown();
        }
        latch.await();
        executorService.shutdown();
    }
}