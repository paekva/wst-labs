package com.paekva.wstlab1.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  private Long id;

  private String password;

  private String email;

  private String groupNumber;

  private Boolean isLocal = false;

  private Date birthDate;

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", group number='" + groupNumber + '\'' +
        ", email='" + email + '\'' +
        ", is local=" + isLocal +
        ", birth date=" + birthDate +
        '}';
  }
}
