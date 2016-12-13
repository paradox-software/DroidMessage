package net.thenightwolf.dm.android.generator.message.send;

public interface IMessageSender {
    void sendSMS(String number, String content);
}
