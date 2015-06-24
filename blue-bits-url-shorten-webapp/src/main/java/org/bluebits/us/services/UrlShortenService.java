package org.bluebits.us.services;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.spi.container.ResourceFilters;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bluebits.us.dao.UrlDAO;
import org.bluebits.us.dao.impl.UrlDAOImpl;
import org.bluebits.us.entities.UrlEntity;
import org.bluebits.us.exceptions.UrlShortenException;
import org.bluebits.us.filters.ExtractResourceFilter;
import org.bluebits.us.filters.ShortenResourceFilter;
import org.bluebits.us.utils.CacheUtil;
import org.bluebits.us.utils.MongoDB;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * Created by satyajit on 5/18/15.
 */

@Path("/us")
public class UrlShortenService {
  private static Logger LOGGER = Logger.getLogger(UrlShortenService.class);

  /**
   * @param body
   * @return Response
   * @throws Exception
   */
  @POST
  @Path("/shorten")
  @Produces({"application/json"})
  @ResourceFilters(ShortenResourceFilter.class)
  public Response shortenUrl(String body, @Context HttpContext context) throws UrlShortenException {
    try {
      if (StringUtils.isBlank(body)) {
        throw new UrlShortenException("Invalid request with empty long URL DATA.");
      }

      JSONObject bodyEntity = new JSONObject(body);
      String longUrl = bodyEntity.getString("longUrl");

      if (StringUtils.isBlank(longUrl)) {
        throw new UrlShortenException("Can not shorten empty URL");
      }

      UrlDAO urlDAO = new UrlDAOImpl(MongoDB.getInstance().getDatastore());
      UrlEntity urlEntity = urlDAO.saveUrl(new UrlEntity(longUrl));
      System.out.println(urlEntity);

      String shortUrl = String.format("%sus/%s", context.getRequest().
          getBaseUri().toString(), urlEntity.getBase62Code());
      JSONObject responseEntity = new JSONObject().put("base62Code", urlEntity.getBase62Code()).
          put("shortUrl", shortUrl).put("longUrl", longUrl);

      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(String.format("%s shortened to %s", urlEntity.getUrl(), shortUrl));
      }

      return Response.ok(responseEntity.toString()).build();
    } catch (Exception e) {
      LOGGER.error("Error shortening URL.", e);
      throw new UrlShortenException(String.format("Error shortening URL. Cause: %s", e.getMessage()));
    }
  }

  /**
   * @param base62Code
   * @return Response
   * @throws Exception
   */
  @GET
  @Path("/{base62Code}")
  @Produces(MediaType.APPLICATION_JSON)
  @ResourceFilters(ExtractResourceFilter.class)
  public Response expandUrl(@PathParam("base62Code") String base62Code) throws UrlShortenException {
    if (StringUtils.isBlank(base62Code)) {
      throw new UrlShortenException(String.format("Invalid short URL with id: %s", base62Code));
    }

    try {
      String longUrl = CacheUtil.lookup(base62Code);
      if (StringUtils.isNotBlank(longUrl)) {
        return Response.seeOther(UriBuilder.fromUri(longUrl).build()).build();
      }

      UrlDAOImpl urlDAO = new UrlDAOImpl(MongoDB.getInstance().getDatastore());
      UrlEntity urlEntity = urlDAO.findByBase62Code(base62Code);
      if (urlEntity == null) {
        throw new UrlShortenException(String.format("Short URL with id: %s can not be expanded", base62Code));
      }

      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(String.format("Long URL: %s found for base62Code: %s", urlEntity.getUrl(), base62Code));
      }

      CacheUtil.cache(base62Code, urlEntity.getUrl());
      return Response.seeOther(UriBuilder.fromUri(urlEntity.getUrl()).build()).build();

    } catch (Exception e) {
      LOGGER.error(String.format("No Long URL found with base62Code; %s in Cache/DB", base62Code), e);
      throw new UrlShortenException(e);
    }
  }

  /**
   * @return
   */
  @GET
  @Path("/hello")
  @Produces(MediaType.APPLICATION_JSON)
  public Response sayHello() {
    return Response.ok("{\"message\" : \"Hello! This is URL shortening Service!\"}").build();
  }
}

