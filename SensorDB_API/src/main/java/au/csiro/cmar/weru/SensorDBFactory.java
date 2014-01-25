/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 24 Jan 2014
 */
package au.csiro.cmar.weru;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A factory for {@link SensorDB} connections.
 * <p>
 * The expectation is that instances of SensorDB that share a
 * host/user are reused.
 * <p>
 * The class of factory can be set bu the command line parameter
 * <code>-Dsensordb.factory=au.csiro.cmar.weru.MySensorDBFactory</code>
 * If not specified, then a {@link DefaultSensorDBFactory} is used.
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
abstract public class SensorDBFactory {
  /** The parameter for a sensordb factory class */
  public static final String FACTORY_PARAMETER = "sensordb.factory";

  /** The factory instance */
  private static SensorDBFactory instance = null;

  /**
   * Get the singleton factory instance.
   * 
   * @return The singleton factory instance
   */
  public static SensorDBFactory instance() {
    if (instance == null) {
      synchronized (SensorDBFactory.class) {
        if (instance == null) {
          String fc = System.getProperty(FACTORY_PARAMETER, DefaultSensorDBFactory.class.getName());
          try {
            Class<?> clazz = SensorDBFactory.class.forName(fc);
            instance = (SensorDBFactory) clazz.newInstance();
          } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            System.err.println("No factory class " + fc + " falling back");
            ex.printStackTrace(System.err);
            instance = new DefaultSensorDBFactory();
          }
        }
      }
    }
    return instance;
  }
  
  /**
   * Clear the factory singleton instance.
   */
  protected static void clear() {
    instance = null;
  }

  /**
   * Create a SensorDB connection.
   * <p>
   * If the user is null, then a non-logged in version of the connection
   * is created, capable of supplying lists of users and measurments.
   * 
   * @param host The host
   * @param user The username
   * @param password The password
   * 
   * @return A sensordb connection
   * 
   * @throws SDBException if unable to create the connection
   */
  abstract public SensorDB create(URL host, String user, String password) throws SDBException; 

  /**
   * The default implementation of a sensordb factory.
   * <p>
   * There is no need to explicitly dispose on a connection.
   * Existing instances (by host/username) are reused via
   * holding them in {@link WeakReference}s which means they will be
   * garbage collected if there are no strong references remaining.
   */
  public static class DefaultSensorDBFactory extends SensorDBFactory {

    /** The current map */
    private Map<URL, Map<String, WeakReference<SensorDB>>> connections;


    /**
     * Construct a default factory.
     */
    public DefaultSensorDBFactory() {
      this.connections = new HashMap<URL, Map<String, WeakReference<SensorDB>>>();
    }

    /**
     * {@inheritDoc}
     * 
      * @see au.csiro.cmar.weru.SensorDBFactory#create(java.net.URL, java.lang.String, java.lang.String)
     */
    @Override
    synchronized public SensorDB create(URL host, String user, String password) throws SDBException {
      Map<String, WeakReference<SensorDB>> users = this.connections.get(host);
      SensorDB sdb = users == null ? null : users.get(user).get();

      if (sdb == null) {
        sdb = user == null ? new SensorDB(host) : new SensorDB(host, user, password);
        if (users == null) {
          users = new HashMap<String, WeakReference<SensorDB>>();
          this.connections.put(host, users);
        }
        users.put(user, new WeakReference<SensorDB>(sdb));
      }
      return sdb;
    }
  }

}
