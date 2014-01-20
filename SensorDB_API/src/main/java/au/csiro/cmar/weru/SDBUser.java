package au.csiro.cmar.weru;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SDBUser extends SDBObject {
  /** Is this user currently active? */
	private boolean active;
	/** The password */
	@JsonProperty
	private String password;
	/** The email address */
	@JsonProperty
	private String email;
	
  /**
   * Construct an empty user.
   */
	public SDBUser() {
	}

  /**
   * Is this user active?
   *
   * @return the active flag
   */
  public boolean isActive() {
    return this.active;
  }

  /**
   * Set the active flag.
   *
   * @param active the new active flag
   */
  public void setActive(boolean active) {
    this.active = active;
  }
	
  /**
   * Get the active flag as an integer
   * 
   * @return 1 if active, 0 if inactive
   */
  @JsonProperty("active")
  public int getActiveInt() {
    return this.isActive() ? 1 : 0;
  }
  
  /**
   * Set the active flag from an integer.
   * 
   * @param active Non-zero for active, zero for inactive
   */
  @JsonProperty("active")
  public void setActiveInt(int active) {
    this.setActive(active != 0);
  }

  /**
   * Get the password.
   *
   * @return the password
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * Set the password.
   *
   * @param password the new password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Get the email.
   *
   * @return the email
   */
  public String getEmail() {
    return this.email;
  }

  /**
   * Set the email.
   *
   * @param email the new email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
    * Update this user from a new user.
   * 
   * @see au.csiro.cmar.weru.SDBObject#updateFrom(au.csiro.cmar.weru.SDBObject)
   */
  public void updateFrom(SDBUser update) {
    super.updateFrom(update);
    this.active = update.active;
    this.password = update.password;
    this.email = update.email;
  } 

}
