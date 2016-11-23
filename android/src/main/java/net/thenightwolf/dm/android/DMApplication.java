package net.thenightwolf.dm.android;

import android.app.Application;
import android.content.Context;
import net.thenightwolf.dm.common.model.message.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DMApplication extends Application {
    private static Context appContext;
    private static final String VERSION = "0.0.1-ALPHA";

    private static BlockingQueue<Message> messageQueue;

    public static Context getAppContext() {
        return appContext;
    }

    @Override()
    public void onCreate(){
        super.onCreate();
        appContext = getApplicationContext();
        messageQueue = new LinkedBlockingQueue<Message>();
    }

    public static String getVERSION() {
        return VERSION;
    }

    public static BlockingQueue<Message> getMessageQueue(){
        return messageQueue;
    }
}
