package com.prem.userservice.exceptions;

public class InvalidUsernameOrPassword extends Throwable {
   public InvalidUsernameOrPassword(String message) {
      super(message);
   }
}
