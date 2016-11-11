package net.thenightwolf.dm.android;

import android.app.Application;
import android.content.Context;
import net.thenightwolf.dm.common.model.message.Sms;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DMApplication extends Application {
    private static Context appContext;
    private static final String VERSION = "0.0.1-ALPHA";

    private static BlockingQueue<Sms> messageQueue;

    public static Context getAppContext() {
        return appContext;
    }

    @Override()
    public void onCreate(){
        super.onCreate();
        appContext = getApplicationContext();
        messageQueue = new LinkedBlockingQueue<Sms>();
    }

    public static String getVERSION() {
        return VERSION;
    }

    public static BlockingQueue<Sms> getMessageQueue(){
        return messageQueue;
    }
}
