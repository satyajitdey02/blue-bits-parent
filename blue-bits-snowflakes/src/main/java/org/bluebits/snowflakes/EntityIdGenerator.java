package org.bluebits.snowflakes;

public interface EntityIdGenerator {
  String generateLongId() throws InvalidSystemClockException, GetHardwareIdFailedException;
}
