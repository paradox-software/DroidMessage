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
import android.provider.ContactsContract;
import net.thenightwolf.dm.android.DMApplication;
import net.thenightwolf.dm.android.R;
import net.thenightwolf.dm.common.model.message.Contact;
import net.thenightwolf.dm.common.model.message.Phone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burni_000 on 9/19/2016.
 */
public class ContactsManager {

    private static final String[] projection = { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };
    private static final String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

    private static final Logger logger = LoggerFactory.getLogger(ContactsManager.class);


    public static String getContactName (Context ctx, String phoneNumber) {
        String res;
        if (phoneNumber != null) {
            try {
                res = phoneNumber;
                ContentResolver resolver = ctx.getContentResolver();
                Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
                Cursor c = resolver.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

                if (c != null) {
                    if (c.moveToFirst()) {
                        res = Tools.getString(c, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        res += " " + Phone.cleanPhoneNumber(phoneNumber);
                    }
                    c.close();
                }
            } catch (Exception ex) {
                res = ctx.getString(R.string.chat_call_hidden);
            }
        } else {
            res = ctx.getString(R.string.chat_call_hidden);
        }
        return res;
    }

    public static List<Contact> getAllContacts(){
        ContentResolver cr = DMApplication.getAppContext().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        List<Contact> contacts = new ArrayList<>();
        logger.info("Contact count: {}", cur.getCount());
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                logger.info("Contact [{}] - {}", id, name);

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    logger.info("Contact has number");
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        logger.info("Moving to next row");
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String number = PhoneFormatter.cleanPhoneNumber(phoneNo);
                            logger.info("Checking for duplicate contact");
                        if(!hasContact(contacts, number)) {
                            Contact tempContact = new Contact();
                            tempContact.cleanNumber = number;
                            tempContact.name = name;
                            tempContact.id = id;
                            contacts.add(tempContact);
                        }
                    }
                    pCur.close();
                }
            }
        }
        cur.close();
        logger.info("Contact total size: {}", contacts.size());
        for(Contact contact : contacts){
            logger.info("Found Contact: [{}] {} - {}", contact.id, contact.name, contact.cleanNumber);
        }
        logger.info("Exiting contact harvester");
        return contacts;
    }

    private static boolean hasContact(List<Contact> contacts, String number) {
        if(contacts.size() == 0)
            return false;

        for(Contact contact : contacts){
            logger.info("Comparing {} to {}", contact.cleanNumber, number);
            if(contact.cleanNumber.equals(number))
                return true;
        }
        return false;
    }

}
