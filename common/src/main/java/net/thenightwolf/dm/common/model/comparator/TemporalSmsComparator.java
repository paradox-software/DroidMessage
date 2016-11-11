/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.common.model.comparator;

import net.thenightwolf.dm.common.model.message.ConvoThread;
import net.thenightwolf.dm.common.model.message.Sms;

import java.util.Comparator;
import java.util.Date;

public class TemporalSmsComparator implements Comparator<Sms> {

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
    public int compare(Sms o1, Sms o2) {
        return ascending * ((o1.getSentDate()).compareTo(o2.getSentDate()));
    }
}
