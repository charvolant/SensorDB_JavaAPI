/*
 * $Id$
 *
 * Copyright (c) 2012 CSIRO
 *
 * Created 10 Oct 2012
 */
package au.csiro.cmar.weru;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import au.csiro.cmar.weru.json.SDBDeserializationContext;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;

/**
 * An object that can be loaded/serialised into JSON
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2012 CSIRO
 *
 */
@JsonAutoDetect(creatorVisibility=Visibility.NONE, fieldVisibility=Visibility.NONE, getterVisibility=Visibility.NONE, isGetterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE)
public abstract class JSONSerialisable {

  /**
   * Construct a JSONSerialisable.
   */
  public JSONSerialisable() {
  }

  /**
   * Write this obejct out as JSON.
   * 
   * @param writer The writer to write to
   * 
   * @throws JsonGenerationException if unable to generate JSON
   * @throws JsonMappingException if unable to map the configuration onto JSON
   * @throws IOException if unable to write the value
   */
  public void save(Writer writer) throws JsonGenerationException, JsonMappingException, IOException {
    this.saveObject(this, writer);
  }
  
  /**
   * Convert this object into a JSON string.
   * 
   * @return The JSON string for this object
   */
  public String toJson() {
    StringWriter writer = new StringWriter(128);
    
    try {
      this.save(writer);
      return writer.toString();
    } catch (Exception ex) {
      return "Invalid object: " + ex.getMessage();
    }
    
  }

  /**
   * Load an JSON object of a specific class.
   * 
   * 
   * @param stream The input stream
   * @param clazz The class of object to load
   * @param context The sensordb context, if any
   * 
   * @return The loaded object
   * 
   * @throws IOException If unable to read or parse the configuration
   */
  public static <C extends JSONSerialisable> C load(InputStream stream, Class<C> clazz, SDBContext context) throws IOException, URISyntaxException {
    SDBDeserializationContext dc = new SDBDeserializationContext(BeanDeserializerFactory.instance, null, context);
    ObjectMapper mapper = new ObjectMapper(null, null, dc);
    InjectableValues.Std inject = new InjectableValues.Std();
    
    inject.addValue("context", context);
    mapper.setInjectableValues(inject);
    return mapper.readValue(stream, clazz);
  }

  /**
   * Load an array JSON object of a specific class.
   * 
   * @param stream The input stream
   * @param clazz The class of object to load
   * @param context The sensordb context, if any
   * 
   * @return The loaded object
   * 
   * @throws IOException If unable to read or parse the configuration
   */
  public static <C extends JSONSerialisable> List<C> loadList(InputStream stream, Class<C> clazz, SDBContext context) throws IOException, URISyntaxException {
    SDBDeserializationContext dc = new SDBDeserializationContext(BeanDeserializerFactory.instance, null, context);
    ObjectMapper mapper = new ObjectMapper(null, null, dc);
    InjectableValues.Std inject = new InjectableValues.Std();
    
    inject.addValue("context", context);
    mapper.setInjectableValues(inject);
    return mapper.readValue(stream, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
  }

  /**
   * Load an parsed tree from a JSON object.
   * 
   * @param stream The input stream
   * @param context The sensordb context, if any
   * 
   * @return The loaded object
   * 
   * @throws IOException If unable to read or parse the configuration
   */
  public static JsonNode loadTree(InputStream stream, SDBContext context) throws IOException, URISyntaxException {
    SDBDeserializationContext dc = new SDBDeserializationContext(BeanDeserializerFactory.instance, null, context);
    ObjectMapper mapper = new ObjectMapper(null, null, dc);
    InjectableValues.Std inject = new InjectableValues.Std();
    
    inject.addValue("context", context);
    mapper.setInjectableValues(inject);
    return mapper.readTree(stream);
  }


  /**
   * Load an JSON object of a specific class from a URL.
   * 
   * 
   * @param source The source URL
   * @param clazz The class of object to load
   * @param context The SensorDB context, if any
   * 
   * @return The loaded object
   * 
   * @throws IOException If unable to read or parse the configuration
   * @throws URISyntaxException If unable to convert the URL into a URI
   * 
   * @see #load(InputStream, URL, Class)
   */
  public static <C extends JSONSerialisable> C load(URL source, Class<C> clazz, SDBContext context) throws IOException, URISyntaxException {
    URLConnection connection;

    connection = source.openConnection();
    connection.setDoOutput(false);
    connection.setRequestProperty("Accept", "application/json");
    return load(connection.getInputStream(), clazz, context);
  }
  
  /**
   * Serialise an abitrary object as JSON.
   * 
   * @param object The object
   * @param writer The writer for the serialisation
   * 
   * @throws JsonGenerationException if unable to create JSON
   * @throws IOException if unable to write the result
   */
  public static void saveObject(Object object, Writer writer) throws JsonGenerationException, IOException {
    ObjectMapper mapper = new ObjectMapper();

    mapper.setSerializationInclusion(Include.NON_DEFAULT);
    mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    mapper.writeValue(writer, object);    
  }
  
  /**
   * Convert this object into a JSON string.
   * 
   * @return The JSON string for this object
   */
  public static String toJson(Object object) {
    StringWriter writer = new StringWriter(128);
    
    try {
      saveObject(object, writer);
      return writer.toString();
    } catch (Exception ex) {
      return "Invalid object: " + ex.getMessage();
    }
    
  }

}
