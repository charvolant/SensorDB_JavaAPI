/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 14 Jan 2014
 */
package au.csiro.cmar.weru;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link SensorDB}
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class SensorDBTest extends SDBTest {

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    this.createServer();
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDB#SensorDB(java.lang.String)}.
   */
  @Test
  public void testSensorDBString() throws Exception {
    SensorDB sdb = new SensorDB(new URL("http://localhost:" + this.PORT));

    assertNotNull(sdb.getUser("fieldprime_test"));
    assertNotNull(sdb.getMeasurement("Celsius"));
    assertNotNull(sdb.getMeasurement("Practical Salinity Unit (PSU)"));
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDB#SensorDB(java.lang.String, java.lang.String, java.lang.String)}.
   */
  @Test
  public void testSensorDBStringStringString() throws Exception {
    SensorDB sdb = new SensorDB(new URL("http://localhost:" + this.PORT), "fieldprime_test", "fieldprime_test");

    assertNotNull(sdb.getUser("fieldprime_test"));
    assertNotNull(sdb.getMeasurement("Celsius"));
    assertNotNull(sdb.getMeasurement("Practical Salinity Unit (PSU)"));
    assertNotNull(sdb.getExperiment("Test1"));
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDB#logout()}.
   */
  @Test
  public void testLogout() throws Exception {
    SensorDB sdb = new SensorDB(new URL("http://localhost:" + this.PORT), "fieldprime_test", "fieldprime_test");

    sdb.logout();
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDB#createUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
   */
  @Test
  public void testCreateUser() {
    //fail("Not yet implemented");
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDB#deleteUser(java.lang.String, java.lang.String)}.
   */
  @Test
  public void testDeleteUser() {
    //fail("Not yet implemented");
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDB#getUserId(java.lang.String)}.
   */
  @Test
  public void testGetUser() throws Exception {
    SensorDB sdb = new SensorDB(new URL("http://localhost:" + this.PORT), "fieldprime_test", "fieldprime_test");
    SDBUser user = sdb.getUser(this.USER1);
    
    assertNotNull(user);
    assertEquals("5253811484aed61d63cd18f4", user.getId());
    assertNull(sdb.getUser("AnyOldIron"));
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDB#createMeasurement(java.lang.String, java.lang.String, java.lang.String)}.
   */
  @Test
  public void testCreateMeasurement() throws Exception {
    SensorDB sdb = new SensorDB(new URL("http://localhost:" + this.PORT), "fieldprime_test", "fieldprime_test");

    try {
      sdb.createMeasurement("Fee", "Fie", "Foe");
      fail("Expecting SBDException");
    } catch (SDBException ex) {
    }
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDB#deleteMeasurement(java.lang.String)}.
   */
  @Test
  public void testDeleteMeasurement() throws Exception {
    SensorDB sdb = new SensorDB(new URL("http://localhost:" + this.PORT), "fieldprime_test", "fieldprime_test");

    try {
      sdb.deleteMeasurement("Celsius");
      fail("Expecting SBDException");
    } catch (SDBException ex) {
    }
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDB#getMeasurementId(java.lang.String)}.
   */
  @Test
  public void testGetMeasurement() throws Exception {
    SensorDB sdb = new SensorDB(new URL("http://localhost:" + this.PORT), "fieldprime_test", "fieldprime_test");
    SDBMeasurement measurement = sdb.getMeasurement("Celsius");
    
    assertNotNull(measurement);
   assertEquals("5245285674c53d6c5b66d942", measurement.getId());
   assertNull(sdb.getMeasurement("AnyOldIron"));
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDB#createExperiment(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)}.
   */
  @Test
  public void testCreateExperiment() {
    //fail("Not yet implemented");
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDB#deleteExperiment(java.lang.String)}.
   */
  @Test
  public void testDeleteExperiment() {
    //fail("Not yet implemented");
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDB#getExperimentId(java.lang.String)}.
   */
  @Test
  public void testGetExperiment() throws Exception {
    SensorDB sdb = new SensorDB(new URL("http://localhost:" + this.PORT), "fieldprime_test", "fieldprime_test");
    SDBExperiment experiment = sdb.getExperiment(this.EXPERIMENT1);
    
    assertNotNull(experiment);
    assertEquals("529fc65f84ae219229313b26", experiment.getId());
    assertNull(sdb.getExperiment("AnyOldIron"));
  }
}
