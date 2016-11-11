/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.android.message;

public class PhoneFormatter {
    public static String cleanPhoneNumber(String number) {
        return number.replace("(", "")
                .replace(")", "")
                .replace("-", "")
                .replace(".", "")
                .replace(" ", "");
    }

    public static String prettyPhoneNumber(String number){
        if(number.length() == 10){
            String areaCode = number.substring(0, 3);
            String first = number.substring(3, 7);
            String second = number.substring(7, 11);
            String test = new StringBuilder()
                    .append("(")
                    .append(areaCode)
                    .append(") ")
                    .append(first)
                    .append("-")
                    .append(second)
                    .toString();
            return number;
        } else if(number.length() == 12){
            return number;
        }
        return number;
    }
}
