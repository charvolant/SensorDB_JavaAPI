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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Serialize a date into seconds from 1.1.1970, as used by sensordb
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class SDBDateSerializer extends JsonSerializer<Date> {

  /**
   * Construct a date serializer.
   */
  public SDBDateSerializer() {
  }

  /**
   * Serialize a date.
   * 
   * @param date The date
   * @param generator The json generator
   * @param serializer The serilaization provider
   * 
   * @throws IOException If unable to write
   * @throws JsonProcessingException If unable to process the date
   * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
   */
  @Override
  public void serialize(Date date, JsonGenerator generator, SerializerProvider serializer)
      throws IOException, JsonProcessingException {
    if (date == null)
      generator.writeNull();
    else
      generator.writeNumber(date.getTime() / 1000L);
  }

}
