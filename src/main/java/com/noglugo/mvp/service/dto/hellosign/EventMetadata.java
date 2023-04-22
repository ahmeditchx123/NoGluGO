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
@JsonPropertyOrder({ "related_signature_id", "reported_for_account_id", "reported_for_app_id" })
@Generated("jsonschema2pojo")
public class EventMetadata {

    @JsonProperty("related_signature_id")
    private String relatedSignatureId;

    @JsonProperty("reported_for_account_id")
    private String reportedForAccountId;

    @JsonProperty("reported_for_app_id")
    private Object reportedForAppId;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("related_signature_id")
    public String getRelatedSignatureId() {
        return relatedSignatureId;
    }

    @JsonProperty("related_signature_id")
    public void setRelatedSignatureId(String relatedSignatureId) {
        this.relatedSignatureId = relatedSignatureId;
    }

    @JsonProperty("reported_for_account_id")
    public String getReportedForAccountId() {
        return reportedForAccountId;
    }

    @JsonProperty("reported_for_account_id")
    public void setReportedForAccountId(String reportedForAccountId) {
        this.reportedForAccountId = reportedForAccountId;
    }

    @JsonProperty("reported_for_app_id")
    public Object getReportedForAppId() {
        return reportedForAppId;
    }

    @JsonProperty("reported_for_app_id")
    public void setReportedForAppId(Object reportedForAppId) {
        this.reportedForAppId = reportedForAppId;
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
