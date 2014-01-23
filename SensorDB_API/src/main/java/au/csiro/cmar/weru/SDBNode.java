package au.csiro.cmar.weru;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SDBNode extends SDBObject {
  /** The owning experiment */
  @JsonIdentityReference(alwaysAsId = true)
  @JsonProperty("eid")
  private SDBExperiment experiment;
  /** The owning user */
  @JsonIdentityReference(alwaysAsId = true)
  @JsonProperty("uid")
  private SDBUser user;
  /** The location latitude */
  @JsonProperty
  private double lat;
  /** The location longitude */
  @JsonProperty
  private double lon;
  /** The location altitude */
  @JsonProperty
  private double alt;
  /** The public access flag */
  @JsonProperty("public_access")
  private int publicAccess;

  /** The associated streams. */ 
  //Not a JsonProperty since the streams arrive separated from the nodes during login 
  private Map<String, SDBStream> streams;

  public SDBNode() {
    this.streams = new HashMap<String, SDBStream>();
  }


  /**
   * Get the owning experiment.
   *
   * @return the experiment
   */
  public SDBExperiment getExperiment() {
    return this.experiment;
  }


  /**
   * Set the owning experiment.
   *
   * @param experiment the new experiment
   */
  public void setExperiment(SDBExperiment experiment) {
    this.experiment = experiment;
  }


  /**
   * Get the owning user.
   *
   * @return the user
   */
  public SDBUser getUser() {
    return this.user;
  }


  /**
   * Set the owning user.
   *
   * @param user the new user
   */
  public void setUser(SDBUser user) {
    this.user = user;
  }


  /**
   * Get the latitude.
   *
   * @return the latitude
   */
  public double getLat() {
    return this.lat;
  }


  /**
   * Set the latitude.
   *
   * @param lat the new latitude
   */
  public void setLat(double lat) {
    this.lat = lat;
  }


  /**
   * Get the longitude.
   *
   * @return the longitude
   */
  public double getLon() {
    return this.lon;
  }


  /**
   * Set the longitude.
   *
   * @param lon the new lonitude
   */
  public void setLon(double lon) {
    this.lon = lon;
  }


  /**
   * Get the altitude.
   *
   * @return the altitude
   */
  public double getAlt() {
    return this.alt;
  }


  /**
   * Set the altitude.
   *
   * @param alt the new altitude
   */
  public void setAlt(double alt) {
    this.alt = alt;
  }


  /**
   * Get the public access flags.
   *
   * @return the public access flags
   */
  public int getPublicAccess() {
    return this.publicAccess;
  }


  /**
   * Set the public access flags.
   *
   * @param publicAccess the new public access flags
   */
  public void setPublicAccess(int publicAccess) {
    this.publicAccess = publicAccess;
  }

  /**
   * Add a stream to the list of streams.
   * 
   * @param stream The new stream
   */
  public void addStream(SDBStream stream) {
    this.streams.put(stream.getName(), stream);
  }

  /**
   * Create a new stream for this node.
   * 
   * @param session The sensordb session
   * @param name The stream name (must be unique to the node)
   * @param measurement The stream measurement type
   * @param description A description
   * @param website A website URI
   * @param picture A picture URI
   * 
   * @return The created stream
   * 
   * @throws SDBException If unable to create the stream
   */
  public SDBStream createStream(SDBSession session, String name, SDBMeasurement measurement, String description, URI website, URI picture) throws SDBException {
    SDBStream stream;

    if (this.streams.containsKey(name))
      throw new SDBException("Stream " + name + " already exists on node " + this.getName());
    stream = new SDBStream();
    stream.setId(null);
    stream.setName(name);
    stream.setMeasurement(measurement);
    stream.setNode(this);
    stream.setDescription(description);
    stream.setWebsite(website);
    stream.setPicture(picture);
    stream = session.post("/streams", stream, SDBStream.class, this.getContext());
    this.addStream(stream);
    return stream;
  }

  /**
   * Delete a stream.
   * 
   * @param The sensordb session
   * 
   * @param The stream name
   * 
   * @throws SDBException if unable to delete the stream
   */
  public void deleteStream(SDBSession session, String name) throws SDBException {
    SDBStream stream = this.streams.get(name);

    if (stream == null)
      throw new SDBException("No stream with name " + name);
    session.delete("/streams?sid=" + stream.getId(), this.getContext());
    this.streams.remove(name);
  }

  /**
   * Get a stream by name.
   * 
   * @param name The stream name
   * 
   * @return The named stream, or null for not found
   */
  public SDBStream getStream(String name) {
    return this.streams.get(name);
  }
  
  
  /**
   * Get the streams.
   * 
   * @return An unmodifiable collection of streams.
   */
  public Collection<SDBStream> getStreams() {
    return Collections.unmodifiableCollection(this.streams.values());
  }

}