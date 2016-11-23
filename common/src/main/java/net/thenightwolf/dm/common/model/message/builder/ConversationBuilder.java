package net.thenightwolf.dm.common.model.message.builder;

import net.thenightwolf.dm.common.model.message.Conversation;
import net.thenightwolf.dm.common.model.message.Message;
import net.thenightwolf.dm.common.model.message.PhoneFormatter;

import javax.swing.text.ComponentView;

public class ConversationBuilder {
    private int conversationId;
    private String address;
    private boolean hasUnread;
    private long lastMessageDate;
    private Message lastMessage;

    public ConversationBuilder(int conversationId){
        this.conversationId = conversationId;
    }

    public ConversationBuilder setAddress(String address){
        this.address = PhoneFormatter.cleanPhoneNumber(address);
        return this;
    }

    public ConversationBuilder hasUnread(boolean hasUnread){
        this.hasUnread = hasUnread;
        return this;
    }

    public ConversationBuilder setLastMessageDate(long lastMessageDate){
        this.lastMessageDate = lastMessageDate;
        return this;
    }

    public ConversationBuilder setLastMessage(Message message){
        this.lastMessage = message;
        return this;
    }

    public Conversation build(){
        return new Conversation(conversationId, address, hasUnread, lastMessageDate, lastMessage);
    }
}
