package ca.jrvs.apps.trading.controller;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

public class ResponseExceptionUtil {

  private static final Logger logger = LoggerFactory.getLogger(ResponseExceptionUtil.class);

  public static ResponseExceptionUtil getResponseStatusException(Exception ex) {
    if (ex instanceof  IllegalArgumentException) {
      logger.debug("Invalid input", ex);
      return new ResponseExceptionUtil(HttpStatus.BAD_REQUEST, ex.getMessage());
    } else {
      logger.error("Internal Error", ex);
      return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error: please "
          + "contact admin");
    }
  }

}
