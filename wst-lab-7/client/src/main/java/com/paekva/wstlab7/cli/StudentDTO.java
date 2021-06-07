package com.paekva.wstlab7.cli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.datatype.XMLGregorianCalendar;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private String email;
    private String password;
    private String groupNumber;
    private Boolean isLocal;
    private XMLGregorianCalendar birthDate;
}