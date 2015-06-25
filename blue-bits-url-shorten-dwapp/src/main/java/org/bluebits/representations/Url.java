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
  @JsonProperty
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @URL
  @JsonProperty
  @NotBlank
  private String url;
  @JsonProperty
  private String base62Code;

  public Url() {
  }

  public Url(String url) {
    this.url = url;
  }

  public Url(String url, String base62Code) {
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
