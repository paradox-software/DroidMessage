/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.common.model.comparator;

import net.thenightwolf.dm.common.model.message.Conversation;

import java.util.Comparator;
import java.util.Date;

public class TemporalThreadComparator implements Comparator<Conversation> {

    int ascending;

    public TemporalThreadComparator(){
        ascending = -1;
    }

    public TemporalThreadComparator(boolean isAscending){
        if(isAscending)
            ascending = 1;
        else
            ascending = -1;
    }

    @Override
    public int compare(Conversation o1, Conversation o2) {
        return ascending * (new Date(o1.getLastMessageDate()).compareTo(new Date(o2.getLastMessageDate())));
    }
}
