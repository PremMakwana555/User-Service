package com.prem.userservice.exceptions;

public class InvalidUsernameOrPassword extends RuntimeException {
   public InvalidUsernameOrPassword(String message) {
      super(message);
   }
}
