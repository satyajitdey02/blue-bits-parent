package org.bluebits.resources;

import com.sun.jersey.api.core.HttpContext;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.apache.commons.lang.StringUtils;
import org.bluebits.exceptions.UrlShortenException;
import org.bluebits.representations.Url;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * Created by satyajit on 5/18/15.
 */

@Path("/us")
public class UrlShortenResources {
  private JacksonDBCollection<Url, String> collection;

  public UrlShortenResources(JacksonDBCollection<Url, String> urls) {
    this.collection = urls;
  }

  @POST
  @Path("/shorten")
  @Produces({"application/json"})
  public Response shortenUrl(@Valid Url url, @Context HttpContext context) throws UrlShortenException {
    try {
      collection.insert(url);
      return Response.ok(url.toString()).build();
    } catch (Exception e) {
      throw new UrlShortenException(String.format("Error shortening URL. Cause: %s", e.getMessage()));
    }
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

