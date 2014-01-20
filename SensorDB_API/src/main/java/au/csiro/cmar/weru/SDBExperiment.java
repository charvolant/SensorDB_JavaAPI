package au.csiro.cmar.weru;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SDBExperiment extends SDBObject {
  /** The owner of the experiment */ 
  @JsonIdentityReference(alwaysAsId = true)
  @JsonProperty("uid")
  private SDBUser user;
  /** The experiment timezone */
  @JsonProperty
  private TimeZone timezone;
  // Access Restriction [0,1]
  @JsonProperty("access_restriction")
  private int accessRestriction;

  /** The nodes attached to the experiment. */
  // Not a JsonProperty because the login returns the nodes as seperate entities
  public Map<String, SDBNode> nodes;

  /**
   * Construct an empty experiment.
   */
  public SDBExperiment() {
    this.nodes = new HashMap<String, SDBNode>();
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
   * Get the timezone.
   *
   * @return the timezone
   */
  public TimeZone getTimezone() {
    return this.timezone;
  }


  /**
   * Set the timezone.
   *
   * @param timezone the new timezone
   */
  public void setTimezone(TimeZone timezone) {
    this.timezone = timezone;
  }


  /**
   * Get the accessRestriction.
   *
   * @return the accessRestriction
   */
  public int getAccessRestriction() {
    return this.accessRestriction;
  }


  /**
   * Set the accessRestriction.
   *
   * @param accessRestriction the new accessRestriction
   */
  public void setAccessRestriction(int accessRestriction) {
    this.accessRestriction = accessRestriction;
  }

  /**
   * Add a node to the experiment.
   * 
   * @param node The new node
   */
  public void addNode(SDBNode node) {
    this.nodes.put(node.getName(), node);
  }
  
  public SDBNode createNode(String host, String cookie, String name, String eid, String description, URI website, URI picture, int public_access) throws SDBException {
    SDBNode node;
    String content;

    if (this.nodes.containsKey(name))
      throw new SDBException("Node with name " + name + " already exists for experiment " + this.getName());

    try {
      URL url = new URL(host + "/nodes");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setDoOutput(true);
      conn.setUseCaches(false);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Accept", "application/json");
      conn.setRequestProperty("Cookie", cookie);

      node = new SDBNode();
      node.setId(null);
      node.setName(name);
      node.setExperiment(this);
      node.setDescription(description);
      node.setWebsite(website);
      node.setPicture(picture);
      node.setPublicAccess(public_access);
      content = node.toJson();

      conn.setRequestProperty("Content-Length",
          Integer.toString(content.length()));
      conn.getOutputStream().write(content.getBytes());
      conn.getOutputStream().flush();

      if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {

        throw new SDBException("POST method failed: " + conn.getResponseCode() + "/" + conn.getResponseMessage());
      } else {
        node = SDBNode.load(conn.getInputStream(), SDBNode.class, this.getContext());
        this.nodes.put(name, node);
        return node;
      }
    } catch (IOException ex) {
      throw new SDBException("Unable to access server", ex);
    } catch (URISyntaxException ex) {
      throw new SDBException("Unable to parse new node from server", ex);
    }
  }

  public void deleteNode(String host, String cookie, String name) throws SDBException {
    SDBNode node = this.nodes.get(name);

    if (node == null)
      throw new SDBException("No node named " + name + " in experiment " + this.getName());
    try {
      URL url = new URL(host + "/nodes?nid=" + node.getId());
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setDoInput(true);
      conn.setDoOutput(true);
      conn.setRequestMethod("DELETE");
      conn.setRequestProperty("Cookie", cookie);

      if (conn.getResponseCode() == 200) 
        this.nodes.remove(node.getId());
      else 
        throw new SDBException("DELETE method failed: " + conn.getResponseCode() + "/" + conn.getResponseMessage());
    } catch (IOException ex) {
      throw new SDBException("Unable to access server to delete node " + name);
    }
  }

  public SDBNode getNode(String name) {
    return this.nodes.get(name);
  }	

}