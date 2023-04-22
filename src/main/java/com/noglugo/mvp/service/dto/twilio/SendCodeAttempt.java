package com.noglugo.mvp.service.dto.twilio;

import java.util.Date;

public class SendCodeAttempt {

    private Date time;
    private String channel;
    private String attempt_sid;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getAttempt_sid() {
        return attempt_sid;
    }

    public void setAttempt_sid(String attempt_sid) {
        this.attempt_sid = attempt_sid;
    }
}
