package org.bluebits.snowflakes;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import static java.lang.System.exit;

public class BasicEntityIdGenerator implements EntityIdGenerator {

  private final long DATA_CENTER_ID_BITS = 10L;
  private final long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);
  private final long TIMESTAMPS_BITS = 41L;

  private final long DATA_CENTER_ID_SHIFT = 64L - DATA_CENTER_ID_BITS;
  private final long TIMESTAMPS_LEFT_SHIFT = 64L - DATA_CENTER_ID_BITS - TIMESTAMPS_BITS;
  private final long SEQUENCE_MAX = 4096;
  private final long TWEPOCH = 1288834974657L;
  private final long DATA_CENTER_ID;

  private volatile long LAST_TIMESTAMPS = -1L;
  private volatile long SEQUENCE = 0L;


  public BasicEntityIdGenerator() throws GetHardwareIdFailedException {
    DATA_CENTER_ID = getDataCenterId();
    if (DATA_CENTER_ID > MAX_DATA_CENTER_ID || DATA_CENTER_ID < 0) {
      throw new GetHardwareIdFailedException("DATA_CENTER_ID > MAX_DATA_CENTER_ID");
    }
  }

  @Override
  public String generateLongId() throws InvalidSystemClockException {
    long timestamp = System.currentTimeMillis();
    if (timestamp < LAST_TIMESTAMPS) {
      throw new InvalidSystemClockException("Clock moved backwards.  Refusing to generate id for " + (
          LAST_TIMESTAMPS - timestamp) + " milliseconds.");
    }
    if (LAST_TIMESTAMPS == timestamp) {
      SEQUENCE = (SEQUENCE + 1) % SEQUENCE_MAX;
      if (SEQUENCE == 0) {
        timestamp = tilNextMillis(LAST_TIMESTAMPS);
      }
    } else {
      SEQUENCE = 0;
    }
    LAST_TIMESTAMPS = timestamp;
    Long id = ((timestamp - TWEPOCH) << TIMESTAMPS_LEFT_SHIFT) | (DATA_CENTER_ID << DATA_CENTER_ID_SHIFT) | SEQUENCE;
    /*A quick hack to avoid negative id value*/
    id = id < 0 ? (-1L * id) : id;
    return id.toString();
  }

  protected long tilNextMillis(long lastTimestamp) {
    long timestamp = System.currentTimeMillis();
    while (timestamp <= lastTimestamp) {
      timestamp = System.currentTimeMillis();
    }
    return timestamp;
  }

  protected long getDataCenterId() throws GetHardwareIdFailedException {
    try {
      byte[] mac = getMacAddress();
      if (mac == null) {
        throw new GetHardwareIdFailedException("MAC address not found.");
      }

      long id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
      return id;
    } catch (SocketException e) {
      throw new GetHardwareIdFailedException(e);
    } catch (UnknownHostException e) {
      throw new GetHardwareIdFailedException(e);
    }
  }

  protected byte[] getMacAddress() throws SocketException, UnknownHostException {
    InetAddress ip = InetAddress.getLocalHost();
    System.out.println("Current IP address : " + ip.getHostAddress());

    Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
    while (networks.hasMoreElements()) {
      NetworkInterface network = networks.nextElement();
      byte[] mac = network.getHardwareAddress();
      if (mac != null) {
        return mac;
      }
    }

    return null;
  }

  public static void main(String[] args) throws GetHardwareIdFailedException, InvalidSystemClockException {
    BasicEntityIdGenerator generator = new BasicEntityIdGenerator();
    int n = Integer.parseInt(args[0]);
    Set<String> ids = new HashSet<String>();
    for (int i = 0; i < n; i++) {
      String id = generator.generateLongId();
      if (ids.contains(id)) {
        System.out.println("Duplicate id:" + id);
        exit(1);
      }
      ids.add(id);
      System.out.println(id);
    }
  }
}
