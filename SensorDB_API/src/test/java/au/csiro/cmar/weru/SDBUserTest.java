/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 16 Jan 2014
 */
package au.csiro.cmar.weru;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.junit.Test;

/**
 * Test cases for SDBUser
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class SDBUserTest extends SDBTest {
  private static final String ID1 = "5253811484aed61d63cd18f4";
  private static final String NAME1 = "name1";
  private static final String DESCRIPTION1 = "I am a description";
  private static final URI PICTURE1 = URI.create("http://localhost/picture1");
  private static final URI WEBSITE1 = URI.create("http://localhost/website1");

  /**
   * Test method for {@link au.csiro.cmar.weru.SBDUser#toJson()}.
   */
  @Test
  public void testSerialize1() {
    SDBUser user = new SDBUser();
    
    user.setId(this.ID1);
    user.setActive(true);
    user.setName(this.NAME1);
    user.setDescription(this.DESCRIPTION1);
    user.setPicture(this.PICTURE1);
    user.setWebsite(this.WEBSITE1);
    
    assertEquals(this.loadResource("user1.json"), user.toJson());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SBDUser#toJson()}.
   */
  @Test
  public void testSerialize2() {
    SDBUser user = new SDBUser();
    Metadata md = new Metadata(this.NAME1, this.DESCRIPTION1);
    
    user.setId(this.ID1);
    user.setName(this.NAME1);
    user.getMetadata().add(md);
    
    assertEquals(this.loadResource("user2.json"), user.toJson());
  }
  
   /**
   * Test method for {@link au.csiro.cmar.weru.SDBUser#load(java.net.URL, Class)}.
   */
  @Test
  public void testDeserialize1() throws Exception {
    SDBUser user = SDBUser.load(this.getClass().getResource("user1.json"), SDBUser.class, null);
    
    assertEquals(this.ID1, user.getId());
    assertEquals(this.NAME1, user.getName());
    assertEquals(this.PICTURE1, user.getPicture());
    assertEquals(this.WEBSITE1, user.getWebsite());
    assertTrue(user.isActive());
    assertEquals(this.DESCRIPTION1, user.getDescription());
    assertNull(user.getCreatedAt());
    assertNull(user.getUpdatedAt());
  }
  
  /**
  * Test method for {@link au.csiro.cmar.weru.SDBUser#load(java.net.URL, Class)}.
  */
 @Test
 public void testDeserialize2() throws Exception {
   SDBUser user = SDBUser.load(this.getClass().getResource("user2.json"), SDBUser.class, null);
   Metadata md;
   
   assertEquals(this.ID1, user.getId());
   assertEquals(this.NAME1, user.getName());
   assertNull(user.getPicture());
   assertNull(user.getWebsite());
   assertFalse(user.isActive());
   assertNull(user.getDescription());
   assertNull(user.getCreatedAt());
   assertNull(user.getUpdatedAt());
   assertEquals(1, user.getMetadata().size());
   md = user.getMetadata().get(0);
   assertEquals(this.NAME1, md.getName());
   assertEquals(this.DESCRIPTION1, md.getValue());
 }
}
