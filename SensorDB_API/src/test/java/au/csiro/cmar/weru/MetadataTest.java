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
  
  /**
   * Test method for {@link au.csiro.cmar.weru.Metadata#Metadata(java.lang.String, java.lang.String)}.
   */
  @Test
  public void testSerialize1() {
    Metadata md = new Metadata(this.NAME1, this.VALUE1);
    
    md.setUpdatedAt(new Date(1000000000L));
    assertEquals(this.loadResource("metadata1.json"), md.toJson());
  }
  /**
   * Test method for {@link au.csiro.cmar.weru.Metadata#Metadata(java.lang.String, java.lang.String)}.
   */
  @Test
  public void testDeserialize1() throws Exception {
    Metadata md = Metadata.load(this.getClass().getResource("metadata1.json"), Metadata.class, null);
    
    assertEquals(this.NAME1, md.getName());
    assertEquals(this.VALUE1, md.getValue());
    assertEquals(new Date(1000000000L), md.getUpdatedAt());
    assertNull(md.getUpdatedBy());
  }

}
