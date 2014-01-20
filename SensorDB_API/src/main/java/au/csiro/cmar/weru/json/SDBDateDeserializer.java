/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 16 Jan 2014
 */
package au.csiro.cmar.weru.json;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * A deserializer that maps an integer as the number of
 * seconds since 1-1-1970/00:00:00
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class SDBDateDeserializer extends JsonDeserializer<Date> {

  /**
   * Construct a date deserializer.
   */
  public SDBDateDeserializer() {
  }

  /**
   * Deserialize a date as an integer seconds into a java date.
   * 
   * @param parser The parser
   * @param context The context
   * 
   * @return The resulting date
   * @throws IOException
   * @throws JsonProcessingException
   * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationContext)
   */
  @Override
  public Date deserialize(JsonParser parser, DeserializationContext context)
      throws IOException, JsonProcessingException {
    return new Date(parser.getLongValue() * 1000L);
  }

}
