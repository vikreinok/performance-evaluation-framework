
package ee.ttu.thesis.model.aggregations;

import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "doc_count_error_upper_bound",
    "sum_other_doc_count",
    "buckets"
})
public class GroupByRequestId {

    @JsonProperty("doc_count_error_upper_bound")
    private Long docCountErrorUpperBound;
    @JsonProperty("sum_other_doc_count")
    private Long sumOtherDocCount;
    @JsonProperty("buckets")
    private List<Bucket> buckets = new ArrayList<Bucket>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The docCountErrorUpperBound
     */
    @JsonProperty("doc_count_error_upper_bound")
    public Long getDocCountErrorUpperBound() {
        return docCountErrorUpperBound;
    }

    /**
     * 
     * @param docCountErrorUpperBound
     *     The doc_count_error_upper_bound
     */
    @JsonProperty("doc_count_error_upper_bound")
    public void setDocCountErrorUpperBound(Long docCountErrorUpperBound) {
        this.docCountErrorUpperBound = docCountErrorUpperBound;
    }

    /**
     * 
     * @return
     *     The sumOtherDocCount
     */
    @JsonProperty("sum_other_doc_count")
    public Long getSumOtherDocCount() {
        return sumOtherDocCount;
    }

    /**
     * 
     * @param sumOtherDocCount
     *     The sum_other_doc_count
     */
    @JsonProperty("sum_other_doc_count")
    public void setSumOtherDocCount(Long sumOtherDocCount) {
        this.sumOtherDocCount = sumOtherDocCount;
    }

    /**
     * 
     * @return
     *     The buckets
     */
    @JsonProperty("buckets")
    public List<Bucket> getBuckets() {
        return buckets;
    }

    /**
     * 
     * @param buckets
     *     The buckets
     */
    @JsonProperty("buckets")
    public void setBuckets(List<Bucket> buckets) {
        this.buckets = buckets;
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
