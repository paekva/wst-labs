package com.paekva.wstlab6.util;

public class ServerBadRequestException extends Exception {
  public ServerBadRequestException(int status, String message) {
    super(status + " " + message);
  }
}