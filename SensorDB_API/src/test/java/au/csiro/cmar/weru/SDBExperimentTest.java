/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 17 Jan 2014
 */
package au.csiro.cmar.weru;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.net.URI;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link SDBMeasurement}
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class SDBExperimentTest extends SDBTest {
  private static final String ID1 = "529fc66884ae219229313b2a";
  private static final String NAME1 = "name1";
  private static final String DESCRIPTION1 = "I am a description";
  private static final URI PICTURE1 = URI.create("http://localhost/picture1");
  private static final URI WEBSITE1 = URI.create("http://localhost/website1");
  private static final Date CREATED1 = new Date(1386202722077000L);
  private static final Date UPDATED1 = new Date(1386202722078000L);
  private static final TimeZone TIMEZONE1 = TimeZone.getTimeZone("GMT+10");

  private SDBMeasurement measurement;
  private SDBUser user;
  private TestContext context;
  
  @Before
  public void setUp() throws Exception {
    this.context = new TestContext();
    this.measurement = SDBMeasurement.load(this.getClass().getResource("measurement1.json"), SDBMeasurement.class, null);
    this.user = SDBUser.load(this.getClass().getResource("user1.json"), SDBUser.class, null);
    this.context.add(this.measurement);
    this.context.add(this.user);
  }
  
  /**
   * Test method for {@link au.csiro.cmar.weru.SBDUser#toJson()}.
   */
  @Test
  public void testSerialize1() {
    SDBExperiment experiment = new SDBExperiment();
    
    experiment.setId(this.ID1);
    experiment.setName(this.NAME1);
    experiment.setDescription(this.DESCRIPTION1);
    experiment.setPicture(this.PICTURE1);
    experiment.setWebsite(this.WEBSITE1);
    experiment.setCreatedAt(this.CREATED1);
    experiment.setUpdatedAt(this.UPDATED1);
    experiment.setAccessRestriction(1);
    experiment.setTimezone(this.TIMEZONE1);
    assertEquals(this.loadResource("experiment1.json"), experiment.toJson());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SBDUser#toJson()}.
   */
  @Test
  public void testSerialize2() {
    SDBExperiment experiment = new SDBExperiment();
    Metadata md = new Metadata(this.NAME1, this.DESCRIPTION1);
    
    experiment.setId(this.ID1);
    experiment.setName(this.NAME1);
    experiment.getMetadata().add(md);
    experiment.setUser(this.user);
    assertEquals(this.loadResource("experiment2.json"), experiment.toJson());
  }
  
   /**
   * Test method for {@link au.csiro.cmar.weru.SDBExperiment#load(java.net.URL, Class)}.
   */
  @Test
  public void testDeserialize1() throws Exception {
    SDBExperiment experiment = SDBExperiment.load(this.getClass().getResource("experiment1.json"), SDBExperiment.class, this.context);
    
    assertEquals(this.ID1, experiment.getId());
    assertEquals(this.NAME1, experiment.getName());
    assertEquals(this.PICTURE1, experiment.getPicture());
    assertEquals(this.WEBSITE1, experiment.getWebsite());
    assertEquals(this.DESCRIPTION1, experiment.getDescription());
    assertEquals(this.CREATED1, experiment.getCreatedAt());
    assertEquals(this.UPDATED1, experiment.getUpdatedAt());
    assertEquals(1, experiment.getAccessRestriction());
    assertEquals(this.TIMEZONE1, experiment.getTimezone());
  }
  
  /**
  * Test method for {@link au.csiro.cmar.weru.SDBExperiment#load(java.net.URL, Class)}.
  */
 @Test
 public void testDeserialize2() throws Exception {
   SDBExperiment experiment = SDBExperiment.load(this.getClass().getResource("experiment2.json"), SDBExperiment.class, this.context);
   Metadata md;
   
   assertEquals(this.ID1, experiment.getId());
   assertEquals(this.NAME1, experiment.getName());
   assertEquals(1, experiment.getMetadata().size());
   md = experiment.getMetadata().get(0);
   assertEquals(this.NAME1, md.getName());
   assertEquals(this.DESCRIPTION1, md.getValue());
   assertSame(this.user, experiment.getUser());
 }
}
