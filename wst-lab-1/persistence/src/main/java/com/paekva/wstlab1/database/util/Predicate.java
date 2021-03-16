package com.paekva.wstlab1.database.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Predicate {
  StringBuilder builder;

  public Predicate and(String where) {
    if (builder == null) {
      builder = new StringBuilder(where);
    } else {
      builder.append(" AND ").append(where);
    }
    return this;
  }

  @Override
  public String toString() {
    return builder.toString();
  }
}
