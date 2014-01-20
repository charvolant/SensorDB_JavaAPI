/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 16 Jan 2014
 */
package au.csiro.cmar.weru.json;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

/**
 * Test cases for {@link SDBDateSerializer}
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class SDBDateSerializerTest {

  /**
   * Test method for {@link au.csiro.cmar.weru.json.SDBDateSerializer#serialize(java.util.Date, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)}.
   */
  @Test
  public void testSerialize1() {
    TestBean bean = new TestBean();
    
    bean.date = new Date(1000000000L);
    assertEquals("{\n  \"date\" : 1000000\n}", bean.toJson());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.json.SDBDateSerializer#serialize(java.util.Date, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)}.
   */
  @Test
  public void testSerialize2() {
    TestBean bean = new TestBean();
    
    assertEquals("{ }", bean.toJson());
  }

}
