package com.noglugo.mvp.service.dto.twilio;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;

public class OtpVerificationResponse {

    private String sid;
    private String service_sid;
    private String account_sid;

    @JsonProperty("to")
    private String myto;

    private String channel;
    private String status;
    private boolean valid;
    private Object amount;
    private Object payee;
    private List<Object> sna_attempts_error_codes;
    private Date date_created;
    private Date date_updated;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getService_sid() {
        return service_sid;
    }

    public void setService_sid(String service_sid) {
        this.service_sid = service_sid;
    }

    public String getAccount_sid() {
        return account_sid;
    }

    public void setAccount_sid(String account_sid) {
        this.account_sid = account_sid;
    }

    public String getMyto() {
        return myto;
    }

    public void setMyto(String myto) {
        this.myto = myto;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Object getAmount() {
        return amount;
    }

    public void setAmount(Object amount) {
        this.amount = amount;
    }

    public Object getPayee() {
        return payee;
    }

    public void setPayee(Object payee) {
        this.payee = payee;
    }

    public List<Object> getSna_attempts_error_codes() {
        return sna_attempts_error_codes;
    }

    public void setSna_attempts_error_codes(List<Object> sna_attempts_error_codes) {
        this.sna_attempts_error_codes = sna_attempts_error_codes;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(Date date_updated) {
        this.date_updated = date_updated;
    }
}
