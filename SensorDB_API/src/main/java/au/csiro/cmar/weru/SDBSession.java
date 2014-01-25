package au.csiro.cmar.weru;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class SDBSession {
  /** The base host for server access */
	private URL host;
	/** The current user */
	private SDBUser user;
	/** The session cookie */
	private String cookie;
	
  /**
   * Construct a session for a specific host.
   * @param host
   */
  public SDBSession(URL host) {
    super();
    this.host = host;
  }

  /**
   * Get the host.
   *
   * @return the host
   */
  public URL getHost() {
    return this.host;
  }

  /**
   * Set the host.
   *
   * @param host the new host
   */
  public void setHost(URL host) {
    this.host = host;
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
   * Get the session cookie.
   *
   * @return the cookie
   */
  public String getCookie() {
    return this.cookie;
  }

  /**
   * Set the session cookie.
   *
   * @param cookie the new cookie
   */
  public void setCookie(String cookie) {
    this.cookie = cookie;
  }
	
  /**
   * Perform a post to the server.
   * <p>
   * Any cookies returned become the session cookie.
   * 
   * @param path The relative path from the host (should begin with a /)
   * @param request The object to post
   * @param responseClass The type of expected return (null for none)
   * @param context The context for response parsing
   * 
   * @return The resulting response for the server or null for no response
   * 
   * @throws SDBException if unable to perform the post correctly
   */
  public <T extends JSONSerialisable> T post(String path, Object request, Class<T> responseClass, SDBContext context) throws SDBException {
    InputStream is = null;
    String content = request == null ? "" : JSONSerialisable.toJson(request);
    T response = null;
    HttpURLConnection conn = null;
    
    try {
      URL url = new URL(this.host, path);
      conn = (HttpURLConnection) url.openConnection();
      
      conn.setRequestMethod("POST");
      conn.setDoOutput(true);
      conn.setUseCaches(false);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Accept", "application/json");
      if (this.cookie != null)
        conn.setRequestProperty("Cookie", this.cookie);
      conn.setRequestProperty("Content-Length", Integer.toString(content.length()));
      conn.getOutputStream().write(content.getBytes());
      conn.getOutputStream().flush();
      if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
        throw new SDBException("POST method failed: " + conn.getResponseCode() + "/" + conn.getResponseMessage());
      if (conn.getHeaderField("Set-Cookie") != null)
        this.cookie = conn.getHeaderField("Set-Cookie");
     if (responseClass == null)
        return null;
      is = conn.getInputStream();
      response = JSONSerialisable.load(is, responseClass, context);
      return response;
    } catch (IOException| URISyntaxException ex) {
      throw new SDBException("Unable to access " + path + " with content " + request, ex);
    } finally {
      if (is != null)
        try {
          is.close();
        } catch (IOException ex) {
          // Just give up!
        }      
    }
  }
  
  
  /**
   * Perform a post to the server and get a JSON tree in response.
   * <p>
   * Any cookies returned become the session cookie.
   * 
   * @param path The relative path from the host (should begin with a /)
   * @param request The object to post
   * @param context The context for response parsing
   * 
   * @return The resulting response from the server as a JSON tree or null for no response
   * 
   * @throws SDBException if unable to perform the post correctly
   */
  public JsonNode postTree(String path, Object request, SDBContext context) throws SDBException {
    InputStream is = null;
    String content = request == null ? "" : JSONSerialisable.toJson(request);
    JsonNode response = null;
    HttpURLConnection conn = null;
    
    try {
      URL url = new URL(this.host, path);
      conn = (HttpURLConnection) url.openConnection();
      
      conn.setRequestMethod("POST");
      conn.setDoOutput(true);
      conn.setUseCaches(false);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Accept", "application/json");
      if (this.cookie != null)
        conn.setRequestProperty("Cookie", this.cookie);
      conn.setRequestProperty("Content-Length", Integer.toString(content.length()));
      conn.getOutputStream().write(content.getBytes());
      conn.getOutputStream().flush();
      if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
        throw new SDBException("POST method failed: " + conn.getResponseCode() + "/" + conn.getResponseMessage());
      if (conn.getHeaderField("Set-Cookie") != null)
        this.cookie = conn.getHeaderField("Set-Cookie");
      is = conn.getInputStream();
      response = JSONSerialisable.loadTree(is, context);
      return response;
    } catch (IOException| URISyntaxException ex) {
      throw new SDBException("Unable to access " + path + " with content " + request, ex);
    } finally {
      if (is != null)
        try {
          is.close();
        } catch (IOException ex) {
          // Just give up!
        }      
    }
  }
  
  /**
   * Perform a get from the server.
   * <p>
   * Any cookies returned become the session cookie.
   * 
   * @param path The relative path from the host (should begin with a /)
   * @param responseClass The type of expected return (null for none)
   * @param context The context for response parsing
   * 
   * @return The resulting response for the server or null for no response
   * 
   * @throws SDBException if unable to perform the post correctly
   */
  public <T extends JSONSerialisable> T get(String path, Class<T> responseClass, SDBContext context) throws SDBException {
    InputStream is = null;
    T response = null;
    HttpURLConnection conn = null;
    
    try {
      URL url = new URL(this.host, path);
      conn = (HttpURLConnection) url.openConnection();
      
      conn.setRequestMethod("GET");
      conn.setDoOutput(true);
      conn.setUseCaches(false);
      conn.setRequestProperty("Content-length", "0");
      conn.setUseCaches(false);
      conn.setAllowUserInteraction(false);
      conn.setConnectTimeout(30000);
      conn.setReadTimeout(30000);
      conn.setRequestProperty("Accept", "application/json");
      if (this.cookie != null)
        conn.setRequestProperty("Cookie", this.cookie);
      conn.connect();
      if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
        throw new SDBException("GET method failed: " + conn.getResponseCode() + "/" + conn.getResponseMessage());
      if (responseClass == null)
        return null;
      is = conn.getInputStream();
      response = JSONSerialisable.load(is, responseClass, context);
      return response;
    } catch (IOException| URISyntaxException ex) {
      throw new SDBException("Unable to access " + path, ex);
    } finally {
      if (is != null)
        try {
          is.close();
        } catch (IOException ex) {
          // Just give up!
        }      
    }
  }
  
  /**
   * Perform a get with a list response from the server.
   * <p>
   * Any cookies returned become the session cookie.
   * 
   * @param path The relative path from the host (should begin with a /)
   * @param responseClass The type of expected return (null for none)
   * @param context The context for response parsing
   * 
   * @return The resulting response for the server or null for no response
   * 
   * @throws SDBException if unable to perform the post correctly
   */
  public <T extends JSONSerialisable> List<T> getList(String path, Class<T> responseClass, SDBContext context) throws SDBException {
    InputStream is = null;
    List<T> response = null;
    HttpURLConnection conn = null;
    
    try {
      URL url = new URL(this.host, path);
      conn = (HttpURLConnection) url.openConnection();
      
      conn.setRequestMethod("GET");
      conn.setDoOutput(true);
      conn.setUseCaches(false);
      conn.setRequestProperty("Content-length", "0");
      conn.setUseCaches(false);
      conn.setAllowUserInteraction(false);
      conn.setConnectTimeout(30000);
      conn.setReadTimeout(30000);
      conn.setRequestProperty("Accept", "application/json");
      if (this.cookie != null)
        conn.setRequestProperty("Cookie", this.cookie);
      conn.connect();
      if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
        throw new SDBException("GET method failed: " + conn.getResponseCode() + "/" + conn.getResponseMessage());
      if (responseClass == null)
        return null;
      is = conn.getInputStream();
      response = JSONSerialisable.loadList(is, responseClass, context);
      return response;
    } catch (IOException| URISyntaxException ex) {
      throw new SDBException("Unable to access " + path, ex);
    } finally {
      if (is != null)
        try {
          is.close();
        } catch (IOException ex) {
          // Just give up!
        }      
    }
  }
  
  /**
   * Perform a delete on the server.
   * 
   * @param path The relative path from the host (should begin with a /)
   * @param context The context for response parsing
   * 
   * @return The resulting response for the server or null for no response
   * 
   * @throws SDBException if unable to perform the post correctly
   */
  public void delete(String path, SDBContext context) throws SDBException {
    HttpURLConnection conn = null;
    
    try {
      URL url = new URL(this.host, path);
      conn = (HttpURLConnection) url.openConnection();
      
      conn.setRequestMethod("DELETE");
      conn.setDoOutput(true);
      conn.setUseCaches(false);
      conn.setRequestProperty("Content-length", "0");
      conn.setUseCaches(false);
      conn.setAllowUserInteraction(false);
      conn.setConnectTimeout(30000);
      conn.setReadTimeout(30000);
      if (this.cookie != null)
        conn.setRequestProperty("Cookie", this.cookie);
      conn.connect();
      if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
        throw new SDBException("DELETE method failed: " + conn.getResponseCode() + "/" + conn.getResponseMessage());
    } catch (IOException ex) {
      throw new SDBException("Unable to access " + path, ex);
    }
  }
}
