
package ee.ttu.thesis.model.stagemonitor;

import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "os",
    "osFamily",
    "osVersion",
    "browser",
    "browserVersion",
    "type",
    "device"
})
public class UserAgent {

    @JsonProperty("os")
    private String os;
    @JsonProperty("osFamily")
    private String osFamily;
    @JsonProperty("osVersion")
    private String osVersion;
    @JsonProperty("browser")
    private String browser;
    @JsonProperty("browserVersion")
    private String browserVersion;
    @JsonProperty("type")
    private String type;
    @JsonProperty("device")
    private String device;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The os
     */
    @JsonProperty("os")
    public String getOs() {
        return os;
    }

    /**
     * 
     * @param os
     *     The os
     */
    @JsonProperty("os")
    public void setOs(String os) {
        this.os = os;
    }

    /**
     * 
     * @return
     *     The osFamily
     */
    @JsonProperty("osFamily")
    public String getOsFamily() {
        return osFamily;
    }

    /**
     * 
     * @param osFamily
     *     The osFamily
     */
    @JsonProperty("osFamily")
    public void setOsFamily(String osFamily) {
        this.osFamily = osFamily;
    }

    /**
     * 
     * @return
     *     The osVersion
     */
    @JsonProperty("osVersion")
    public String getOsVersion() {
        return osVersion;
    }

    /**
     * 
     * @param osVersion
     *     The osVersion
     */
    @JsonProperty("osVersion")
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    /**
     * 
     * @return
     *     The browser
     */
    @JsonProperty("browser")
    public String getBrowser() {
        return browser;
    }

    /**
     * 
     * @param browser
     *     The browser
     */
    @JsonProperty("browser")
    public void setBrowser(String browser) {
        this.browser = browser;
    }

    /**
     * 
     * @return
     *     The browserVersion
     */
    @JsonProperty("browserVersion")
    public String getBrowserVersion() {
        return browserVersion;
    }

    /**
     * 
     * @param browserVersion
     *     The browserVersion
     */
    @JsonProperty("browserVersion")
    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    /**
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The device
     */
    @JsonProperty("device")
    public String getDevice() {
        return device;
    }

    /**
     * 
     * @param device
     *     The device
     */
    @JsonProperty("device")
    public void setDevice(String device) {
        this.device = device;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
