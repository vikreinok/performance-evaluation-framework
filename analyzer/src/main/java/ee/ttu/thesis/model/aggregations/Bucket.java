
package ee.ttu.thesis.model.aggregations;

import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "key",
    "doc_count",
    "group_by_request_id"
})
public class Bucket {

    @JsonProperty("key")
    private String key;
    @JsonProperty("doc_count")
    private Long docCount;
    @JsonProperty("group_by_request_id")
    private GroupByRequestId groupByRequestId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The key
     */
    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    /**
     * 
     * @param key
     *     The key
     */
    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 
     * @return
     *     The docCount
     */
    @JsonProperty("doc_count")
    public Long getDocCount() {
        return docCount;
    }

    /**
     * 
     * @param docCount
     *     The doc_count
     */
    @JsonProperty("doc_count")
    public void setDocCount(Long docCount) {
        this.docCount = docCount;
    }

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
     *     The group_by_request_id
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
