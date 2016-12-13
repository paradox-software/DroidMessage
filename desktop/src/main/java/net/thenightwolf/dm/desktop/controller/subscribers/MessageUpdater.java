package net.thenightwolf.dm.desktop.controller.subscribers;

import net.thenightwolf.dm.common.model.message.Message;

public interface MessageUpdater {
    public void addMessage(Message message);
}
