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
@JsonPropertyOrder({ "event", "signature_request" })
@Generated("jsonschema2pojo")
public class HelloSignEvent {

    @JsonProperty("event")
    private Event event;

    @JsonProperty("signature_request")
    private SignatureRequest signatureRequest;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("event")
    public Event getEvent() {
        return event;
    }

    @JsonProperty("event")
    public void setEvent(Event event) {
        this.event = event;
    }

    @JsonProperty("signature_request")
    public SignatureRequest getSignatureRequest() {
        return signatureRequest;
    }

    @JsonProperty("signature_request")
    public void setSignatureRequest(SignatureRequest signatureRequest) {
        this.signatureRequest = signatureRequest;
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
