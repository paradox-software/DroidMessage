/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.common.model.message;

import java.util.ArrayList;

public class ConvoThread {
    public String name;
    public int thread_id;
    public String address;
    public int person;
    public long date;
    public boolean read;
    public String body;
    public ArrayList<Sms> messages;

    @Override
    public String toString() {
        return "ConvoThread{" +
                "name='" + name + '\'' +
                ", thread_id=" + thread_id +
                ", address='" + address + '\'' +
                ", person=" + person +
                ", date=" + date +
                ", read=" + read +
                ", body='" + body + '\'' +
                ", messages=" + messages +
                '}';
    }
}
