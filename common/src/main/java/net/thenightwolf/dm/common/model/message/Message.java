/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.common.model.message;

import java.util.Date;

/**
 * Created by burni_000 on 9/19/2016.
 */
public class Message {

    public transient static final int SMS_TYPE = 1;
    public transient static final int MMS_IMAGE = 2;
    public transient static final int MMS_TEXT = 3;

    private int id;
    private int messageType;
    private String number;
    private String content;
    private Date sentDate;

    public Message(){ }

    public Message(int id, int messageType, String number, String content, Date sentDate) {
        this.id = id;
        this.messageType = messageType;
        this.number = number;
        this.content = content;
        this.sentDate = sentDate;
    }

    public int getId() {
        return id;
    }

    public int getMessageType() {
        return messageType;
    }

    public String getContent() {
        return content;
    }

    public String getNumber() {
        return number;
    }

    public Date getSentDate() {
        return sentDate;
    }
}
