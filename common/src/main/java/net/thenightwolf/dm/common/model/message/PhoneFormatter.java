/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.common.model.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhoneFormatter {
    
    private static final Logger logger = LoggerFactory.getLogger(PhoneFormatter.class);
    
    public static String cleanPhoneNumber(String number) {
        if(number.equals("")) return "";
        String cleaned = number.replaceAll("[^\\d]", "");
        int length = cleaned.length();
        logger.info("cleaning number: {} -> {}", number, cleaned );
        switch (length){
            case 10: return cleaned;
            default: return cleaned.substring(1);
        }
    }

    public static String prettyPhoneNumber(String number){
        if(number.length() == 10){
            return number.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
        }
        return number;
    }
}
