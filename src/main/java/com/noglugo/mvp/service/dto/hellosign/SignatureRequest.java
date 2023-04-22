package com.noglugo.mvp.service.dto.hellosign;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    {
        "signature_request_id",
        "title",
        "original_title",
        "subject",
        "message",
        "metadata",
        "created_at",
        "is_complete",
        "is_declined",
        "has_error",
        "custom_fields",
        "response_data",
        "signing_url",
        "signing_redirect_url",
        "files_url",
        "details_url",
        "requester_email_address",
        "signatures",
        "cc_email_addresses",
        "template_ids",
        "client_id",
    }
)
@Generated("jsonschema2pojo")
public class SignatureRequest {

    @JsonProperty("signature_request_id")
    private String signatureRequestId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("original_title")
    private String originalTitle;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("message")
    private String message;

    @JsonProperty("metadata")
    private Metadata metadata;

    @JsonProperty("created_at")
    private Integer createdAt;

    @JsonProperty("is_complete")
    private Boolean isComplete;

    @JsonProperty("is_declined")
    private Boolean isDeclined;

    @JsonProperty("has_error")
    private Boolean hasError;

    @JsonProperty("custom_fields")
    private List<Object> customFields;

    @JsonProperty("response_data")
    private List<Object> responseData;

    @JsonProperty("signing_url")
    private Object signingUrl;

    @JsonProperty("signing_redirect_url")
    private Object signingRedirectUrl;

    @JsonProperty("files_url")
    private String filesUrl;

    @JsonProperty("details_url")
    private String detailsUrl;

    @JsonProperty("requester_email_address")
    private String requesterEmailAddress;

    @JsonProperty("signatures")
    private List<Object> signatures;

    @JsonProperty("cc_email_addresses")
    private List<Object> ccEmailAddresses;

    @JsonProperty("template_ids")
    private Object templateIds;

    @JsonProperty("client_id")
    private Object clientId;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("signature_request_id")
    public String getSignatureRequestId() {
        return signatureRequestId;
    }

    @JsonProperty("signature_request_id")
    public void setSignatureRequestId(String signatureRequestId) {
        this.signatureRequestId = signatureRequestId;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("original_title")
    public String getOriginalTitle() {
        return originalTitle;
    }

    @JsonProperty("original_title")
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    @JsonProperty("subject")
    public String getSubject() {
        return subject;
    }

    @JsonProperty("subject")
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("metadata")
    public Metadata getMetadata() {
        return metadata;
    }

    @JsonProperty("metadata")
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @JsonProperty("created_at")
    public Integer getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("is_complete")
    public Boolean getIsComplete() {
        return isComplete;
    }

    @JsonProperty("is_complete")
    public void setIsComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }

    @JsonProperty("is_declined")
    public Boolean getIsDeclined() {
        return isDeclined;
    }

    @JsonProperty("is_declined")
    public void setIsDeclined(Boolean isDeclined) {
        this.isDeclined = isDeclined;
    }

    @JsonProperty("has_error")
    public Boolean getHasError() {
        return hasError;
    }

    @JsonProperty("has_error")
    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    @JsonProperty("custom_fields")
    public List<Object> getCustomFields() {
        return customFields;
    }

    @JsonProperty("custom_fields")
    public void setCustomFields(List<Object> customFields) {
        this.customFields = customFields;
    }

    @JsonProperty("response_data")
    public List<Object> getResponseData() {
        return responseData;
    }

    @JsonProperty("response_data")
    public void setResponseData(List<Object> responseData) {
        this.responseData = responseData;
    }

    @JsonProperty("signing_url")
    public Object getSigningUrl() {
        return signingUrl;
    }

    @JsonProperty("signing_url")
    public void setSigningUrl(Object signingUrl) {
        this.signingUrl = signingUrl;
    }

    @JsonProperty("signing_redirect_url")
    public Object getSigningRedirectUrl() {
        return signingRedirectUrl;
    }

    @JsonProperty("signing_redirect_url")
    public void setSigningRedirectUrl(Object signingRedirectUrl) {
        this.signingRedirectUrl = signingRedirectUrl;
    }

    @JsonProperty("files_url")
    public String getFilesUrl() {
        return filesUrl;
    }

    @JsonProperty("files_url")
    public void setFilesUrl(String filesUrl) {
        this.filesUrl = filesUrl;
    }

    @JsonProperty("details_url")
    public String getDetailsUrl() {
        return detailsUrl;
    }

    @JsonProperty("details_url")
    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    @JsonProperty("requester_email_address")
    public String getRequesterEmailAddress() {
        return requesterEmailAddress;
    }

    @JsonProperty("requester_email_address")
    public void setRequesterEmailAddress(String requesterEmailAddress) {
        this.requesterEmailAddress = requesterEmailAddress;
    }

    @JsonProperty("signatures")
    public List<Object> getSignatures() {
        return signatures;
    }

    @JsonProperty("signatures")
    public void setSignatures(List<Object> signatures) {
        this.signatures = signatures;
    }

    @JsonProperty("cc_email_addresses")
    public List<Object> getCcEmailAddresses() {
        return ccEmailAddresses;
    }

    @JsonProperty("cc_email_addresses")
    public void setCcEmailAddresses(List<Object> ccEmailAddresses) {
        this.ccEmailAddresses = ccEmailAddresses;
    }

    @JsonProperty("template_ids")
    public Object getTemplateIds() {
        return templateIds;
    }

    @JsonProperty("template_ids")
    public void setTemplateIds(Object templateIds) {
        this.templateIds = templateIds;
    }

    @JsonProperty("client_id")
    public Object getClientId() {
        return clientId;
    }

    @JsonProperty("client_id")
    public void setClientId(Object clientId) {
        this.clientId = clientId;
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
