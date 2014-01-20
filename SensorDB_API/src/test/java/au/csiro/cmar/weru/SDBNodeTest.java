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

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link SDBMeasurement}
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class SDBNodeTest extends SDBTest {
  private static final String ID1 = "529fc66884ae219229313b2a";
  private static final String NAME1 = "name1";
  private static final String DESCRIPTION1 = "I am a description";
  private static final double LAT1 = -38.75;
  private static final double LONG1 = 148.77;
  private static final double ALT1 = 100.0;
  private static final URI PICTURE1 = URI.create("http://localhost/picture1");
  private static final URI WEBSITE1 = URI.create("http://localhost/website1");
  private static final Date CREATED1 = new Date(1386202722077000L);
  private static final Date UPDATED1 = new Date(1386202722078000L);

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
    SDBNode node = new SDBNode();
    
    node.setId(this.ID1);
    node.setName(this.NAME1);
    node.setAlt(this.ALT1);
    node.setLon(this.LONG1);
    node.setLat(this.LAT1);
    node.setDescription(this.DESCRIPTION1);
    node.setPicture(this.PICTURE1);
    node.setWebsite(this.WEBSITE1);
    node.setCreatedAt(this.CREATED1);
    node.setUpdatedAt(this.UPDATED1);
    
    assertEquals(this.loadResource("node1.json"), node.toJson());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SBDUser#toJson()}.
   */
  @Test
  public void testSerialize2() {
    SDBNode node = new SDBNode();
    Metadata md = new Metadata(this.NAME1, this.DESCRIPTION1);
    
    node.setId(this.ID1);
    node.setName(this.NAME1);
    node.getMetadata().add(md);
    node.setUser(this.user);
    
    assertEquals(this.loadResource("node2.json"), node.toJson());
  }
  
   /**
   * Test method for {@link au.csiro.cmar.weru.SDBNode#load(java.net.URL, Class)}.
   */
  @Test
  public void testDeserialize1() throws Exception {
    SDBNode node = SDBNode.load(this.getClass().getResource("node1.json"), SDBNode.class, this.context);
    
    assertEquals(this.ID1, node.getId());
    assertEquals(this.NAME1, node.getName());
    assertEquals(this.PICTURE1, node.getPicture());
    assertEquals(this.WEBSITE1, node.getWebsite());
    assertEquals(this.DESCRIPTION1, node.getDescription());
    assertEquals(this.LAT1, node.getLat(), 0.000001);
    assertEquals(this.LONG1, node.getLon(), 0.000001);
    assertEquals(this.ALT1, node.getAlt(), 0.00001);
    assertEquals(this.CREATED1, node.getCreatedAt());
    assertEquals(this.UPDATED1, node.getUpdatedAt());
  }
  
  /**
  * Test method for {@link au.csiro.cmar.weru.SDBNode#load(java.net.URL, Class)}.
  */
 @Test
 public void testDeserialize2() throws Exception {
   SDBNode node = SDBNode.load(this.getClass().getResource("node2.json"), SDBNode.class, this.context);
   Metadata md;
   
   assertEquals(this.ID1, node.getId());
   assertEquals(this.NAME1, node.getName());
   assertEquals(1, node.getMetadata().size());
   md = node.getMetadata().get(0);
   assertEquals(this.NAME1, md.getName());
   assertEquals(this.DESCRIPTION1, md.getValue());
   assertSame(this.user, node.getUser());
 }
  
  /**
  * Test method for {@link au.csiro.cmar.weru.SDBNode#load(java.net.URL, Class)}.
  */
 @Test
 public void testDeserialize3() throws Exception {
   SDBNode node = SDBNode.load(this.getClass().getResource("node3.json"), SDBNode.class, this.context);
   
   assertEquals(this.ID1, node.getId());
   assertEquals(this.NAME1, node.getName());
   assertEquals(this.LAT1, node.getLat(), 0.000001);
   assertEquals(this.LONG1, node.getLon(), 0.000001);
   assertEquals(this.ALT1, node.getAlt(), 0.00001);
 }

}
