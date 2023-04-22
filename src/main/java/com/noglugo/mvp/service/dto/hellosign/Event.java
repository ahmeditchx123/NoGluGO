package com.noglugo.mvp.service.dto.hellosign;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "event_time", "event_type", "event_hash", "event_metadata" })
@Generated("jsonschema2pojo")
public class Event {

    @JsonProperty("event_time")
    private String eventTime;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("event_hash")
    private String eventHash;

    @JsonProperty("event_metadata")
    private EventMetadata eventMetadata;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("event_time")
    public String getEventTime() {
        return eventTime;
    }

    @JsonProperty("event_time")
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    @JsonProperty("event_type")
    public String getEventType() {
        return eventType;
    }

    @JsonProperty("event_type")
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @JsonProperty("event_hash")
    public String getEventHash() {
        return eventHash;
    }

    @JsonProperty("event_hash")
    public void setEventHash(String eventHash) {
        this.eventHash = eventHash;
    }

    @JsonProperty("event_metadata")
    public EventMetadata getEventMetadata() {
        return eventMetadata;
    }

    @JsonProperty("event_metadata")
    public void setEventMetadata(EventMetadata eventMetadata) {
        this.eventMetadata = eventMetadata;
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
