package au.csiro.cmar.weru;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class SDBStream extends SDBObject {
  /** The post batch size */
  private static final int POST_BATCH = 60;

  /** The owning node */
  @JsonIdentityReference(alwaysAsId = true)
  @JsonProperty("nid")
  private SDBNode node;
  /** The owning user */
  @JsonIdentityReference(alwaysAsId = true)
  @JsonProperty("uid")
  private SDBUser user;
  /** The measurement type */
  @JsonIdentityReference(alwaysAsId = true)
  @JsonProperty("mid")
  private SDBMeasurement measurement;
  /** The token for accessing the stream */
  @JsonProperty
  private String token;

  /**
   * Construct an empty stream.
   */
  public SDBStream() {
  }

  /**
   * Get the node.
   *
   * @return the node
   */
  public SDBNode getNode() {
    return this.node;
  }

  /**
   * Set the node.
   *
   * @param node the new node
   */
  public void setNode(SDBNode node) {
    this.node = node;
  }

  /**
   * Get the user.
   *
   * @return the user
   */
  public SDBUser getUser() {
    return this.user;
  }

  /**
   * Set the user.
   *
   * @param user the new user
   */
  public void setUser(SDBUser user) {
    this.user = user;
  }

  /**
   * Get the measurement.
   *
   * @return the measurement
   */
  public SDBMeasurement getMeasurement() {
    return this.measurement;
  }

  /**
   * Set the measurement.
   *
   * @param measurement the new measurement
   */
  public void setMeasurement(SDBMeasurement measurement) {
    this.measurement = measurement;
  }

  /**
   * Get the token.
   *
   * @return the token
   */
  public String getToken() {
    return this.token;
  }

  /**
   * Set the token.
   *
   * @param token the new token
   */
  public void setToken(String token) {
    this.token = token;
  }

  /**
   * Post a timeseries to the server.
   * 
   * @param session The session
   * @param timeseries The timeseries to post
   * 
   * @return The number of observations writter
   * 
   * @throws SDBException if unable to post data to the server
   */
  public int postData(SDBSession session, Timeseries timeseries) throws SDBException {
    Iterator<Observation> obs = timeseries.iterator();
    int total = 0;

    while (obs.hasNext()) {
      int count = 0;
      Map<String, List<Observation>> update = new HashMap<String, List<Observation>>();
      List<Observation> batch = new ArrayList<Observation>(this.POST_BATCH);

      while (obs.hasNext() && count < this.POST_BATCH) {
        batch.add(obs.next());
        count++;
      }
      update.put(this.getToken(), batch);
      session.post("/data", update, null, this.getContext());
      total += count;
    }
    return total;
  }

  /**
   * Get data from the server.
   * <p>
   * The data is returned strictly within the start/end date/times, inclusive.
   * 
   * @param session The session to use
   * @param start The strart date/time
   * @param end The end date/time
   * @param aggregation The aggregation level (if null then raw)
   * 
   * @return A timeseries with the returned data.
   * 
   * @throws SDBException if unable to get the data.
   */
  public Timeseries getData(SDBSession session, Date start, Date end, String aggregation) throws SDBException {
    DataRequest request = new DataRequest(this, start, end, aggregation);
    JsonNode response = session.postTree("/data_download", request, this.getContext());
    JsonNode rl = response.get(this.getId());
    Timeseries result;
    
    if (rl == null || !rl.isArray())
      throw new SDBException("Unexpected response, expected JSON array got " + rl);
    result = new Timeseries(rl.size());
    for (JsonNode o: rl) {
      if (o == null || !o.isArray() || o.size() != 2)
        throw new SDBException("Expected time, value array, got " + o);
      Date timestamp = new Date(o.get(0).longValue() * 1000L);
      double value = o.get(1).doubleValue();
      if ((start == null || !start.after(timestamp)) && (end == null || !end.before(timestamp)))
        result.add(new Observation(timestamp, value));
    }
    return result;
   }
  
  /**
   * A get data request.
   */
  private static class DataRequest extends JSONSerialisable {
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("sid")
    private SDBStream stream;
    @JsonProperty
    @JsonFormat(pattern = "yyyy-MM-DD")
    private Date start;
    @JsonProperty
    @JsonFormat(pattern = "yyyy-MM-DD")
    private Date end;
    @JsonProperty
    private String aggregation;
    
    /**
     * Construct a request.
     * 
     * @param start The start date
     * @param end The end date
     * @param aggregation The agregation level (or null for raw data)
     */
    private DataRequest(SDBStream stream, Date start, Date end, String aggregation) {
      super();
      this.stream = stream;
      this.start = start;
      this.end = end;
      this.aggregation = aggregation == null ? "raw" : aggregation;
    }
  }

}
