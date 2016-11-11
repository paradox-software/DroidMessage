/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.common.model.comparator;

import net.thenightwolf.dm.common.model.message.Contact;

import java.util.Comparator;

public class LexicalContactComparator implements Comparator<Contact> {

    @Override
    public int compare(Contact contactOne, Contact contactTwo) {
        return contactOne.name.compareToIgnoreCase(contactTwo.name);
    }
}
