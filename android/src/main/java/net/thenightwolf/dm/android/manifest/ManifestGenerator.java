package net.thenightwolf.dm.android.manifest;

import net.thenightwolf.dm.android.DMApplication;
import net.thenightwolf.dm.common.model.Manifest;
import net.thenightwolf.dm.common.model.message.Contact;
import net.thenightwolf.dm.android.message.ContactsManager;
import net.thenightwolf.dm.common.model.message.ConvoThread;
import net.thenightwolf.dm.android.message.SmsManager;

import java.util.List;

public class ManifestGenerator {
    List<Contact> contacts;
    List<ConvoThread> threads;
    public Manifest generate(){
        contacts = ContactsManager.getAllContacts();

        SmsManager smsManager = new SmsManager(DMApplication.getAppContext());
        threads = smsManager.getAllThreads();

        bindThreadsToContacts();

        Manifest manifest = new Manifest();
        manifest.contacts = contacts;
        manifest.threads = threads;

        return manifest;
    }

    private void bindThreadsToContacts(){
        int counter = 0;
        for (ConvoThread thread : threads) {
            for (Contact contact : contacts) {
                if (contact.cleanNumber.equals(thread.address)) {
                    contact.boundThreadId = thread.thread_id;
                    counter++;
                }
            }
        }
    }
}
