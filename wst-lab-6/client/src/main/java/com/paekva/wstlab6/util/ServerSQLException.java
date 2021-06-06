package com.paekva.wstlab6.util;

public class ServerSQLException extends Exception {
  public ServerSQLException(int status, String message) {
    super(status + " " + message);
  }
}