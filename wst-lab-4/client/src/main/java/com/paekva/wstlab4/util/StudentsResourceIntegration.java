package com.paekva.wstlab4.util;

import com.paekva.wstlab4.database.entity.Student;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MediaType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

@Slf4j
public class StudentsResourceIntegration {
    private final static String BASE_URL = "http://localhost:8080/students/";

    private final static String FIND_ALL_URL = BASE_URL + "all";

    private final static String FILTER_URL = BASE_URL + "filter";

    public List<Student> findAll() {
        Client client = Client.create();
        WebResource webResource = client.resource(FIND_ALL_URL);
        return getStudents(webResource);
    }

    public List<Student> findWithFilters(
            Long id,
            String email,
            String groupNumber,
            Boolean isLocal,
            XMLGregorianCalendar birthDate
    ) {
        Client client = Client.create();
        WebResource webResource = client.resource(FILTER_URL);
        if (id != null) {
            webResource = webResource.queryParam("id", id + "");
        }
        if (email != null) {
            webResource = webResource.queryParam("email", email);
        }
        if (groupNumber != null) {
            webResource = webResource.queryParam("groupNumber", groupNumber);
        }
        if (isLocal != null) {
            webResource = webResource.queryParam("isLocal", isLocal.toString());
        }
        if (birthDate != null) {
            webResource = webResource.queryParam("birthDate", birthDate.toString());
        }
        return getStudents(webResource);
    }

    private List<Student> getStudents(final WebResource webResource) {
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE)
                .get(ClientResponse.class);
        if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed. HTTP code: " + response.getStatus());
        }
        GenericType<List<Student>> type = new GenericType<List<Student>>() {
        };
        return response.getEntity(type);
    }
}