/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 16 Jan 2014
 */
package au.csiro.cmar.weru;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import au.csiro.cmar.weru.json.SDBDateDeserializer;
import au.csiro.cmar.weru.json.SDBDateSerializer;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Common sensordb information.
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="_id")
abstract public class SDBObject extends JSONSerialisable {
  /** The context */
  @JacksonInject("context")
  private SDBContext context;
  /** The (mongodb) object identifier */
  @JsonProperty("_id")
  private String id;
  /** The object name */
  @JsonProperty
  private String name;
  /** A reference to a graphic for the object */
  @JsonProperty
  public URI picture;
  /** A reference to a website describing the object */
  @JsonProperty
  public URI website;
  /** A long description */
  @JsonProperty
  public String description;
  /** The metadata dictionary */
  @JsonProperty("metadata")
  private List<Metadata> metadata;
  /** The date created */
  @JsonProperty("created_at")
  @JsonSerialize(using = SDBDateSerializer.class)
  @JsonDeserialize(using = SDBDateDeserializer.class)
  private Date createdAt;
  /** The date updated */
  @JsonProperty("updated_at")
  @JsonSerialize(using = SDBDateSerializer.class)
  @JsonDeserialize(using = SDBDateDeserializer.class)
  private Date updatedAt;
  
  /**
   * Construct an empty SDBObject.
   */
  public SDBObject() {
    this.id = UUID.randomUUID().toString();
    this.metadata = new ArrayList<Metadata>();
  }

  /**
   * Get the context.
   *
   * @return the context
   */
  public SDBContext getContext() {
    return this.context;
  }

  /**
   * Set the context.
   *
   * @param context the new context
   */
  public void setContext(SDBContext context) {
    this.context = context;
  }

  /**
   * Get the id.
   * <p>
   * This corresponds to a mongo object id.
   *
   * @return the id
   */
  public String getId() {
    return this.id;
  }

  /**
   * Set the id.
   *
   * @param id the new id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Get the name.
   *
   * @return the name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Set the name.
   *
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the picture.
   *
   * @return the picture
   */
  public URI getPicture() {
    return this.picture;
  }

  /**
   * Set the picture.
   *
   * @param picture the new picture
   */
  public void setPicture(URI picture) {
    this.picture = picture;
  }

  /**
   * Get the website.
   *
   * @return the website
   */
  public URI getWebsite() {
    return this.website;
  }

  /**
   * Set the website.
   *
   * @param website the new website
   */
  public void setWebsite(URI website) {
    this.website = website;
  }

  /**
   * Get the description.
   *
   * @return the description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Set the description.
   *
   * @param description the new description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Get the metadata entries.
   *
   * @return the metadata
   */
  public List<Metadata> getMetadata() {
    return this.metadata;
  }

  /**
   * Set the metadata entries.
   *
   * @param metadata the new metadata
   */
  public void setMetadata(List<Metadata> metadata) {
    this.metadata = metadata;
  }

  /**
   * Get the date the object was created on.
   *
   * @return the created date/time
   */
  public Date getCreatedAt() {
    return this.createdAt;
  }

  /**
   * Set the created date/time.
   *
   * @param createdAt the new created date/time
   */
  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * Get the date the object was updated.
   *
   * @return the updated date/time
   */
  public Date getUpdatedAt() {
    return this.updatedAt;
  }

  /**
   * Set the updated date/time.
   *
   * @param updatedAt the new updated date/time
   */
  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }
  
  /**
   * Update this object with new data.
   * <p>
   * This can happen if a new dictionary is downloaded.
   * To keep object identity, the new data is copied into the
   * old instance.
   * <p>
   * The object id may change as a consequence, so any instance
   * of {@link SDBContext} that maps the id will also need to be
   * updated. 
   * 
   * @param update
   */
  protected void updateFrom(SDBObject update) {
    this.id = update.id;
    this.createdAt = update.createdAt;
    this.updatedAt = update.updatedAt;
    this.name = update.name;
    this.description = update.description;
    this.metadata = update.metadata;
    this.picture = update.picture;
    this.website = update.website;
  }
}
