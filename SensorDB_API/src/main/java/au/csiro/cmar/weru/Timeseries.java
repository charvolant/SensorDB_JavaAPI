/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 22 Jan 2014
 */
package au.csiro.cmar.weru;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A timeseries is a collection of timestamp-value pairs.
 * <p>
 * The timeseries is ordered by the timestamp.
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class Timeseries extends JSONSerialisable implements Iterable<Observation> {
  /** The timeseries entries. Implemented as a sorted array */
  @JsonProperty
  private ArrayList<Observation> timeseries;

  /**
   * Construct an empty timeseries.
   */
  public Timeseries() {
    this(32);
  }

  /**
   * Construct an empty timeseries with an initial capacity.
   * 
   * @param capacity The initial capacity
   */
  public Timeseries(int capacity) {
    this.timeseries = new ArrayList<Observation>(capacity);
  }
  
  /**
   * Get the earliest timestamp in the timeseries.
   * 
   * @return The start time, or null if empty
   */
  public Date getStart() {
    return this.timeseries.isEmpty() ? null : this.timeseries.get(0).getTimestamp();
  }
  
  /**
   * Get the latest timestamp in the timeseries.
   * 
   * @return The end time, or null if empty
   */
  public Date getEnd() {
    return this.timeseries.isEmpty() ? null : this.timeseries.get(this.timeseries.size() - 1).getTimestamp();
  }
  
  /**
   * Add an observation.
   * <p>
   * The observation is added in timestamp order.
   * If there is already an observation with the same timestamp,
   * that observation is replaced.
   * 
   * @param obs The observation to add
   */
  public void add(Observation obs) {
    Date end = this.getEnd();
    int index;
    
    // Usual case
    if (end == null || end.before(obs.getTimestamp()))
      this.timeseries.add(obs);
    // Insert into the correct place
    index = Collections.binarySearch(this.timeseries, obs);
    if (index < 0)
      this.timeseries.add(-(index + 1), obs);
    else
      this.timeseries.set(index, obs);
  }

  /**
   * Create an iterator for the timeseries.
   * @return
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<Observation> iterator() {
    return this.timeseries.iterator();
  }
}
