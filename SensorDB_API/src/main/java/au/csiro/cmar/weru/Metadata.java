/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 16 Jan 2014
 */
package au.csiro.cmar.weru;

import java.util.Date;

import au.csiro.cmar.weru.json.SDBDateDeserializer;
import au.csiro.cmar.weru.json.SDBDateSerializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A metadata entry for sensordb.
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class Metadata extends JSONSerialisable {
  /** The name */
  @JsonProperty
  private String name;
  /** The value */
  @JsonProperty
  private String value;
  /** The user who updated the metadata */
  @JsonProperty("updated_by")
  private String updatedBy;
  /** The last date of modification */
  @JsonProperty("updated_at")
  @JsonSerialize(using = SDBDateSerializer.class)
  @JsonDeserialize(using = SDBDateDeserializer.class)
  private Date updatedAt;
  
  /**
   * Construct an empty metadata entry.
   */
  public Metadata() {
  }

  /**
   * Construct a metadata entry with a name and value.
   * 
   * @param name The metadata name
   * @param value The metadata value
   */
  public Metadata(String name, String value) {
    super();
    this.name = name;
    this.value = value;
  }

  /**
   * Get the metadata value.
   *
   * @return the value
   */
  public String getValue() {
    return this.value;
  }

  /**
   * Set the metadata value.
   *
   * @param value the new value
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Get the user who updated the metadata.
   *
   * @return the updater
   */
  public String getUpdatedBy() {
    return this.updatedBy;
  }

  /**
   * Set the user who updated the metadata.
   *
   * @param updatedBy the new updater name
   */
  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  /**
   * Get the time the metadata was updated.
   *
   * @return the updated date/time
   */
  public Date getUpdatedAt() {
    return this.updatedAt;
  }

  /**
   * Set the time the metadata was updated.
   *
   * @param updatedAt the new time updated
   */
  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  /**
   * Get the name (key).
   *
   * @return the name
   */
  public String getName() {
    return this.name;
  }

}
