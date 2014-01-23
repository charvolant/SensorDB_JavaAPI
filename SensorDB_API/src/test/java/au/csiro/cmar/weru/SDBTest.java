/*
 * $Id$
 *
 * Copyright (c) 2014 CSIRO
 *
 * Created 16 Jan 2014
 */
package au.csiro.cmar.weru;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.fail;

import java.io.InputStreamReader;
import java.io.StringWriter;

import org.junit.Rule;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

/**
 * Common methods for testing SDB.
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright 2014 CSIRO
 *
 */
public class SDBTest {
  protected static final int PORT = 8909;
  protected static final String COOKIE = "LovelyCookie";
  protected static final String EXPERIMENT1 = "Test1";
  protected static final String MEASUREMENT1 = "Celsius";
  protected static final String USER1 = "fieldprime_test";
  protected static final String WEBSITE1 = "http://localhost/nowhere";
  protected static final String WEBSITE2 = "http://en.wikipedia.org/wiki/Celsius";

  @Rule
  public WireMockRule wmr = new WireMockRule(this.PORT);

  /**
   * Construct a SDBTest.
   */
  public SDBTest() {
    super();
  }

  /**
   * Load a resource as a string.
   * 
   * @param name The resource name, local to the current package
   * 
   * @return The resource as a string
   */
  protected String loadResource(String name) {
    try {
      StringWriter writer = new StringWriter(4096);
      InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream(name));
      char[] buffer = new char[1024];
      int n;
      
      while ((n = reader.read(buffer)) > 0)
        writer.write(buffer, 0, n);
      reader.close();
      writer.close();
      return writer.toString();
    } catch (Exception ex) {
      fail("Unable to load resource " + name + " " + ex.getMessage());
      return null;
    }
  }

  /**
   * Create a mock sensordb server to interact with.
   * 
   * @throws java.lang.Exception
   */
  public void createServer() throws Exception {
    stubFor(get(urlEqualTo("/users"))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody(this.loadResource("users1.json"))
            )
        );
    stubFor(get(urlEqualTo("/measurements"))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody(this.loadResource("measurements1.json"))
            )
        );
    stubFor(post(urlEqualTo("/login"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Set-Cookie", this.COOKIE)
            .withBody(this.loadResource("login1.json"))
            )
        );
    stubFor(post(urlEqualTo("/logout"))
        .withHeader("Cookie", equalTo(this.COOKIE))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody("{}")
            )
        );
    stubFor(post(urlEqualTo("/data"))
        .withHeader("Cookie", equalTo(this.COOKIE))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody("{}")
            )
        );
    stubFor(post(urlEqualTo("/data_download"))
        .withHeader("Cookie", equalTo(this.COOKIE))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody(this.loadResource("data1.json"))
            )
        );
  }
}