/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.common.model.message;

import java.util.Date;

/**
 * Created by burni_000 on 9/19/2016.
 */
public class Sms {
    private int id;
    private String message;
    private String number;
    private Date sentDate;

    public Sms(){ }

    public Sms(int id, String message, String number, Date sentDate) {
        this.id = id;
        this.message = message;
        this.number = number;
        this.sentDate = sentDate;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getNumber() {
        return number;
    }

    public Date getSentDate() {
        return sentDate;
    }
}
