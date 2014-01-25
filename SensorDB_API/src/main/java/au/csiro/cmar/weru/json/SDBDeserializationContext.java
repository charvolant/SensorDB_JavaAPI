/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 20 Jan 2014
 */
package au.csiro.cmar.weru.json;

import java.io.IOException;

import au.csiro.cmar.weru.SDBContext;
import au.csiro.cmar.weru.SDBObject;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerCache;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;

/**
 * A deserialization context that allows pre-defintion fof certain object id mappings.
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class SDBDeserializationContext extends DefaultDeserializationContext {
  private static final long serialVersionUID = -402085877449807767L;

  /** The sensordb context */
  private SDBContext context;
  
  /**
   * {@inheritDoc}
   * 
   * @param context The context for accessing additional bindings
   */
  public SDBDeserializationContext(DeserializerFactory df,
      DeserializerCache cache, SDBContext context) {
    super(df, cache);
    this.context = context;
  }

  /**
   * {@inheritDoc}
   */
  public SDBDeserializationContext(SDBDeserializationContext src,
      DeserializerFactory factory) {
    super(src, factory);
    this.context = src.context;
  }

  /**
   * Construct a SDBDeserializationContext.
   * @param src
   * @param config
   * @param jp
   * @param values
   */
  public SDBDeserializationContext(SDBDeserializationContext src,
      DeserializationConfig config, JsonParser jp, InjectableValues values) {
    super(src, config, jp, values);
    this.context = src.context;
  }

  /**
   * {@inheritDoc}

   * @see com.fasterxml.jackson.databind.deser.DefaultDeserializationContext#with(com.fasterxml.jackson.databind.deser.DeserializerFactory)
   */
  @Override
  public DefaultDeserializationContext with(DeserializerFactory factory) {
    return new SDBDeserializationContext(this, factory);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.fasterxml.jackson.databind.deser.DefaultDeserializationContext#createInstance(com.fasterxml.jackson.databind.DeserializationConfig, com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.InjectableValues)
   */
  @Override
  public DefaultDeserializationContext createInstance(
      DeserializationConfig config, JsonParser jp, InjectableValues values) {
    return new SDBDeserializationContext(this, config, jp, values);
  }

  /**
   * {@inheritDoc}
   * <p>
   * Extended to look up the context for any unknown objects and
   * bind them.
   * 
   * @see com.fasterxml.jackson.databind.deser.DefaultDeserializationContext#findObjectId(java.lang.Object, com.fasterxml.jackson.annotation.ObjectIdGenerator)
   */
  @Override
  public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> generator) {
    ReadableObjectId oid = super.findObjectId(id, generator);
    
    if (oid.item == null && this.context != null) {
      SDBObject object = this.context.findBinding(id);
      if (object != null)
        try {
          oid.bindItem(object);
        } catch (IOException ex) {
          throw new IllegalStateException("Unable to bind " + object + " to " + id, ex);
        }
    }
    return oid; 
  }
  
  
}
