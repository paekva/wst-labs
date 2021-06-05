package com.paekva.wstlab4.database.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class StudentDTO implements Serializable {
    private String email;

    private String password;

    private String groupNumber;

    private Boolean isLocal = false;

    private String birthDate;
}
