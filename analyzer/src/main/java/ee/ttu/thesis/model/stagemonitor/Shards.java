
package ee.ttu.thesis.model.stagemonitor;

import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "total",
    "successful",
    "failed"
})
public class Shards {

    @JsonProperty("total")
    private Long total;
    @JsonProperty("successful")
    private Long successful;
    @JsonProperty("failed")
    private Long failed;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The total
     */
    @JsonProperty("total")
    public Long getTotal() {
        return total;
    }

    /**
     * 
     * @param total
     *     The total
     */
    @JsonProperty("total")
    public void setTotal(Long total) {
        this.total = total;
    }

    /**
     * 
     * @return
     *     The successful
     */
    @JsonProperty("successful")
    public Long getSuccessful() {
        return successful;
    }

    /**
     * 
     * @param successful
     *     The successful
     */
    @JsonProperty("successful")
    public void setSuccessful(Long successful) {
        this.successful = successful;
    }

    /**
     * 
     * @return
     *     The failed
     */
    @JsonProperty("failed")
    public Long getFailed() {
        return failed;
    }

    /**
     * 
     * @param failed
     *     The failed
     */
    @JsonProperty("failed")
    public void setFailed(Long failed) {
        this.failed = failed;
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
