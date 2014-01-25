/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 17 Jan 2014
 */
package au.csiro.cmar.weru;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;

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
  private static final Date TIMESTAMP1 = new Date(1386243845000L);
  private static final Date TIMESTAMP2 = new Date(1386245052000L);
  private static final Date TIMESTAMP3 = new Date(1386245054000L);
  private static final double VALUE1 = 28.0;
  private static final double VALUE2 = 27.0;
  private static final double VALUE3 = 29.0;

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

  /**
   * Test method for {@link au.csiro.cmar.weru.SDBStream#getData}.
   */
  @Test
  public void testGetData1() throws Exception {
    SDBStream stream = SDBStream.load(this.getClass().getResource("stream1.json"), SDBStream.class, this.context);
    Timeseries timeseries;
    Iterator<Observation> i;
    Observation o;
    SDBSession session = new SDBSession(new URL("http://localhost:" + this.PORT));

    this.createServer();
    session.setCookie(this.COOKIE);
    timeseries = stream.getData(session, null, null, null);
    i = timeseries.iterator();
    assertTrue(i.hasNext());
    o = i.next();
    assertEquals(this.TIMESTAMP1, o.getTimestamp());
    assertEquals(this.VALUE1, o.getValue(), 0.0000001);
    assertTrue(i.hasNext());
    o = i.next();
    assertEquals(this.TIMESTAMP2, o.getTimestamp());
    assertEquals(this.VALUE2, o.getValue(), 0.0000001);
    assertTrue(i.hasNext());
    o = i.next();
    assertEquals(this.TIMESTAMP3, o.getTimestamp());
    assertEquals(this.VALUE3, o.getValue(), 0.0000001);
    assertFalse(i.hasNext());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SDBStream#getData}.
   */
  @Test
  public void testGetData2() throws Exception {
    SDBStream stream = SDBStream.load(this.getClass().getResource("stream1.json"), SDBStream.class, this.context);
    Timeseries timeseries;
    Iterator<Observation> i;
    Observation o;
    SDBSession session = new SDBSession(new URL("http://localhost:" + this.PORT));

    this.createServer();
    session.setCookie(this.COOKIE);
    timeseries = stream.getData(session, this.TIMESTAMP2, null, null);
    i = timeseries.iterator();
    assertTrue(i.hasNext());
    o = i.next();
    assertEquals(this.TIMESTAMP2, o.getTimestamp());
    assertEquals(this.VALUE2, o.getValue(), 0.0000001);
    assertTrue(i.hasNext());
    o = i.next();
    assertEquals(this.TIMESTAMP3, o.getTimestamp());
    assertEquals(this.VALUE3, o.getValue(), 0.0000001);
    assertFalse(i.hasNext());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SDBStream#getData}.
   */
  @Test
  public void testGetData3() throws Exception {
    SDBStream stream = SDBStream.load(this.getClass().getResource("stream1.json"), SDBStream.class, this.context);
    Timeseries timeseries;
    Iterator<Observation> i;
    Observation o;
    SDBSession session = new SDBSession(new URL("http://localhost:" + this.PORT));

    this.createServer();
    session.setCookie(this.COOKIE);
    timeseries = stream.getData(session, null, this.TIMESTAMP2, null);
    i = timeseries.iterator();
    assertTrue(i.hasNext());
    o = i.next();
    assertEquals(this.TIMESTAMP1, o.getTimestamp());
    assertEquals(this.VALUE1, o.getValue(), 0.0000001);
    assertTrue(i.hasNext());
    o = i.next();
    assertEquals(this.TIMESTAMP2, o.getTimestamp());
    assertEquals(this.VALUE2, o.getValue(), 0.0000001);
    assertFalse(i.hasNext());
  }

  /**
   * Test method for {@link au.csiro.cmar.weru.SDBStream#postData(SDBSession, Timeseries)}.
   */
  @Test
  public void testPostData1() throws Exception {
    SDBStream stream = SDBStream.load(this.getClass().getResource("stream1.json"), SDBStream.class, this.context);
    Timeseries timeseries = new Timeseries();
    SDBSession session = new SDBSession(new URL("http://localhost:" + this.PORT));
    int count;
    
    this.createServer();
    session.setCookie(this.COOKIE);
    timeseries.add(new Observation(this.TIMESTAMP1, this.VALUE1));
    timeseries.add(new Observation(this.TIMESTAMP2, this.VALUE2));
    timeseries.add(new Observation(this.TIMESTAMP3, this.VALUE3));
    count = stream.postData(session, timeseries);
    assertEquals(3, count);
  }

}
