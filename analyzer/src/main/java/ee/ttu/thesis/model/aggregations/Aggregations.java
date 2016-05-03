
package ee.ttu.thesis.model.aggregations;

import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "group_by_request_id"
})
public class Aggregations {

    @JsonProperty("group_by_request_id")
    private GroupByRequestId groupByRequestId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The groupByRequestId
     */
    @JsonProperty("group_by_request_id")
    public GroupByRequestId getGroupByRequestId() {
        return groupByRequestId;
    }

    /**
     * 
     * @param groupByRequestId
     *     The group_by_method
     */
    @JsonProperty("group_by_request_id")
    public void setGroupByRequestId(GroupByRequestId groupByRequestId) {
        this.groupByRequestId = groupByRequestId;
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
