/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 17 Jan 2014
 */
package au.csiro.cmar.weru;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link SDBMeasurement}
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class SDBStreamTest extends SDBTest {
  private static final String ID1 = "5253811484aed61d63cd18f4";
  private static final String NAME1 = "name1";
  private static final String DESCRIPTION1 = "I am a description";
  private static final String TOKEN1 = "8de459a1";
  private static final URI PICTURE1 = URI.create("http://localhost/picture1");
  private static final URI WEBSITE1 = URI.create("http://localhost/website1");

  private SDBMeasurement measurement;
  private TestContext context;
  
  @Before
  public void setUp() throws Exception {
    this.context = new TestContext();
    this.measurement = SDBMeasurement.load(this.getClass().getResource("measurement1.json"), SDBMeasurement.class, null);
    this.context.add(this.measurement);
  }
  
  /**
   * Test method for {@link au.csiro.cmar.weru.SBDUser#toJson()}.
   */
  @Test
  public void testSerialize1() {
    SDBStream stream = new SDBStream();
    
    stream.setId(this.ID1);
    stream.setName(this.NAME1);
    stream.setToken(this.TOKEN1);
    stream.setDescription(this.DESCRIPTION1);
    stream.setPicture(this.PICTURE1);
    stream.setWebsite(this.WEBSITE1);
    stream.setMeasurement(this.measurement);
    
    assertEquals(this.loadResource("stream1.json"), stream.toJson());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SBDUser#toJson()}.
   */
  @Test
  public void testSerialize2() {
    SDBStream stream = new SDBStream();
    Metadata md = new Metadata(this.NAME1, this.DESCRIPTION1);
    
    stream.setId(this.ID1);
    stream.setName(this.NAME1);
    stream.getMetadata().add(md);
    
    assertEquals(this.loadResource("stream2.json"), stream.toJson());
  }
  
   /**
   * Test method for {@link au.csiro.cmar.weru.SDBStream#load(java.net.URL, Class)}.
   */
  @Test
  public void testDeserialize1() throws Exception {
    SDBStream stream = SDBStream.load(this.getClass().getResource("stream1.json"), SDBStream.class, this.context);
    
    assertEquals(this.ID1, stream.getId());
    assertEquals(this.NAME1, stream.getName());
    assertEquals(this.PICTURE1, stream.getPicture());
    assertEquals(this.WEBSITE1, stream.getWebsite());
    assertEquals(this.DESCRIPTION1, stream.getDescription());
    assertNull(stream.getCreatedAt());
    assertNull(stream.getUpdatedAt());
  }
  
  /**
  * Test method for {@link au.csiro.cmar.weru.SDBStream#load(java.net.URL, Class)}.
  */
 @Test
 public void testDeserialize2() throws Exception {
   SDBStream stream = SDBStream.load(this.getClass().getResource("stream2.json"), SDBStream.class, this.context);
   Metadata md;
   
   assertEquals(this.ID1, stream.getId());
   assertEquals(this.NAME1, stream.getName());
   assertNull(stream.getPicture());
   assertNull(stream.getWebsite());
   assertNull(stream.getDescription());
   assertNull(stream.getCreatedAt());
   assertNull(stream.getUpdatedAt());
   assertEquals(1, stream.getMetadata().size());
   md = stream.getMetadata().get(0);
   assertEquals(this.NAME1, md.getName());
   assertEquals(this.DESCRIPTION1, md.getValue());
 }

}
