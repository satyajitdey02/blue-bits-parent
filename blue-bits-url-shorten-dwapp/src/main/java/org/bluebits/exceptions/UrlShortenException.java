package org.bluebits.exceptions;

/**
 * Created by satyajit on 5/18/15.
 */
public class UrlShortenException extends Exception{
  public UrlShortenException() {

  }

  public UrlShortenException(String message) {
    super(message);
  }

  public UrlShortenException(Throwable cause) {
    super(cause);
  }

  public UrlShortenException(String message, Throwable cause) {
    super(message, cause);
  }
}
