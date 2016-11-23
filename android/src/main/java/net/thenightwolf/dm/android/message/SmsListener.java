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
import net.thenightwolf.dm.common.model.message.Message;
import net.thenightwolf.dm.common.model.message.PhoneFormatter;
import net.thenightwolf.dm.common.model.message.builder.MessageBuilder;
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

                Message message = new MessageBuilder(-1)
                        .setSMSMessage()
                        .setNumber(PhoneFormatter.cleanPhoneNumber(smsMessage.getOriginatingAddress()))
                        .setContent(smsMessage.getMessageBody())
                        .setSentDate(smsMessage.getTimestampMillis())
                        .build();

                DMApplication.getMessageQueue().add(message);
            }
        }
    }
}
