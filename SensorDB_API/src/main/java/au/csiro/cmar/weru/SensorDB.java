package au.csiro.cmar.weru;

import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SensorDB implements SDBContext {
  /** The active session */
  private SDBSession session;
  /** The available measurements */
  private Map<String, SDBMeasurement> measurements;
  /** The available users */
  private Map<String, SDBUser> users;
  /** The session experiment map */
  private Map<String, SDBExperiment> experiments;
  /** Objects by identifier */
  private Map<Object, SDBObject> objectsById;

  /**
   * Construct a SensorDB connection that can access user and measurement lists.
   * 
   * @param host The sensordb host
   * 
   * @throws SDBException if able to access the server
   */
  protected SensorDB(URL host) throws SDBException {
    this.session = new SDBSession(host);
    this.users = new HashMap<String, SDBUser>(); 
    this.measurements = new HashMap<String, SDBMeasurement>(); 
    this.experiments = new HashMap<String, SDBExperiment>();
    this.objectsById = new HashMap<Object, SDBObject>();
    this.retrieveUsers();
    this.retrieveMeasurements();
  }

  /**
   * Construct a SensorDB connection for a specific user.
   * <p>
   * The list of experiments/nodes/streams is collected for the user.
   * 
   * @param host The host URL
   * @param user The user name
   * @param password The password
   * @throws SDBException
   */
  protected SensorDB(URL host, String user, String password)
      throws SDBException {
    this.session = new SDBSession(host);
    this.users = new HashMap<String, SDBUser>(); 
    this.measurements = new HashMap<String, SDBMeasurement>(); 
    this.experiments = new HashMap<String, SDBExperiment>();
    this.objectsById = new HashMap<Object, SDBObject>();
    this.retrieveUsers();
    this.retrieveMeasurements();
    LoginResponse login = this.login(user, password);
    this.populateSensorDB(login);

  }

  private LoginResponse login(String user, String password) throws SDBException {
    Login login = new Login(user, password);
    SDBUser existing = this.getUser(user);
    // Ugh! Disgusting hack to make sure Jackson can re-bind the object
    // I feel ashamed
    this.users.remove(user);
    this.objectsById.remove(existing.getId());
    return this.session.post("/login", login, LoginResponse.class, this);
  }

  public void logout() throws SDBException {
    this.session.post("/logout", null, null, this);
    this.session.setCookie(null);
    this.session.setUser(null);
  }

  public SDBUser createUser(String username, String password, String email, String description, URI website, URI picture) throws SDBException {
    SDBUser user = new SDBUser();

    user.setName(username);
    user.setPassword(password);
    user.setEmail(email);
    user.setDescription(description);
    user.setWebsite(website);
    user.setPicture(picture);
    user = this.session.post("/register", user, SDBUser.class, this);
    this.users.put(user.getName(), user);
    this.objectsById.put(user.getId(), user);
    return user;
  }

  public void deleteUser(String username, String password)
      throws SDBException {
    Login login = new Login(username, password);

    this.session.post("/remove", login, null, this);
    this.users.remove(username);
  }

  /**
   * Get a user by name.
   * 
   * @param name The user name
   * 
   * @return The corresponding user or null for none
   */
  public SDBUser getUser(String name) {
    return this.users.get(name);
  }
  
  /**
   * Get the users.
   * <p>
   * This returns all users.
   * The session, returned by {#getSession()} contains the active user.
   * 
   * @return An unmodifiable collection of users.
   */
  public Collection<SDBUser> getUsers() {
    return Collections.unmodifiableCollection(this.users.values());
  }

  // TO BE IMPLEMENTED IN SERVER
  public SDBMeasurement createMeasurement(String name, String description, String website) throws SDBException {
    throw new SDBException("Not yet implemented");
  }

  // TO BE IMPLEMENTED IN SERVER
  public void deleteMeasurement(String name) throws SDBException {
    throw new SDBException("Not yet implemented");
  }

  /**
   * Get a measurement by name.
   * 
   * @param name The measurement name
   * 
   * @return The measurement or null for none
   */
  public SDBMeasurement getMeasurement(String name) {
    return this.measurements.get(name);
  }
  
  /**
   * Get the measurements.
   * 
   * @return An unmodifiable collection of measurements.
   */
  public Collection<SDBMeasurement> getMeasurements() {
    return Collections.unmodifiableCollection(this.measurements.values());
  }

  /**
   * Create a new experiment
   * 
   * @param name The experiment name (must be unique)
   * @param timezone The experiment timezone
   * @param description A description of the experiment
   * @param website The experiment website
   * @param picture The experiment picture
   * @param publicAaccess Set to 1 is this allows public access
   * 
   * @return The created experiment
   * 
   * @throws SDBException if unable to create the experiment
   */
  public SDBExperiment createExperiment(String name, TimeZone timezone,
      String description, URI website, URI picture,
      int publicAaccess) throws SDBException {
    SDBExperiment experiment;

    if (this.experiments.containsKey(name))
      throw new SDBException("Experiment " + name + " already exists");
    experiment = new SDBExperiment();
    experiment.setId(null);
    experiment.setName(name);
    experiment.setTimezone(timezone);
    experiment.setDescription(description);
    experiment.setWebsite(website);
    experiment.setPicture(picture);
    experiment.setAccessRestriction(publicAaccess);
    experiment = this.session.post("/experiments", experiment, SDBExperiment.class, this);
    this.experiments.put(experiment.getName(), experiment);
    this.objectsById.put(experiment.getId(), experiment);
    return experiment;
  }

  public void deleteExperiment(String name) throws SDBException {
    SDBExperiment experiment = this.experiments.get(name);
    if (experiment == null)
      throw new SDBException("No experiment named " + name);

    this.session.delete("/experiments?eid=" + experiment.getId(), this);
  }

  /**
   * Get an experiment by name.
   * 
   * @param name The experiment name
   * 
   * @return The experiment or null for not foudn
   */
  public SDBExperiment getExperiment(String name) {
    return this.experiments.get(name);
  }
  
  /**
   * Get the experiments.
   * 
   * @return An unmodifiable collection of experiments.
   */
  public Collection<SDBExperiment> getExperiments() {
    return Collections.unmodifiableCollection(this.experiments.values());
  }

  private void populateSensorDB(LoginResponse data) {
    SDBUser user = this.getUser(data.user.getName());

    if (user != null)
      user.updateFrom(data.user);
    else {
      user = data.user;
      this.users.put(user.getName(), user);
      this.objectsById.put(user.getId(), user);
    }
    this.session.setUser(user);
    for (SDBExperiment experiment: data.experiments) {
      this.experiments.put(experiment.getName(), experiment);
      this.objectsById.put(experiment.getId(), experiment);
    }
    for (SDBNode node: data.nodes) {
      SDBExperiment experiment = node.getExperiment();
      if (experiment != null)
        experiment.addNode(node);
    }
    for (SDBStream stream: data.streams) {
      SDBNode node = stream.getNode();
          if (node != null)
            node.addStream(stream);
    }
  }

  public void retrieveUsers() throws SDBException {
    List<SDBUser> newUsers = this.session.getList("/users", SDBUser.class, this);

    for (SDBUser user: newUsers) {
      SDBUser old = this.users.get(user.getName());
      if (old != null) {
        String oid = old.getId();

        old.updateFrom(user);
        if (!oid.equals(old.getId())) {
          this.objectsById.remove(oid);
          this.objectsById.put(old.getId(), old);
        }
      } else {
        this.users.put(user.getName(), user);
        this.objectsById.put(user.getId(), user);
      }
    }
  }

  public void retrieveMeasurements() throws SDBException {
    List<SDBMeasurement> newMeasurements = this.session.getList("/measurements", SDBMeasurement.class, this);

    for (SDBMeasurement measurement: newMeasurements) {
      SDBMeasurement old = this.measurements.get(measurement.getName());
      if (old != null) {
        String oid = old.getId();

        old.updateFrom(measurement);
        if (!oid.equals(old.getId())) {
          this.objectsById.remove(oid);
          this.objectsById.put(old.getId(), old);
        }
      } else {
        this.measurements.put(measurement.getName(), measurement);
        this.objectsById.put(measurement.getId(), measurement);
      }
    }
  }

  /**
   * A bean for login details.
   */
  private static class Login extends JSONSerialisable {
    @JsonProperty
    public String name;
    @JsonProperty
    public String password;

    private Login(String name, String password) {
      super();
      this.name = name;
      this.password = password;
    }
    
    @SuppressWarnings("unused")
    public Login() {
    }
  }

  /**
   * A bean for holding the login response.
   */
  private static class LoginResponse extends JSONSerialisable {
    @JsonProperty
    public SDBUser user;
    @JsonProperty
    public List<SDBExperiment> experiments;
    @JsonProperty
    public List<SDBNode> nodes;
    @JsonProperty
    public List<SDBStream> streams;
    
    @SuppressWarnings("unused")
    public LoginResponse() {
    }
  }

  /**
   * Find a bound object for deserialisation.
   * 
   * @param id The object id
   * 
   * @return The bound object or null for not found
   * 
   * @see au.csiro.cmar.weru.SDBContext#findBinding(java.lang.Object)
   */
  @Override
  public SDBObject findBinding(Object id) {
    return this.objectsById.get(id);
  }
}
