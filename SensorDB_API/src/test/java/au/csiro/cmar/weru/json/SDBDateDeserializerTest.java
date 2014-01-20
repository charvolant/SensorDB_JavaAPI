/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 16 Jan 2014
 */
package au.csiro.cmar.weru.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Test cases for {@link SDBDateDeserializer}
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class SDBDateDeserializerTest {

  /**
   * Test method for {@link au.csiro.cmar.weru.json.SDBDateDeserializer#deserialize(java.util.Date, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)}.
   */
  @Test
  public void testDeserialize1() throws Exception {
    TestBean bean = TestBean.load(this.getClass().getResource("testbean1.json"), TestBean.class, null);

    assertEquals(1000000000L, bean.date.getTime());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.json.SDBDateDeserializer#deserialize}.
   */
  @Test
  public void testDeserialize2() throws Exception {
    TestBean bean = TestBean.load(this.getClass().getResource("testbean2.json"), TestBean.class, null);

    assertNull(bean.date);
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.json.SDBDateDeserializer#deserialize}.
   */
  @Test
  public void testDeserialize3() throws Exception {
    TestBean bean = TestBean.load(this.getClass().getResource("testbean3.json"), TestBean.class, null);

    assertNull(bean.date);
  }

}
