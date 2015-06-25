package org.bluebits.snowflakes;

public class GetHardwareIdFailedException extends Exception {
  GetHardwareIdFailedException(String reason) {
    super(reason);
  }

  GetHardwareIdFailedException(Exception e) {
    super(e);
  }
}
