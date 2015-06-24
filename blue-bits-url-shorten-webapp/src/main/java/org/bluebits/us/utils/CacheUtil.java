package org.bluebits.us.utils;

import org.apache.commons.io.IOUtils;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.jcs.engine.control.CompositeCacheManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by satyajit on 5/28/15.
 */
public class CacheUtil {
  private static final String JCS_CONFIG_FILE = "JcsConfig.properties";
  private static JCS cache;

  static {
    try {
      CompositeCacheManager ccm = CompositeCacheManager.getUnconfiguredInstance();
      ccm.configure(loadConfig());
      cache = JCS.getInstance("urlShorten");
    } catch (CacheException ce) {
      ce.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void cache(String key, String value) throws CacheException {
    cache.put(key, value);
  }

  public static String lookup(String key) {
    return (String) cache.get(key);
  }

  private static Properties loadConfig() throws Exception {
    Properties prop = new Properties();
    InputStream input = null;

    try {
      input = CacheUtil.class.getClassLoader().getResourceAsStream(JCS_CONFIG_FILE);
      prop.load(input);
    } catch (FileNotFoundException fnfe) {
      throw fnfe;
    } catch (IOException ex) {
      throw ex;
    } finally {
      IOUtils.closeQuietly(input);
    }

    return prop;
  }
}
