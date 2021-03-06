
package ee.ttu.thesis.model.aggregations;

import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "group_by_modification_id"
})
public class Aggregations {

    @JsonProperty("group_by_modification_id")
    private GroupByModificationId groupByModificationId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The groupByModificationId
     */
    @JsonProperty("group_by_modification_id")
    public GroupByModificationId getGroupByModificationId() {
        return groupByModificationId;
    }

    /**
     * 
     * @param groupByModificationId
     *     The group_by_modification_id
     */
    @JsonProperty("group_by_modification_id")
    public void setGroupByModificationId(GroupByModificationId groupByModificationId) {
        this.groupByModificationId = groupByModificationId;
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
