/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 22 Jan 2014
 */
package au.csiro.cmar.weru;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

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
  private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
  
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
   * Construct a timeseries from a CSV file.
   * 
   * @param source The source file
   * @param timestampColumn The column (counting from 0) that holds the timestamp
   * @param valueColumn The column (counting from 0) that holds the value
   * @param headers The number of header lines
   */
  public Timeseries(File source, int timestampColumn, int valueColumn, int headers) throws SDBException {
    try {
      FileReader reader = new FileReader(source);
      
      this.buildFromCSV(reader, timestampColumn, valueColumn, headers);
      reader.close();
    } catch (NumberFormatException | IOException ex) {
      throw new SDBException("Unable to read timeseries from " + source, ex);
    }
  }
  
  /**
   * Construct a timeseries from a CSV stream.
   * <p>
   * Timestamps can either be in ISO8601 format or milliseconds
   * since 1970-01-01T00:00:00 format.
   * 
   * @param reader The source reader
   * @param timestampColumn The column that holds the timestamp
   * @param valueColumn The column that holds the value
   * @param headers The number of header lines
   * 
   * @throws SDBException if unable to build the timeseries
   */
  public Timeseries(Reader reader, int timestampColumn, int valueColumn, int headers) throws SDBException {
    try {
      this.buildFromCSV(reader, timestampColumn, valueColumn, headers);
    } catch (NumberFormatException | IOException ex) {
      throw new SDBException("Unable to read timeseries", ex);
    }
  }
  
  /**
   * Build a timeseries from a CSV source.
   * 
   * @throws IOException, NumberFormatException 
   */
  private void buildFromCSV(Reader reader, int timestampColumn, int valueColumn, int headers) throws IOException, NumberFormatException {
    CSVReader csv = new CSVReader(reader);
    String[] line;
    Date ts;
    double v;
    
    try {
    while (headers-- > 0)
      csv.readNext();
    this.timeseries = new ArrayList<Observation>(128);
    while ((line = csv.readNext()) != null) {
      try {
        ts = this.TIMESTAMP_FORMAT.parse(line[timestampColumn]);
      } catch (ParseException ex) {
        ts = new Date(Long.parseLong(line[timestampColumn]));
      }
      v = Double.parseDouble(line[valueColumn]);
      this.add(new Observation(ts, v));
    }
    } finally {
      csv.close();
    }
    
  }
  
  /**
   * Get the size of the timeseries.
   * 
   * @return The size
   */
  public int size() {
    return this.timeseries.size();
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
   * Get an observation indexed by timestamp.
   * <p>
   * TODO Inefficient implementation. Creates a finder for each call.
   * 
   * @param timestamp The timestamp
   * 
   * @return The matching observation, or null for none
   */
  public Observation get(Date timestamp) {
    Observation finder = new Observation(timestamp, 0.0);
    int index = Collections.binarySearch(this.timeseries, finder);
    
    return index < 0 ? null : this.timeseries.get(index);
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
   * 
   * @return An iterator across the series
   * 
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<Observation> iterator() {
    return this.timeseries.iterator();
  }
}
