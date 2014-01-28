package au.csiro.cmar.weru;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 19 Jan 2014
 */

/**
 * Test context for deserialization
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class TestContext implements SDBContext {
  private Map<Object, SDBObject> values;
  private SDBSession session;
  
  public TestContext() throws Exception {
    this.values = new HashMap<Object, SDBObject>();
    this.session = new SDBSession(new URL(SDBTest.HOST1));
    this.session.setCookie(SDBTest.COOKIE);
  }
  
  public void add(SDBObject object) {
    this.values.put(object.getId(), object);
  }

  /**
   * Find a binding in the pre-bound values.
   * 
   * @param id The identifier
   * 
   * @return The binding or null for not found
   * 
   * @see au.csiro.cmar.weru.SDBContext#findBinding(java.lang.Object)
   */
  @Override
  public SDBObject findBinding(Object id) {
    return this.values.get(id);
  }

  /**
   * Get the session associated with the context.
   * 
   * @return The session
   * 
   * @see au.csiro.cmar.weru.SDBContext#getSession()
   */
  @Override
  public SDBSession getSession() {
    return this.session;
  }
  
  
}
