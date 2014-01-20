/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 19 Jan 2014
 */
package au.csiro.cmar.weru;

/**
 * Contextual information for sensordb objects.
 * <p>
 * The context provides a way of injecting exernal information
 * into a sensordb configuration.
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public interface SDBContext {

  /**
   * Find an existing id -> sensordb object binding.
   * 
   * @param id The identifier for the object
   * 
   * @return A matching bound obejct that sensordb already knows about or null for not found
   */
  public SDBObject findBinding(Object id);
}
