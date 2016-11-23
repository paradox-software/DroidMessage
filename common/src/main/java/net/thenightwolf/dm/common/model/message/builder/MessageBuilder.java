package net.thenightwolf.dm.common.model.message.builder;

import net.thenightwolf.dm.common.model.message.Message;
import net.thenightwolf.dm.common.model.message.PhoneFormatter;

import java.security.MessageDigest;
import java.util.Date;

public class MessageBuilder {

    private int id;
    private int messageType;
    private String number;
    private String content;
    private Date sentDate;

    public MessageBuilder(int id){
        this.id = id;
    }

    public MessageBuilder setNumber(String number){
        this.number = PhoneFormatter.cleanPhoneNumber(number);
        return this;
    }

    public MessageBuilder setContent(String content){
        this.content = content;
        return this;
    }

    public MessageBuilder setSentDate(Date date){
        this.sentDate = date;
        return this;
    }

    public MessageBuilder setSentDate(long date){
        this.sentDate = new Date(date);
        return this;
    }

    public MessageBuilder setMMSImageMessage(){
        messageType = Message.MMS_IMAGE;
        return this;
    }

    public MessageBuilder setMMSTextMessage(){
        messageType = Message.MMS_TEXT;
        return this;
    }

    public MessageBuilder setSMSMessage(){
        messageType = Message.SMS_TYPE;
        return this;
    }

    public Message build(){
        return new Message(id, messageType, number, content, sentDate);
    }
}
