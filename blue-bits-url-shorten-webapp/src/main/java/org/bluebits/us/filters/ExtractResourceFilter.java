package org.bluebits.us.filters;

import com.sun.jersey.spi.container.*;

/**
 * Created by satyajit on 5/28/15.
 */
public class ExtractResourceFilter implements ResourceFilter {

  @Override
  public ContainerRequestFilter getRequestFilter() {
    return new ContainerRequestFilter() {
      @Override
      public ContainerRequest filter(ContainerRequest containerRequest) {
        return containerRequest;
      }
    };
  }

  @Override
  public ContainerResponseFilter getResponseFilter() {
    return new ContainerResponseFilter() {
      @Override
      public ContainerResponse filter(ContainerRequest containerRequest, ContainerResponse containerResponse) {
        return containerResponse;
      }
    };
  }
}
