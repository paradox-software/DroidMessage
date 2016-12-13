package net.thenightwolf.dm.android.generator.update;

import net.thenightwolf.dm.android.DMApplication;
import net.thenightwolf.dm.common.model.message.Message;

import java.util.ArrayList;
import java.util.List;

public class UpdateGenerator implements IUpdateGenerator {

    public List<Message> getLatestMessages(){
        List<Message> latestSms = new ArrayList<Message>();

        Message current;
        while((current = DMApplication.getMessageQueue().poll()) != null) {
            latestSms.add(current);
        }

        return latestSms;
    }
}
