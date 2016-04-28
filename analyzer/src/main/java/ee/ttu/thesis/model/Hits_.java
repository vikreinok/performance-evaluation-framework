
package ee.ttu.thesis.model;

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
    "total",
    "max_score",
    "hits"
})
public class Hits_ {

    @JsonProperty("total")
    private Integer total;
    @JsonProperty("max_score")
    private Double maxScore;
    @JsonProperty("hits")
    private List<Hit> hits = new ArrayList<Hit>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The total
     */
    @JsonProperty("total")
    public Integer getTotal() {
        return total;
    }

    /**
     * 
     * @param total
     *     The total
     */
    @JsonProperty("total")
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * 
     * @return
     *     The maxScore
     */
    @JsonProperty("max_score")
    public Double getMaxScore() {
        return maxScore;
    }

    /**
     * 
     * @param maxScore
     *     The max_score
     */
    @JsonProperty("max_score")
    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }

    /**
     * 
     * @return
     *     The hits
     */
    @JsonProperty("hits")
    public List<Hit> getHits() {
        return hits;
    }

    /**
     * 
     * @param hits
     *     The hits
     */
    @JsonProperty("hits")
    public void setHits(List<Hit> hits) {
        this.hits = hits;
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
