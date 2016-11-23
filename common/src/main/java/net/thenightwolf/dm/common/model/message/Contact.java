/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.common.model.message;

import java.util.List;

/**
 * Created by burni_000 on 9/19/2016.
 */
public class Contact {
    public String name;
    public String cleanNumber;
    public String id;
    public List<Long> rawIds;
    public List<Message> messages;
    public int boundThreadId;

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", cleanNumber='" + cleanNumber + '\'' +
                ", id='" + id + '\'' +
                ", rawIds=" + rawIds +
                ", messages=" + messages +
                ", boundThreadId=" + boundThreadId +
                '}';
    }
}
