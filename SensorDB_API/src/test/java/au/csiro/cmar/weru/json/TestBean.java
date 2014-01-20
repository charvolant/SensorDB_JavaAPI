/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 16 Jan 2014
 */
package au.csiro.cmar.weru.json;

import java.util.Date;

import au.csiro.cmar.weru.JSONSerialisable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A simple test bean for serialization
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class TestBean extends JSONSerialisable {
  @JsonProperty
  @JsonSerialize(using = SDBDateSerializer.class)
  @JsonDeserialize(using = SDBDateDeserializer.class)
  public Date date;
}
