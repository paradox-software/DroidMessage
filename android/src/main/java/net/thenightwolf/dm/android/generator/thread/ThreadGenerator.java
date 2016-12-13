package net.thenightwolf.dm.android.generator.thread;

import android.util.Log;
import net.thenightwolf.dm.android.DMApplication;
import net.thenightwolf.dm.android.message.Mms;
import net.thenightwolf.dm.android.message.MmsManager;
import net.thenightwolf.dm.android.message.SmsManager;
import net.thenightwolf.dm.android.utils.SessionUtils;
import net.thenightwolf.dm.common.model.message.Conversation;
import net.thenightwolf.dm.common.model.message.ConversationMessageBundle;
import net.thenightwolf.dm.common.model.message.Message;

import java.util.ArrayList;
import java.util.List;

public class ThreadGenerator implements IThreadGenerator {
    @Override
    public List<Conversation> getAllConversations() {
        SmsManager smsManager = new SmsManager(DMApplication.getAppContext());
        return smsManager.getAllThreads();
    }

    @Override
    public ConversationMessageBundle getMessageBundle(int id, int offset, int limit) {
        ArrayList<Message> messages;
        messages = new SmsManager(DMApplication.getAppContext())
                .getAllSmsForThread(id, offset, limit);

        ConversationMessageBundle thread = new ConversationMessageBundle();
        thread.messages = messages;
        thread.threadID = id;
        return thread;
    }
}
