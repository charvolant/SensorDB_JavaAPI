package au.csiro.cmar.weru;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
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

  public SDBNode createNode(String name, String eid, String description, URI website, URI picture, int public_access) throws SDBException {
    SDBNode node;

    if (this.nodes.containsKey(name))
      throw new SDBException("Node with name " + name + " already exists for experiment " + this.getName());
    node = new SDBNode();
    node.setId(null);
    node.setName(name);
    node.setExperiment(this);
    node.setDescription(description);
    node.setWebsite(website);
    node.setPicture(picture);
    node.setPublicAccess(public_access);
    node = this.getSession().post("/nodes", node, SDBNode.class, this.getContext());
    this.nodes.put(name, node);
    return node;
  }

  public void deleteNode(String name) throws SDBException {
    SDBNode node = this.nodes.get(name);

    if (node == null)
      throw new SDBException("No node named " + name + " in experiment " + this.getName());
    this.getSession().delete("/nodes?nid=" + node.getId(), this.getContext());
    this.nodes.remove(node.getId());
  }

  /**
   * Find a node by name.
   * 
   * @param name The node name
   * 
   * @return 
   */
  public SDBNode getNode(String name) {
    return this.nodes.get(name);
  }	

  /**
   * Get the nodes.
   * 
   * @return An unmodifiable collection of nodes.
   */
  public Collection<SDBNode> getNodes() {
    return Collections.unmodifiableCollection(this.nodes.values());
  }

}