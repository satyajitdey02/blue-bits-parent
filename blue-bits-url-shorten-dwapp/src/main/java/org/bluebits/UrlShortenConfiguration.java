package org.bluebits;

import com.yammer.dropwizard.config.Configuration;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by satyajit on 6/25/15.
 */
public class UrlShortenConfiguration extends Configuration {

  @JsonProperty
  @NotEmpty
  public String mongohost = System.getenv("OPENSHIFT_MONGODB_DB_HOST") == null ? "localhost" : System.getenv("OPENSHIFT_MONGODB_DB_HOST");

  @JsonProperty
  @Min(1)
  @Max(65535)
  public int mongoport = System.getenv("OPENSHIFT_MONGODB_DB_PORT") == null ? 27017 : Integer.parseInt(System.getenv("OPENSHIFT_MONGODB_DB_PORT"));

  @JsonProperty
  @NotEmpty
  public String mongodb = System.getenv("OPENSHIFT_APP_NAME") == null ? "mydb" : System.getenv("OPENSHIFT_APP_NAME");

}