package org.bluebits.us.exceptions;

/**
 * Created by satyajit on 5/18/15.
 */
public class UrlShortenDAOException extends Exception{
  public UrlShortenDAOException() {

  }

  public UrlShortenDAOException(String message) {
    super(message);
  }

  public UrlShortenDAOException(Throwable cause) {
    super(cause);
  }

  public UrlShortenDAOException(String message, Throwable cause) {
    super(message, cause);
  }
}
