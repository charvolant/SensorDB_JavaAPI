/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 16 Jan 2014
 */
package au.csiro.cmar.weru;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

/**
 * Test cases for {@link Metadata}
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class MetadataTest extends SDBTest {
  private static final String NAME1 = "name1";
  private static final String VALUE1 = "value1";
  private static final Date UPDATED1 = new Date(10000000000L);
  private static final Date START1 = new Date(10000200000L);
  private static final Date END1 = new Date(10000300000L);
  
  /**
   * Test method for {@link au.csiro.cmar.weru.Metadata#Metadata(java.lang.String, java.lang.String)}.
   */
  @Test
  public void testSerialize1() {
    Metadata md = new Metadata(this.NAME1, this.VALUE1);
    
    md.setUpdatedAt(this.UPDATED1);
    assertEquals(this.loadResource("metadata1.json"), md.toJson());
  }
  
  /**
   * Test method for {@link au.csiro.cmar.weru.Metadata#Metadata(java.lang.String, java.lang.String)}.
   */
  @Test
  public void testSerialize2() {
    Metadata md = new Metadata(this.NAME1, this.VALUE1);
    
    md.setUpdatedAt(this.UPDATED1);
    md.setStart(this.START1);
    md.setEnd(this.END1);
    assertEquals(this.loadResource("metadata2.json"), md.toJson());
  }
 
  /**
   * Test method for {@link au.csiro.cmar.weru.Metadata#Metadata(java.lang.String, java.lang.String)}.
   */
  @Test
  public void testDeserialize1() throws Exception {
    Metadata md = Metadata.load(this.getClass().getResource("metadata1.json"), Metadata.class, null);
    
    assertEquals(this.NAME1, md.getName());
    assertEquals(this.VALUE1, md.getValue());
    assertEquals(this.UPDATED1, md.getUpdatedAt());
    assertNull(md.getUpdatedBy());
  }
  
  /**
   * Test method for {@link au.csiro.cmar.weru.Metadata#Metadata(java.lang.String, java.lang.String)}.
   */
  @Test
  public void testDeserialize2() throws Exception {
    Metadata md = Metadata.load(this.getClass().getResource("metadata2.json"), Metadata.class, null);
    
    assertEquals(this.NAME1, md.getName());
    assertEquals(this.VALUE1, md.getValue());
    assertEquals(this.UPDATED1, md.getUpdatedAt());
    assertEquals(this.START1, md.getStart());
    assertEquals(this.END1, md.getEnd());
    assertNull(md.getUpdatedBy());
  }

}
