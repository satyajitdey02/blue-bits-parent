package org.bluebits.us.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Created by satyajit on 5/18/15.
 */
public class ShortUrlBuilder {
  private static final Logger LOGGER = Logger.getLogger(ShortUrlBuilder.class);
  private static final String SHORT_URL_CONTEXT_PATH = "http://127.0.0.1:8080/urlshorten/api/us/";

  public static String getShortenUrl(String base62Code) throws Exception {
    if (StringUtils.isBlank(base62Code)) {
      throw new Exception("base62Code can not be null.");
    }

    return String.format("%s%s", SHORT_URL_CONTEXT_PATH, base62Code);
  }
}
