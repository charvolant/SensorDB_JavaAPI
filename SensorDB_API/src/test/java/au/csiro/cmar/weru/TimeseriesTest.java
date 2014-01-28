/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 23 Jan 2014
 */
package au.csiro.cmar.weru;

import static org.junit.Assert.*;

import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

/**
 * TODO Class documentation
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class TimeseriesTest extends SDBTest {
  private static final Date TIMESTAMP1 = new Date(123456789000L);
  private static final Date TIMESTAMP2 = new Date(123456799000L);
  private static final Date TIMESTAMP3 = new Date(123456798000L);
  private static final double VALUE1 = 10.5; 
  private static final double VALUE2 = -28.66557; 
  private static final double VALUE3 = 0.0; 
  /**
   * TODO Method documentation
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
  }
 
  /**
   * Test method for {@link au.csiro.cmar.weru.SBDUser#toJson()}.
   */
  @Test
  public void testSerialize1() {
    Timeseries timeseries = new Timeseries();
    
    timeseries.add(new Observation(this.TIMESTAMP1, this.VALUE1));
    timeseries.add(new Observation(this.TIMESTAMP2, this.VALUE2));
    assertEquals(this.loadResource("timeseries1.json"), timeseries.toJson());
  }
  
   /**
   * Test method for {@link au.csiro.cmar.weru.SDBNode#load(java.net.URL, Class)}.
   */
  @Test
  public void testDeserialize1() throws Exception {
    Timeseries timeseries = Timeseries.load(this.getClass().getResource("timeseries1.json"), Timeseries.class, null);
    
    assertEquals(this.TIMESTAMP1, timeseries.getStart());
   }

  /**
   * Test method for {@link au.csiro.cmar.weru.Timeseries#getStart()}.
   */
  @Test
  public void testGetStart1() {
    Timeseries timeseries = new Timeseries();
    
    timeseries.add(new Observation(this.TIMESTAMP1, this.VALUE1));
    timeseries.add(new Observation(this.TIMESTAMP2, this.VALUE2));
    assertEquals(this.TIMESTAMP1, timeseries.getStart());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.Timeseries#getEnd()}.
   */
  @Test
  public void testGetEnd1() {
    Timeseries timeseries = new Timeseries();
    
    timeseries.add(new Observation(this.TIMESTAMP1, this.VALUE1));
    timeseries.add(new Observation(this.TIMESTAMP2, this.VALUE2));
    assertEquals(this.TIMESTAMP2, timeseries.getEnd());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.Timeseries#add(au.csiro.cmar.weru.Observation)}.
   */
  @Test
  public void testAdd1() {
    Timeseries timeseries = new Timeseries();
    Iterator<Observation> i;
    
    timeseries.add(new Observation(this.TIMESTAMP1, this.VALUE1));
    i = timeseries.iterator();
    assertTrue(i.hasNext());
    assertEquals(this.TIMESTAMP1, i.next().getTimestamp());
    assertFalse(i.hasNext());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.Timeseries#add(au.csiro.cmar.weru.Observation)}.
   */
  @Test
  public void testAdd2() {
    Timeseries timeseries = new Timeseries();
    Iterator<Observation> i;
    
    timeseries.add(new Observation(this.TIMESTAMP1, this.VALUE1));
    timeseries.add(new Observation(this.TIMESTAMP2, this.VALUE2));
    i = timeseries.iterator();
    assertTrue(i.hasNext());
    assertEquals(this.TIMESTAMP1, i.next().getTimestamp());
    assertTrue(i.hasNext());
    assertEquals(this.TIMESTAMP2, i.next().getTimestamp());
    assertFalse(i.hasNext());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.Timeseries#add(au.csiro.cmar.weru.Observation)}.
   */
  @Test
  public void testAdd3() {
    Timeseries timeseries = new Timeseries();
    Iterator<Observation> i;
    
    timeseries.add(new Observation(this.TIMESTAMP2, this.VALUE2));
    timeseries.add(new Observation(this.TIMESTAMP1, this.VALUE1));
    i = timeseries.iterator();
    assertTrue(i.hasNext());
    assertEquals(this.TIMESTAMP1, i.next().getTimestamp());
    assertTrue(i.hasNext());
    assertEquals(this.TIMESTAMP2, i.next().getTimestamp());
    assertFalse(i.hasNext());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.Timeseries#add(au.csiro.cmar.weru.Observation)}.
   */
  @Test
  public void testAdd4() {
    Timeseries timeseries = new Timeseries();
    Iterator<Observation> i;
    
    timeseries.add(new Observation(this.TIMESTAMP3, this.VALUE1));
    timeseries.add(new Observation(this.TIMESTAMP2, this.VALUE2));
    timeseries.add(new Observation(this.TIMESTAMP1, this.VALUE3));
    i = timeseries.iterator();
    assertTrue(i.hasNext());
    assertEquals(this.TIMESTAMP1, i.next().getTimestamp());
    assertTrue(i.hasNext());
    assertEquals(this.TIMESTAMP3, i.next().getTimestamp());
    assertTrue(i.hasNext());
    assertEquals(this.TIMESTAMP2, i.next().getTimestamp());
    assertFalse(i.hasNext());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.Timeseries#add(au.csiro.cmar.weru.Observation)}.
   */
  @Test
  public void testAdd5() {
    Timeseries timeseries = new Timeseries();
    Iterator<Observation> i;
    Observation o;
    
    timeseries.add(new Observation(this.TIMESTAMP1, this.VALUE1));
    timeseries.add(new Observation(this.TIMESTAMP2, this.VALUE2));
    timeseries.add(new Observation(this.TIMESTAMP1, this.VALUE3));
    i = timeseries.iterator();
    assertTrue(i.hasNext());
    o = i.next();
    assertEquals(this.TIMESTAMP1, o.getTimestamp());
    assertEquals(this.VALUE3, o.getValue(), 0.0000001);
    assertTrue(i.hasNext());
    assertEquals(this.TIMESTAMP2, i.next().getTimestamp());
    assertFalse(i.hasNext());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.Timeseries#size()}.
   */
  @Test
  public void testSize1() {
    Timeseries timeseries = new Timeseries();
    
    timeseries.add(new Observation(this.TIMESTAMP1, this.VALUE1));
    timeseries.add(new Observation(this.TIMESTAMP2, this.VALUE2));
    timeseries.add(new Observation(this.TIMESTAMP3, this.VALUE3));
    assertEquals(3, timeseries.size());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.Timeseries#size()}.
   */
  @Test
  public void testSize2() {
    Timeseries timeseries = new Timeseries();
    
    timeseries.add(new Observation(this.TIMESTAMP1, this.VALUE1));
    timeseries.add(new Observation(this.TIMESTAMP2, this.VALUE2));
    timeseries.add(new Observation(this.TIMESTAMP1, this.VALUE3));
    assertEquals(2, timeseries.size());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.Timeseries#size()}.
   */
  @Test
  public void testGet1() {
    Timeseries timeseries = new Timeseries();
    Observation o1, o2, o3;
    
    o1 = new Observation(this.TIMESTAMP1, this.VALUE1);
    o2 = new Observation(this.TIMESTAMP2, this.VALUE2);
    o3 = new Observation(this.TIMESTAMP3, this.VALUE3);
    timeseries.add(o1);
    timeseries.add(o2);
    timeseries.add(o3);
    assertSame(o1, timeseries.get(this.TIMESTAMP1));
    assertSame(o2, timeseries.get(this.TIMESTAMP2));
    assertSame(o3, timeseries.get(this.TIMESTAMP3));
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.Timeseries#size()}.
   */
  @Test
  public void testGet2() {
    Timeseries timeseries = new Timeseries();
    Observation o1, o2, o3;
    
    o1 = new Observation(this.TIMESTAMP1, this.VALUE1);
    o2 = new Observation(this.TIMESTAMP2, this.VALUE2);
    o3 = new Observation(this.TIMESTAMP1, this.VALUE3);
    timeseries.add(o1);
    timeseries.add(o2);
    timeseries.add(o3);
    assertSame(o3, timeseries.get(this.TIMESTAMP1));
    assertSame(o2, timeseries.get(this.TIMESTAMP2));
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.Timeseries#iterator()}.
   */
  @Test
  public void testIterator() {
    // Tested by other methods
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.Timeseries#Timeseries(java.io.Reader, int, int, int)}.
   */
  @Test
  public void testTimeseries1() throws Exception {
    Timeseries timeseries = new Timeseries(new InputStreamReader(this.getClass().getResourceAsStream("data1.csv")), 0, 2, 1);
    Iterator<Observation> i;
    Observation o;
    
    i = timeseries.iterator();
    assertTrue(i.hasNext());
    o = i.next();
    assertEquals(this.TIMESTAMP1, o.getTimestamp());
    assertEquals(this.VALUE1, o.getValue(), 0.0000001);
    assertTrue(i.hasNext());
    o = i.next();
    assertEquals(this.TIMESTAMP3, o.getTimestamp());
    assertEquals(this.VALUE3, o.getValue(), 0.0000001);
    assertTrue(i.hasNext());
    o = i.next();
    assertEquals(this.TIMESTAMP2, o.getTimestamp());
    assertEquals(this.VALUE2, o.getValue(), 0.0000001);
    assertFalse(i.hasNext());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.Timeseries#Timeseries(java.io.Reader, int, int, int)}.
   */
  @Test
  public void testTimeseries2() throws Exception {
    Timeseries timeseries = new Timeseries(new InputStreamReader(this.getClass().getResourceAsStream("data2.csv")), 0, 2, 1);
    Iterator<Observation> i;
    Observation o;
    
    i = timeseries.iterator();
    assertTrue(i.hasNext());
    o = i.next();
    assertEquals(this.TIMESTAMP1, o.getTimestamp());
    assertEquals(this.VALUE1, o.getValue(), 0.0000001);
    assertTrue(i.hasNext());
    o = i.next();
    assertEquals(this.TIMESTAMP3, o.getTimestamp());
    assertEquals(this.VALUE3, o.getValue(), 0.0000001);
    assertTrue(i.hasNext());
    o = i.next();
    assertEquals(this.TIMESTAMP2, o.getTimestamp());
    assertEquals(this.VALUE2, o.getValue(), 0.0000001);
    assertFalse(i.hasNext());
  }

}
