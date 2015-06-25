package org.bluebits.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.lang.StringUtils;
import org.bluebits.codec.Base62Codec;
import org.bluebits.exceptions.UrlShortenException;
import org.bluebits.representations.Url;
import org.bluebits.snowflakes.BasicEntityIdGenerator;
import org.bluebits.snowflakes.EntityIdGenerator;
import org.bluebits.snowflakes.GetHardwareIdFailedException;
import org.bluebits.snowflakes.InvalidSystemClockException;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * Created by satyajit on 5/18/15.
 */

@Path("/api/v1/us")
public class UrlShortenResources {
  private JacksonDBCollection<Url, String> collection;

  public UrlShortenResources(JacksonDBCollection<Url, String> urls) {
    this.collection = urls;
  }

  @POST
  @Path("/shorten")
  @Produces({"application/json"})
  public Response shortenUrl(String body, @Context HttpContext context) throws UrlShortenException {
    if (StringUtils.isBlank(body)) {
      throw new UrlShortenException("Long Url couldn't be empty/null");
    }

    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonNode = mapper.readTree(body);
      String longUrl = jsonNode.findValue("longUrl").asText();
      Url url = collection.findOne(DBQuery.is("longUrl", longUrl.trim()));
      if(url != null) {
        return Response.ok(url.toString()).build();
      }

      EntityIdGenerator entityIdGenerator = new BasicEntityIdGenerator();
      Long id = getEntityId(entityIdGenerator);

      String base62Code = Base62Codec.encode(id);
      url = new Url(id, longUrl, base62Code);
      collection.insert(url);

      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String responseJson = ow.writeValueAsString(url);

      return Response.ok(responseJson).build();
    } catch (Exception e) {
      throw new UrlShortenException(String.format("Error shortening URL. Cause: %s", e.getMessage()), e);
    }
  }

  private Long getEntityId(EntityIdGenerator entityIdGenerator) throws InvalidSystemClockException, GetHardwareIdFailedException {
    Long id = entityIdGenerator.generateLongId();
    Url url = collection.findOne(DBQuery.is("_id", id));
    if(url != null) {
      return getEntityId(entityIdGenerator);
    }

    return id;
  }

  @GET
  @Path("/{base62Code}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response expandUrl(@PathParam("base62Code") String base62Code) throws UrlShortenException {
    if (StringUtils.isBlank(base62Code)) {
      throw new UrlShortenException(String.format("Invalid short URL with id: %s", base62Code));
    }

    try {
      Url url = collection.findOne(DBQuery.is("base62Code", base62Code));
      return Response.seeOther(UriBuilder.fromUri(url.getUrl()).build()).build();
    } catch (Exception e) {
      throw new UrlShortenException(e);
    }
  }
}

