package com.paekva.wstlab4.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class Student {
    private Long id;

    private String password;

    private String email;

    private String groupNumber;

    private Boolean isLocal = false;

    private Date birthDate;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", group number='" + groupNumber + '\'' +
                ", email='" + email + '\'' +
                ", is local=" + isLocal +
                ", birth date=" + birthDate +
                '}';
    }
}
