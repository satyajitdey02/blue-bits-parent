package org.bluebits.us.utils;

/**
 * Created by satyajit on 5/18/15.
 */
public class Base62Codec {
  private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final int BASE = ALPHABET.length();
  private static final char[] CHARSET = ALPHABET.toCharArray();

  /**
   * Encodes a given integer to a Base62Coded String
   *
   * @param number
   * @return String
   */
  public static String encode(int number) {
    int i = number;
    if (i == 0) {
      return Character.toString(CHARSET[0]);
    }

    StringBuilder stringBuilder = new StringBuilder();
    while (i > 0) {
      int remainder = i % BASE;
      i /= BASE;
      stringBuilder.append(CHARSET[remainder]);
    }
    return stringBuilder.reverse().toString();
  }

  /**
   * Decodes a given Base62Coded String to an integer
   *
   * @param s
   * @return int
   */
  public static int decode(String s) {
    int i = 0;
    char[] chars = s.toCharArray();
    for (char c : chars) {
      i = i * BASE + ALPHABET.indexOf(c);
    }
    return i;
  }

  /**
   * Main Method for quick Code Testing
   *
   * @param args
   */
  public static void main(String... args) {
    for (int i = 12345678; i < 1234567890; i = i + 2345678) {
      String encoded = encode(i);
      System.out.println(i + " encoded to: " + encode(i));
      System.out.println(encoded + " decoded to: " + decode(encoded));
    }
  }
}
