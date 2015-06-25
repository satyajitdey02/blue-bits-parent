package org.bluebits.representations;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.mongojack.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * Created by satyajit on 6/24/15.
 */
public class Url {
  @Id
  @NotBlank
  private Long id;

  @URL
  @NotBlank
  private String url;

  @NotBlank
  private String base62Code;

  public Url() {
  }

  public Url(Long id, String url, String base62Code) {
    this.id = id;
    this.url = url;
    this.base62Code = base62Code;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getBase62Code() {
    return base62Code;
  }

  public void setBase62Code(String base62Code) {
    this.base62Code = base62Code;
  }
}
