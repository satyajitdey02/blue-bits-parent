package org.bluebits.snowflakes;

public class InvalidSystemClockException extends Exception {
  public InvalidSystemClockException(String message) {
    super(message);
  }
}
