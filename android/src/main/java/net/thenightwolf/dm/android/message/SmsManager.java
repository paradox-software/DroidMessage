/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.android.message;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import net.thenightwolf.dm.android.R;
import net.thenightwolf.dm.common.model.message.ConvoThread;
import net.thenightwolf.dm.common.model.message.Sms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.telephony.SmsManager.getDefault;

/**
 * Created by burni_000 on 9/19/2016.
 */
public class SmsManager {
    private final Context _context;

    private static final Logger logger = LoggerFactory.getLogger(SmsManager.class);

    private String smsNumber = "918-923-1326";

    private static final Uri THREADS_CONTENT_URI = Uri.parse("content://mms-sms/conversations/");
    private static final Uri SMS_CONTENT_URI = Uri.parse("content://sms");

    private static final Uri SMS_INBOX_CONTENT_URI = Uri.withAppendedPath(SMS_CONTENT_URI, "inbox");
    private static final Uri SMS_SENTBOX_CONTENT_URI = Uri.withAppendedPath(SMS_CONTENT_URI, "sent");

    private static final String THREAD_COL[] = new String[] {"_id", "date", "thread_id", "ct_t","read", "address"};
    private static final String COLUMNS[] = new String[] { "_id", "person", "address", "body", "date", "type", "thread_id" };

    private static final String SORT_ORDER = "date DESC";
    private static final String SORT_ORDER_LIMIT = "date DESC limit ";
    private static final String homeNumber = "9189231326";

    public SmsManager(Context baseContext) {
        _context = baseContext;
    }

    public ArrayList<Sms> getAllSms(){
        return getAllSms(null);
    }

    public ArrayList<Sms> getLastUnreadSms() {
        return getAllSms("read = 0");
    }

    public ArrayList<Sms> getLastSms(){
        ArrayList<Sms> res = new ArrayList<Sms>();
        Cursor c = _context.getContentResolver().query(SMS_CONTENT_URI, COLUMNS, null, null, "date DESC LIMIT 1");
        Log.d("SMS count", "counter: " + c.getCount());
        if(c != null && c.moveToFirst()){
            boolean isSent = Tools.getInt(c, "type") == 2;
                Sms sms = new Sms(Tools.getInt(c, "_id"),
                        Tools.getString(c, "body"),
                        Tools.getString(c, "address"),
                        Tools.getDateMilliSeconds(c, "date"));
            res.add(sms);
        }
        c.close();
        return res;
    }

    public ArrayList<Sms> getLastSmsForThread(int id){
        return getAllSmsForThread(id, 0, 1);
    }

    public ArrayList<Sms> getAllSmsForThread(int id, int offest, int limit){
        String where = "thread_id = " + id;
        ArrayList<Sms> res = new ArrayList<Sms>();
        Cursor c = _context.getContentResolver().query(SMS_CONTENT_URI, COLUMNS, where, null, "date DESC LIMIT " + limit + " OFFSET " + offest);
        Log.d("SMS count", "counter: " + c.getCount());
        if(c != null && c.moveToFirst()){
            do {
                boolean isSent = Tools.getInt(c, "type") == 2;
                String number = getCorrectNumber(Tools.getString(c, "address"),
                        Tools.getInt(c, "type"));

                Sms sms = new Sms(Tools.getInt(c, "_id"),
                        Tools.getString(c, "body"),
                        number,
                        Tools.getDateMilliSeconds(c, "date"));
                logger.info("SMS found: {}", sms.toString());
                res.add(sms);
            } while(c.moveToNext());
        }
        c.close();
        return res;
    }

    private String getCorrectNumber(String address, int type){
        if(type == 1){
            return address;
        } else {
            return homeNumber;
        }
    }

    private ArrayList<Sms> getAllSms(String where) {
        ArrayList<Sms> res = new ArrayList<Sms>();

        Cursor c = _context.getContentResolver().query(SMS_INBOX_CONTENT_URI, COLUMNS, where, null, SORT_ORDER_LIMIT + smsNumber);
        Log.d("getAllSMS", String.valueOf(c.getCount()));
        if (c != null) {
            for (boolean hasData = c.moveToFirst(); hasData; hasData = c.moveToNext()) {
                boolean isSent = Tools.getInt(c, "type") == 2;
                String address = Tools.getString(c, "address");

                String sender = ContactsManager.getContactName(_context, address);
                String receiver = _context.getString(R.string.chat_me);
                Sms sms = new Sms(Tools.getInt(c, "_id"),
                        Tools.getString(c, "body"),
                        Tools.getString(c, "address"),
                        Tools.getDateMilliSeconds(c, "date"));
                res.add(sms);
            }
            c.close();
        }
        return res;
    }

    public List<ConvoThread> getAllThreads() {
        logger.info("Getting all threads");
        List<ConvoThread> threads = new ArrayList<>();
        ContentResolver cr = _context.getContentResolver();
        Cursor query = cr.query(THREADS_CONTENT_URI, THREAD_COL, null, null, null);
        logger.info("Count: {}", query.getCount());
        if (query.moveToFirst()) {
            do {
                String type = Tools.getString(query, "ct_t");
                logger.info("type = {}", type);
                if ("application/vnd.wap.multipart.related".equals(type)) {
                    logger.info("Type is MMS!");
                    String selection = "mid=" + Tools.getString(query, "_id");
                    Uri uri = Uri.parse("content://mms/part");
                    Cursor subQuery = _context.getContentResolver().query(uri, null, selection, null, null);
                    assert subQuery != null;
                    if(subQuery.moveToFirst()){
                        do {
                            String partID = Tools.getString(subQuery, "_id");
                            String mmsType = Tools.getString(subQuery, "ct");
                            if("text/plain".equals(mmsType)){
                                logger.info("Found MMS text message!");
                                String data = Tools.getString(subQuery, "_data");
                                String body;
                                if(data != null){
                                    body = getMmsText(partID);
                                } else {
                                    body = Tools.getString(subQuery, "text");
                                }
                                logger.info("MMS text message: {}", body);
                            } else {
                                logger.info("Not text MMS message!");
                            }
                        } while(subQuery.moveToNext());
                    }
                } else {
                    logger.info("Type is SMS!");
                    ConvoThread convoThread = new ConvoThread();
                    logger.info("ThreadBundle Details: [{}] Date: {} ThreadBundle ID: {} Read: {} Address: {}",
                            Tools.getString(query, "_id"),
                            Tools.getString(query, "date"),
                            Tools.getString(query, "thread_id"),
                            Tools.getInt(query, "read"),
                            Tools.getString(query, "address"));

                    convoThread.thread_id = Tools.getInt(query, "thread_id");
                    convoThread.body = getLastSmsForThread(convoThread.thread_id).get(0).getMessage();
                    convoThread.date = Tools.getLong(query, "date");
                    convoThread.read = Tools.getInt(query, "read") == 1;
                    convoThread.address = Tools.getString(query, "address");
                    threads.add(convoThread);
                }

            } while (query.moveToNext());
        }
        query.close();
        return threads;
    }

    public void sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        android.telephony.SmsManager sms = getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private String getMmsText(String id) {
        Uri partURI = Uri.parse("content://mms/part/" + id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = _context.getContentResolver().openInputStream(partURI);
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                String temp = reader.readLine();
                while (temp != null) {
                    sb.append(temp);
                    temp = reader.readLine();
                }
            }
        } catch (IOException e) {}
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
        return sb.toString();
    }
}
