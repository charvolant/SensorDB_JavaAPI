/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 22 Jan 2014
 */
package au.csiro.cmar.weru;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A timestamped observation.
 * <p>
 * SensorDB only allows double observations.
 * <p>
 * Subclasses can add additional information, such as metadata, etc.
 * 
 * TODO Observations should, theretically be paramaterised, but it doesn't seem worth the while at the moment.
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class Observation extends JSONSerialisable implements Comparable<Observation> {
  /** The observation timestamp */
  @JsonProperty
  private Date timestamp;
  /** The observation value */
  @JsonProperty
  private double value;
  
  /**
   * Empty constructor for deserialisation.
   */
  protected Observation() {
  }
  
  /**
   * Construct an abstract observation.
   * 
   * @param timestamp The observation timestamp
   * @param value The observation value
   */
  public Observation(Date timestamp, double value) {
    this.timestamp = timestamp;
    this.value = value;
  }

  /**
   * Get the timestamp
   * 
   * @return The timestamp
   */
  public Date getTimestamp() {
    return this.timestamp;
  }

  /**
   * Get the value.
   */
  public double getValue() {
    return this.value;
  }

  /**
   * Compare the observation for timestamp ordering.
   * 
   * @param o The observation to compare against.
   * 
   * @return The comparison result
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(Observation o) {
    return this.timestamp.compareTo(o.timestamp);
  }

}
