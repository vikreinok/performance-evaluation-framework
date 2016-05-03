
package ee.ttu.thesis.model.stagemonitor;

import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "_index",
    "_type",
    "_id",
    "_score",
    "_source"
})
public class Hit {

    @JsonProperty("_index")
    private String Index;
    @JsonProperty("_type")
    private String Type;
    @JsonProperty("_id")
    private String Id;
    @JsonProperty("_score")
    private Double Score;
    @JsonProperty("_source")
    private ee.ttu.thesis.model.stagemonitor.Source Source;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The Index
     */
    @JsonProperty("_index")
    public String getIndex() {
        return Index;
    }

    /**
     * 
     * @param Index
     *     The _index
     */
    @JsonProperty("_index")
    public void setIndex(String Index) {
        this.Index = Index;
    }

    /**
     * 
     * @return
     *     The Type
     */
    @JsonProperty("_type")
    public String getType() {
        return Type;
    }

    /**
     * 
     * @param Type
     *     The _type
     */
    @JsonProperty("_type")
    public void setType(String Type) {
        this.Type = Type;
    }

    /**
     * 
     * @return
     *     The Id
     */
    @JsonProperty("_id")
    public String getId() {
        return Id;
    }

    /**
     * 
     * @param Id
     *     The _id
     */
    @JsonProperty("_id")
    public void setId(String Id) {
        this.Id = Id;
    }

    /**
     * 
     * @return
     *     The Score
     */
    @JsonProperty("_score")
    public Double getScore() {
        return Score;
    }

    /**
     * 
     * @param Score
     *     The _score
     */
    @JsonProperty("_score")
    public void setScore(Double Score) {
        this.Score = Score;
    }

    /**
     * 
     * @return
     *     The Source
     */
    @JsonProperty("_source")
    public ee.ttu.thesis.model.stagemonitor.Source getSource() {
        return Source;
    }

    /**
     * 
     * @param Source
     *     The _source
     */
    @JsonProperty("_source")
    public void setSource(ee.ttu.thesis.model.stagemonitor.Source Source) {
        this.Source = Source;
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
