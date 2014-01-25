/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 25 Jan 2014
 */
package au.csiro.cmar.weru;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import java.net.URL;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.csiro.cmar.weru.SensorDBFactory.DefaultSensorDBFactory;

/**
 * Test cases for {@link SensorDBFactory}
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class SensorDBFactoryTest extends SDBTest {
  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
    SensorDBFactory.clear();
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDBFactory#instance()}.
   */
  @Test
  public void testInstance1() {
    SensorDBFactory instance = SensorDBFactory.instance();
    
    Assert.assertEquals(DefaultSensorDBFactory.class, instance.getClass());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDBFactory#instance()}.
   */
  @Test
  public void testInstance2() {
    System.setProperty(SensorDBFactory.FACTORY_PARAMETER, DefaultSensorDBFactory.class.getName());
    SensorDBFactory instance = SensorDBFactory.instance();
    
    Assert.assertEquals(DefaultSensorDBFactory.class, instance.getClass());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDBFactory#instance()}.
   */
  @Test
  public void testInstance3() {
    System.setProperty(SensorDBFactory.FACTORY_PARAMETER, TestSensorDBFactory.class.getName());
    SensorDBFactory instance = SensorDBFactory.instance();
    
    Assert.assertEquals(TestSensorDBFactory.class, instance.getClass());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDBFactory#create(java.net.URL, java.lang.String, java.lang.String)}.
   */
  @Test
  public void testCreate1() throws Exception {
    URL host = new URL("http://localhost:" + this.PORT);
    SensorDBFactory instance = SensorDBFactory.instance();
    SensorDB sdb;
    
    this.createServer();
    sdb = instance.create(host, null, null);
    assertEquals(1, sdb.getUsers().size());
    assertEquals(27, sdb.getMeasurements().size());
    assertEquals(0, sdb.getExperiments().size());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDBFactory#create(java.net.URL, java.lang.String, java.lang.String)}.
   */
  @Test
  public void testCreate2() throws Exception {
    URL host = new URL("http://localhost:" + this.PORT);
    SensorDBFactory instance = SensorDBFactory.instance();
    SensorDB sdb;
    
    this.createServer();
    sdb = instance.create(host, this.USER1, this.PASSWORD1);
    assertEquals(1, sdb.getUsers().size());
    assertEquals(27, sdb.getMeasurements().size());
    assertEquals(1, sdb.getExperiments().size());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDBFactory#create(java.net.URL, java.lang.String, java.lang.String)}.
   */
  @Test
  public void testCreate3() throws Exception {
    URL host = new URL("http://localhost:" + this.PORT);
    SensorDBFactory instance = SensorDBFactory.instance();
    SensorDB sdb1, sdb2;
    
    this.createServer();
    sdb1 = instance.create(host, this.USER1, this.PASSWORD1);
    sdb2 = instance.create(host, this.USER1, this.PASSWORD1);
    assertSame(sdb1, sdb2);
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SensorDBFactory#create(java.net.URL, java.lang.String, java.lang.String)}.
   */
  @Test
  public void testCreate4() throws Exception {
    URL host = new URL("http://localhost:" + this.PORT);
    SensorDBFactory instance = SensorDBFactory.instance();
    SensorDB sdb1, sdb2;
    int ss1, ss2;
    
    this.createServer();
    sdb1 = instance.create(host, this.USER1, this.PASSWORD1);
    ss1 = System.identityHashCode(sdb1);
    sdb1 = null;
    System.gc();
    sdb2 = instance.create(host, this.USER1, this.PASSWORD1);
    ss2 = System.identityHashCode(sdb2);
    assertFalse(ss1 == ss2);
  }

  public static class TestSensorDBFactory extends DefaultSensorDBFactory {
  }
}
