package au.csiro.cmar.weru;

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
  
  public TestContext() {
    this.values = new HashMap<Object, SDBObject>();
  }
  
  public void add(SDBObject object) {
    this.values.put(object.getId(), object);
  }

  /**
   * TODO Method documentation
   * @param id
   * @return
   * @see au.csiro.cmar.weru.SDBContext#findBinding(java.lang.Object)
   */
  @Override
  public SDBObject findBinding(Object id) {
    return this.values.get(id);
  }
}
