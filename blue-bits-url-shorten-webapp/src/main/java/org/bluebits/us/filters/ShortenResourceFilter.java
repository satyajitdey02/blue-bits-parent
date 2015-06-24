package org.bluebits.us.filters;

import com.sun.jersey.spi.container.*;
import org.apache.commons.lang.StringUtils;
import org.apache.jcs.access.exception.CacheException;
import org.bluebits.us.utils.CacheUtil;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.core.Response;

/**
 * Created by satyajit on 5/28/15.
 */

public class ShortenResourceFilter implements ResourceFilter{
  @Override
  public ContainerRequestFilter getRequestFilter() {
    return new ContainerRequestFilter() {
      @Override
      public ContainerRequest filter(ContainerRequest request) {
        return request;
      }
    };
  }

  @Override
  public ContainerResponseFilter getResponseFilter() {
    return new ContainerResponseFilter() {
      @Override
      public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        if(response.getStatus() == 200) {
          try {
            JSONObject entity = new JSONObject((String)response.getEntity());
            String base62Code = entity.getString("base62Code");
            String longUrl = entity.getString("longUrl");
            if(StringUtils.isBlank(CacheUtil.lookup(base62Code))) {
              CacheUtil.cache(base62Code, longUrl);
            }

            response.setResponse(Response.ok(new JSONObject().put("shortUrl",
                entity.getString("shortUrl")).toString()).type("application/json").build());
          } catch (JSONException je) {
            je.printStackTrace();
          } catch (CacheException ce) {
            ce.printStackTrace();
          }
        }

        return response;
      }
    };
  }
}
