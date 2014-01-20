package au.csiro.cmar.weru;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import au.com.bytecode.opencsv.CSVReader;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SDBStream extends SDBObject {
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

  @SuppressWarnings("unchecked")
	public boolean postData(SDBSession session, String filename, int column)
			throws IOException, ParseException, java.text.ParseException {

		CSVReader reader = new CSVReader(new FileReader(filename));
		List<String[]> dataList = reader.readAll();
		// Remove headers
		dataList.remove(0);

		int i = 0;

		while ((i * 60) < dataList.size()) {

			
			URL url = new URL(session.getHost() + "/data");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Cookie", session.getCookie());
			
			JSONObject data = new JSONObject();
			Date dataDate;
			for (int j = 0; j < 5000 && ((i * 60) < dataList.size()); j++) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat dateFormatT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				dataDate = dateFormat.parse(dataList.get(i * 60)[0]);
				data.put(dateFormatT.format(dataDate), dataList.get(i * 60)[column]);
				System.out.println(dateFormatT.format(dataDate));
				i++;
			}
			
			JSONObject object = new JSONObject();
			object.put(token, data);
		
			conn.setRequestProperty("Content-Length",
					Integer.toString(object.toString().length()));
			conn.getOutputStream().write(object.toString().getBytes());
			conn.getOutputStream().flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {

				System.out.println("POST method failed: "
						+ conn.getResponseCode() + "\t"
						+ conn.getResponseMessage());
				return false;

			} else
				conn.disconnect();
		}

		return true;
	}

	// Start date may be specified. If it is not the earliest date is used.
	// End date may be specified. If it is not the latest date is used.
	// Optionally the aggregation level may be specified as one of the
	// following:
	// raw, 1-minute, 5-minute, 15-minute, 1-hour, 3-hour, 6-hour, 1-day,
	// 1-month, 1-year
	@SuppressWarnings("unchecked")
	public boolean getData(SDBSession session, Calendar start_date,
			Calendar end_date, String agregation_level) throws IOException,
			ParseException {
		
		URL url = new URL(session.getHost() + "/data_download");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Cookie", session.getCookie());
		
		JSONObject obj = new JSONObject();

		if (agregation_level != null)
			obj.put("level", agregation_level);
		else
			obj.put("level", "raw");
		
		if (start_date != null)
			obj.put("sd", start_date.get(Calendar.YEAR) + "-" + formatValue(start_date.get(Calendar.MONTH)+1,2) + "-" + formatValue(start_date.get(Calendar.DATE),2));

		if (end_date != null)
			obj.put("ed", end_date.get(Calendar.YEAR) + "-" + formatValue(end_date.get(Calendar.MONTH)+1,2) + "-" + formatValue(end_date.get(Calendar.DATE),2));
		
		obj.put("sid", this.getId());
		
		System.out.println(obj.toJSONString());
		
		
		conn.setRequestProperty("Content-Length",
				Integer.toString(obj.toString().length()));
		conn.getOutputStream().write(obj.toString().getBytes());
		conn.getOutputStream().flush();

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {

			System.out.println("POST method failed: " + conn.getResponseCode()
					+ "\t" + conn.getResponseMessage());
			return false;

		} else {

			JSONParser parser = new JSONParser();
			BufferedReader bin = new BufferedReader(new InputStreamReader(
					new DataInputStream(conn.getInputStream())));
			JSONObject jsonObject = (JSONObject) parser.parse(bin);
			bin.close();
			
			System.out.println(jsonObject.toJSONString());
			
			return true;
		}
	}
	
	// Returns a string containing value with a fixed number of characters
	// determined by positions
	// Fills with "0" on the left side of the value
	public static String formatValue(int value, int positions) {
		StringBuilder sbValue = new StringBuilder(Integer.toString(value));

		while (sbValue.length() < positions) {
			sbValue.insert(0, "0");
		}

		return sbValue.toString();
	}

}
