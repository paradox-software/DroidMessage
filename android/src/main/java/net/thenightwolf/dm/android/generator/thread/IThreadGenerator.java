package net.thenightwolf.dm.android.generator.thread;

import net.thenightwolf.dm.common.model.message.Conversation;
import net.thenightwolf.dm.common.model.message.ConversationMessageBundle;

import java.util.List;

public interface IThreadGenerator {
    List<Conversation> getAllConversations();

    ConversationMessageBundle getMessageBundle(int id, int offset, int limit);
}
