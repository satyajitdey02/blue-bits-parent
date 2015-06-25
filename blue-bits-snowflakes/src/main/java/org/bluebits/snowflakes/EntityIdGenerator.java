package org.bluebits.snowflakes;

public interface EntityIdGenerator {
  public long generateLongId() throws InvalidSystemClockException, GetHardwareIdFailedException;
}
