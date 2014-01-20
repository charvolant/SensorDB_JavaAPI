package au.csiro.cmar.weru;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SensorDB implements SDBContext {

  public SDBSession session;
  public Map<String, SDBMeasurement> measurements;
  public Map<String, SDBUser> users;
  public Map<String, SDBExperiment> experiments;
  public Map<Object, SDBObject> objectsById;

  // Minimal constructor for SensorDB
  // Just can access public services as getMeasurements() and getUsers()
  public SensorDB(String host) throws SDBException {
    this.session = new SDBSession(host);
    this.users = new HashMap<String, SDBUser>(); 
    this.measurements = new HashMap<String, SDBMeasurement>(); 
    this.experiments = new HashMap<String, SDBExperiment>();
    this.objectsById = new HashMap<Object, SDBObject>();
    this.retrieveUsers();
    this.retrieveMeasurements();
  }

  public SensorDB(String host, String user, String password)
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

  public SDBUser getUser(String name) {
    return this.users.get(name);
  }

  // TO BE IMPLEMENTED IN SERVER
  public SDBMeasurement createMeasurement(String name, String description, String website) throws SDBException {
    throw new SDBException("Not yet implemented");
  }

  // TO BE IMPLEMENTED IN SERVER
  public void deleteMeasurement(String name) throws SDBException {
    throw new SDBException("Not yet implemented");
  }

  public SDBMeasurement getMeasurement(String name) {
    return this.measurements.get(name);
  }

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

  public SDBExperiment getExperiment(String name) throws IOException, ParseException {
    return this.experiments.get(name);
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
