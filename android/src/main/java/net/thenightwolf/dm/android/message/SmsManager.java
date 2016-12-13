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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.view.menu.ListMenuItemView;
import android.util.Base64;
import android.util.Log;
import net.thenightwolf.dm.android.DMApplication;
import net.thenightwolf.dm.android.R;
import net.thenightwolf.dm.common.model.message.Conversation;
import net.thenightwolf.dm.common.model.message.Message;
import net.thenightwolf.dm.common.model.message.builder.ConversationBuilder;
import net.thenightwolf.dm.common.model.message.builder.MessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;
import static android.telephony.SmsManager.getDefault;

/**
 * Created by burni_000 on 9/19/2016.
 */
public class SmsManager {
    private final Context _context;

    private static final Logger logger = LoggerFactory.getLogger(SmsManager.class);

    private String smsNumber = "9189231326";

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

    public ArrayList<Message> getAllSms(){
        return getAllSms(null);
    }

    public ArrayList<Message> getLastUnreadSms() {
        return getAllSms("read = 0");
    }

    public Message getLastUnownedSms(int threadID){
        String selection = "type=1 AND thread_id=" + threadID;
        Message message = null;
        Cursor c = _context.getContentResolver().query(SMS_CONTENT_URI, COLUMNS, selection, null, "date DESC LIMIT 1");
        Log.d("SMS count", "counter: " + c.getCount());
        if(c != null && c.moveToFirst()){
            boolean isSent = Tools.getInt(c, "type") == 2;
            message = new MessageBuilder(Tools.getInt(c, "_id"))
                    .setSMSMessage()
                    .setContent(Tools.getString(c, "body"))
                    .setNumber(Tools.getString(c, "address"))
                    .setSentDate(Tools.getDateMilliSeconds(c, "date"))
                    .build();
        }
        c.close();
        return message;
    }

    public ArrayList<Message> getLastSms(){
        ArrayList<Message> res = new ArrayList<Message>();
        Cursor c = _context.getContentResolver().query(SMS_CONTENT_URI, COLUMNS, null, null, "date DESC LIMIT 1");
        Log.d("SMS count", "counter: " + c.getCount());
        if(c != null && c.moveToFirst()){
            boolean isSent = Tools.getInt(c, "type") == 2;
            Message message = new MessageBuilder(Tools.getInt(c, "_id"))
                    .setSMSMessage()
                    .setContent(Tools.getString(c, "body"))
                    .setNumber(Tools.getString(c, "address"))
                    .setSentDate(Tools.getDateMilliSeconds(c, "date"))
                    .build();
            res.add(message);
        }
        c.close();
        return res;
    }

    public ArrayList<Message> getLastSmsForThread(int id){
        return getAllSmsForThread(id, 0, 1);
    }

    public ArrayList<Message> getAllSmsForThread(int id, int offest, int limit){
        String where = "thread_id = " + id;
        ArrayList<Message> res = new ArrayList<Message>();
        Cursor c = _context.getContentResolver().query(SMS_CONTENT_URI, COLUMNS, where, null, "date DESC LIMIT " + limit + " OFFSET " + offest);
        Log.d("SMS count", "counter: " + c.getCount());
        return query(c);
    }

    public ArrayList<Message> getAllSmsForThread(int id){
        String where = "thread_id = " + id;
        Cursor c = _context.getContentResolver().query(SMS_CONTENT_URI, COLUMNS, where, null, "date DESC");
        Log.d("SMS count", "counter: " + c.getCount());
        return query(c);
    }

    private ArrayList<Message> query(Cursor c){
        ArrayList<Message> res = new ArrayList<Message>();
        if(c != null && c.moveToFirst()){
            do {
                boolean isSent = Tools.getInt(c, "type") == 2;
                String number = getCorrectNumber(Tools.getString(c, "address"),
                        Tools.getInt(c, "type"));

                Message message = new MessageBuilder(Tools.getInt(c, "_id"))
                        .setSMSMessage()
                        .setContent(Tools.getString(c, "body"))
                        .setNumber(number)
                        .setSentDate(Tools.getDateMilliSeconds(c, "date"))
                        .build();

                logger.info("SMS found: {}", message.toString());
                res.add(message);
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

    private ArrayList<Message> getAllSms(String where) {
        ArrayList<Message> res = new ArrayList<Message>();

        Cursor c = _context.getContentResolver().query(SMS_INBOX_CONTENT_URI, COLUMNS, where, null, SORT_ORDER_LIMIT + smsNumber);
        Log.d("getAllSMS", String.valueOf(c.getCount()));
        if (c != null) {
            for (boolean hasData = c.moveToFirst(); hasData; hasData = c.moveToNext()) {
                boolean isSent = Tools.getInt(c, "type") == 2;
                String address = Tools.getString(c, "address");

                String sender = ContactsManager.getContactName(_context, address);
                String receiver = _context.getString(R.string.chat_me);
                Message message = new MessageBuilder(Tools.getInt(c, "_id"))
                        .setSMSMessage()
                        .setContent(Tools.getString(c, "body"))
                        .setNumber(Tools.getString(c, "address"))
                        .setSentDate(Tools.getDateMilliSeconds(c, "date"))
                        .build();

                res.add(message);
            }
            c.close();
        }
        return res;
    }

    public List<Conversation> getAllThreads() {
        logger.info("Getting all threads");
        List<Conversation> threads = new ArrayList<>();
        ContentResolver cr = _context.getContentResolver();
        Cursor query = cr.query(THREADS_CONTENT_URI, THREAD_COL, null, null, null);
        for(String col : query.getColumnNames()){
            logger.info("Column : {}", col);
        }
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
                            } else if ("image/jpeg".equals(mmsType) ||
                                        "image/bmp".equals(mmsType) ||
                                        "image/gif".equals(mmsType) ||
                                        "image/jpg".equals(mmsType) ||
                                        "image/png".equals(mmsType)) {
                                Bitmap bitmap = getMmsImage(partID);

                                Message message = new MessageBuilder(0)
                                        .setContent(encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100))
                                        .build();


                                Conversation conversation = new ConversationBuilder(Tools.getInt(query, "thread_id"))
                                        .setAddress(getNumber(DMApplication.getAppContext(), partID))
                                        .setAddress(getLastUnownedSms(Tools.getInt(query, "thread_id")).getNumber())
                                        .setLastMessageDate(Tools.getLong(query, "date"))
                                        .hasUnread(Tools.getInt(query, "read") == 1)
                                        .setLastMessage(message)
                                        .build();

                                threads.add(conversation);

                            }
                        } while(subQuery.moveToNext());
                    }
                } else {
                    logger.info("Type is SMS!");
                    logger.info("ConversationMessageBundle Details: [{}] Date: {} ConversationMessageBundle ID: {} Read: {} Address: {}",
                            Tools.getString(query, "_id"),
                            Tools.getString(query, "date"),
                            Tools.getString(query, "thread_id"),
                            Tools.getInt(query, "read"),
                            Tools.getString(query, "address"));

                    int threadID = Tools.getInt(query, "thread_id");
                    Conversation conversation = new ConversationBuilder(threadID)
                            .setAddress(Tools.getString(query, "address"))
                            .setLastMessageDate(Tools.getLong(query, "date"))
                            .hasUnread(Tools.getInt(query, "read") == 1)
                            .setLastMessage(getLastSmsForThread(threadID).get(0))
                            .build();

                    threads.add(conversation);
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

    private Bitmap getMmsImage(String _id) {
        Uri partURI = Uri.parse("content://mms/part/" + _id);
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = DMApplication.getAppContext().getContentResolver().openInputStream(partURI);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) { logger.error("Error", e);}
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) { logger.error("Error", e);}
            }
        }
        return bitmap;
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
    private static String getNumber(Context c, String id) {
        String add = "";
        final String[] projection = new String[]{"address"};
        Uri.Builder builder = Uri.parse("content://mms").buildUpon();
        builder.appendPath(String.valueOf(id)).appendPath("addr");
        Cursor cursor = c.getContentResolver().query(
                builder.build(),
                projection,
                "type=" + 0x97,
                null, null);
        if (cursor.moveToFirst()) {
            add = cursor.getString(cursor.getColumnIndex("address"));
        }
        cursor.close();
        return add;
    }
}
