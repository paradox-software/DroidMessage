/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.common.model.comparator;

import net.thenightwolf.dm.common.model.message.Message;

import java.util.Comparator;

public class TemporalSmsComparator implements Comparator<Message> {

    int ascending;

    public TemporalSmsComparator(){
        ascending = -1;
    }

    public TemporalSmsComparator(boolean isAscending){
        if(isAscending)
            ascending = 1;
        else
            ascending = -1;
    }

    @Override
    public int compare(Message o1, Message o2) {
        return ascending * ((o1.getSentDate()).compareTo(o2.getSentDate()));
    }
}
