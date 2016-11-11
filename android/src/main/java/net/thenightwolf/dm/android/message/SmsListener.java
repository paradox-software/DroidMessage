/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.android.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import net.thenightwolf.dm.android.DMApplication;
import net.thenightwolf.dm.common.model.message.Sms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


public class SmsListener extends BroadcastReceiver {

    private static final Logger logger = LoggerFactory.getLogger(SmsListener.class);

    @Override
    public void onReceive(Context context, Intent intent) {

        if(Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())){
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                logger.info("SMS Received - from: {} message: {} date: {}",
                        smsMessage.getOriginatingAddress(),
                        smsMessage.getMessageBody(),
                        new Date(smsMessage.getTimestampMillis()).toString());
                Sms sms = new Sms(1, smsMessage.getMessageBody(),
                        smsMessage.getOriginatingAddress(),
                        new Date(smsMessage.getTimestampMillis()));
                DMApplication.getMessageQueue().add(sms);
            }
        }
    }
}
