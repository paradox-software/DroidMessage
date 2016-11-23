package net.thenightwolf.dm.common.model;


import net.thenightwolf.dm.common.model.message.Contact;
import net.thenightwolf.dm.common.model.message.Conversation;

import java.util.List;

public class Manifest {
    public List<Contact> contacts;
    public List<Conversation> threads;
    public String ownerNumber;
}
