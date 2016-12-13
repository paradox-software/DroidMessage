package net.thenightwolf.dm.android.generator.message.send;

import net.thenightwolf.dm.android.DMApplication;
import net.thenightwolf.dm.android.message.SmsManager;

public class MessageSender implements IMessageSender {
    @Override
    public void sendSMS(String number, String content) {
        SmsManager smsManager = new SmsManager(DMApplication.getAppContext());
        smsManager.sendSMS(number, content);
    }
}
