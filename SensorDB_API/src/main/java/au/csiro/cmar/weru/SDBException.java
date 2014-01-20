/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 20 Jan 2014
 */
package au.csiro.cmar.weru;

/**
 * An exception causes by the sensor db API.
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class SDBException extends Exception {
  private static final long serialVersionUID = 7009772246165410631L;

  /**
   * Construct an empty exception.
   */
  public SDBException() {
  }

  /**
   * Construct an exception with a detail message.
   * 
   * @param message The detail message
   */
  public SDBException(String message) {
    super(message);
  }

  /**
   * Construct an exception with an underlying cause.
   * 
   * @param cause The cause
   */
  public SDBException(Throwable cause) {
    super(cause);
  }

  /**
   * Construct an exception with a detail message and underlying cause.
   * 
   * @param message The detail message
   * @param cause The cause
   */
  public SDBException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Construct a potentially surpressable exception
   * 
   * @param message The detail message
   * @param cause The cause
   * @param enableSuppression Allow suppression
   * @param writableStackTrace Allow the stack trace to be modified
   */
  public SDBException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
