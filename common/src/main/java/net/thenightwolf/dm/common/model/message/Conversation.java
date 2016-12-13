/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.common.model.message;

import java.util.ArrayList;

public class Conversation {
    private int conversationId;
    private String address;
    private boolean hasUnread;
    private long lastMessageDate;
    private Message lastMessage;

    public Conversation(int conversationId, String address, boolean hasUnread, long lastMessageDate, Message lastMessage) {
        this.conversationId = conversationId;
        this.address = address;
        this.hasUnread = hasUnread;
        this.lastMessageDate = lastMessageDate;
        this.lastMessage = lastMessage;
    }

    public int getConversationId() {
        return conversationId;
    }

    public String getAddress() {
        return address;
    }

    public boolean isHasUnread() {
        return hasUnread;
    }

    public long getLastMessageDate() {
        return lastMessageDate;
    }

    public Message getLastMessage() {
        return lastMessage;
    }
}
