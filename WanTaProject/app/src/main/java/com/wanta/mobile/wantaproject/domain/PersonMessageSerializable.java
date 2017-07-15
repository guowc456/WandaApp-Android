package com.wanta.mobile.wantaproject.domain;

import java.io.Serializable;

/**
 * Created by WangYongqiang on 2016/11/25.
 */
public class PersonMessageSerializable implements Serializable{
    private String inputMessage ;

    public PersonMessageSerializable(String inputMessage) {
        this.inputMessage = inputMessage;
    }

    public String getInputMessage() {
        return inputMessage;
    }

    public void setInputMessage(String inputMessage) {
        this.inputMessage = inputMessage;
    }
}
