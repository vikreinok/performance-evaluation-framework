
package ee.ttu.thesis.model.aggregations;

import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "took",
    "timed_out",
    "_shards",
    "hits",
    "aggregations"
})
public class Aggregation {

    @JsonProperty("took")
    private Long took;
    @JsonProperty("timed_out")
    private Boolean timedOut;
    @JsonProperty("_shards")
    private ee.ttu.thesis.model.aggregations.Shards Shards;
    @JsonProperty("hits")
    private Hits hits;
    @JsonProperty("aggregations")
    private Aggregations aggregations;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The took
     */
    @JsonProperty("took")
    public Long getTook() {
        return took;
    }

    /**
     * 
     * @param took
     *     The took
     */
    @JsonProperty("took")
    public void setTook(Long took) {
        this.took = took;
    }

    /**
     * 
     * @return
     *     The timedOut
     */
    @JsonProperty("timed_out")
    public Boolean getTimedOut() {
        return timedOut;
    }

    /**
     * 
     * @param timedOut
     *     The timed_out
     */
    @JsonProperty("timed_out")
    public void setTimedOut(Boolean timedOut) {
        this.timedOut = timedOut;
    }

    /**
     * 
     * @return
     *     The Shards
     */
    @JsonProperty("_shards")
    public ee.ttu.thesis.model.aggregations.Shards getShards() {
        return Shards;
    }

    /**
     * 
     * @param Shards
     *     The _shards
     */
    @JsonProperty("_shards")
    public void setShards(ee.ttu.thesis.model.aggregations.Shards Shards) {
        this.Shards = Shards;
    }

    /**
     * 
     * @return
     *     The hits
     */
    @JsonProperty("hits")
    public Hits getHits() {
        return hits;
    }

    /**
     * 
     * @param hits
     *     The hits
     */
    @JsonProperty("hits")
    public void setHits(Hits hits) {
        this.hits = hits;
    }

    /**
     * 
     * @return
     *     The aggregations
     */
    @JsonProperty("aggregations")
    public Aggregations getAggregations() {
        return aggregations;
    }

    /**
     * 
     * @param aggregations
     *     The aggregations
     */
    @JsonProperty("aggregations")
    public void setAggregations(Aggregations aggregations) {
        this.aggregations = aggregations;
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
