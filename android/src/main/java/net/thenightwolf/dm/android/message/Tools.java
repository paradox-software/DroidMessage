/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.android.message;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Tools {
    public static final String AUTHORS_URL = "https://raw.githubusercontent.com/Yakoo63/gtalksms/master/AUTHORS";
    public static final String DONORS_URL = "https://raw.githubusercontent.com/Yakoo63/gtalksms/master/Donors";
    public static final String CHANGELOG_URL = "https://raw.githubusercontent.com/Yakoo63/gtalksms/master/Changelog";
    public final static String LOG_TAG = "gtalksms";
    public final static String APP_NAME = "GTalkSMS";
    public final static String LineSep = System.getProperty("line.separator");
    private final static int shortenTo = 35;

    private static final Logger logger = LoggerFactory.getLogger(Tools.class);


    public static String getFileFormat(Calendar c) {
        return
                c.get(Calendar.YEAR) +
                        "-" +
                        String.format("%02d", (c.get(Calendar.MONTH)+ 1)) +
                        "-" +
                        String.format("%02d", c.get(Calendar.DAY_OF_MONTH)) +
                        " " +
                        String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) +
                        "h" +
                        String.format("%02d", c.get(Calendar.MINUTE)) +
                        "m" +
                        String.format("%02d", c.get(Calendar.SECOND)) +
                        "s";
    }

    public static String getVersionName(Context context) {

        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    public static String getVersion(Context context, Class<?> cls) {

        try {
            ComponentName comp = new ComponentName(context, cls);
            return context.getPackageManager().getPackageInfo(comp.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    public static String getVersionCode(Context context, Class<?> cls) {

        try {
            ComponentName comp = new ComponentName(context, cls);
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(comp.getPackageName(), 0);

            return "" + pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    public static <T> List<T> getLastElements(ArrayList<T> list, int nbElems) {
        return list.subList(Math.max(list.size() - nbElems, 0), list.size());
    }

    public static Long getLong(Cursor c, String col) {
        return c.getLong(c.getColumnIndex(col));
    }

    public static int getInt(Cursor c, String col) {
        return c.getInt(c.getColumnIndex(col));
    }

    public static String getString(Cursor c, String col) {
        return c.getString(c.getColumnIndex(col));
    }

    public static boolean getBoolean(Cursor c, String col) {
        return getInt(c, col) == 1;
    }

    public static Date getDateSeconds(Cursor c, String col) {
        return new Date(Long.parseLong(Tools.getString(c, col)) * 1000);
    }

    public static Date getDateMilliSeconds(Cursor c, String col) {
        return new Date(Long.parseLong(Tools.getString(c, col)));
    }


    public static boolean isInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException nfe) {}
        return false;
    }

    public static Boolean parseBool(String value) {
        Boolean res = null;
        if (value.compareToIgnoreCase("true") == 0) {
            res = true;
        } else if (value.compareToIgnoreCase("false") == 0) {
            res = false;
        }
        return res;
    }

    public static Integer parseInt(String value) {
        Integer res = null;
        try {
            res = Integer.parseInt(value);
        } catch(Exception e) {}

        return res;
    }

    public static Integer parseInt(String value, Integer defaultValue) {
        Integer res = defaultValue;
        try {
            res = Integer.parseInt(value);
        } catch(Exception e) {}

        return res;
    }

    public static int getMinNonNeg(int... x) {
        int min = Integer.MAX_VALUE;
        for(int i : x) {
            if(i >= 0 && i < min)
                min = i;
        }
        return min;
    }

    public static boolean copyFile(File from, File to) {
        if (!from.isFile() || !to.isFile())
            return false;

        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(from);
            out = new FileOutputStream(to);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    /* Ignore */
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    /* Ignore */
                }
            }
        }
        return true;
    }

    public static boolean writeFile(byte[] data, File file) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static String getAppBaseDir(Context ctx) {
        String filesDir = ctx.getFilesDir().toString();
        int index = filesDir.indexOf("/files");
        return filesDir.substring(0, index);
    }

    public static String getSharedPrefDir(Context ctx) {
        return getAppBaseDir(ctx) + "/shared_prefs";
    }

    public static String shortenString(String s) {
        if (s.length() > 20) {
            return s.substring(0, 20);
        } else {
            return s;
        }
    }

    public static String shortenMessage(String message) {
        String shortenedMessage;
        if (message == null) {
            shortenedMessage = "";
        } else if (message.length() < shortenTo) {
            shortenedMessage = message.replace("\n", " ");
        } else {
            shortenedMessage = message.substring(0, shortenTo).replace("\n", " ") + "...";
        }
        return shortenedMessage;
    }



    public static void openLink(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Returns a String in the simple date format
     *
     * @return the current date in dd/MM/yyyy format
     */
    public static String currentDate() {
        DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        return DATE_FORMAT.format(cal.getTime());
    }

    public static String ipIntToString(int ip) {
        return String.format("%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));
    }

    public static String STMArrayToString(StackTraceElement[] stma) {
        String res = "";
        for (StackTraceElement e : stma) {
            res += (e.toString() + '\n');
        }
        return res;
    }

    /**
     * Checks if the given fromJid is part of the notified Address set. fromJid can either be a fullJid or a bareJid
     *
     * @param list List of allowed addresses
     * @param fromJid The JID we received a message from
     * @return true if the given JID is part of the notified Address set, otherwise false
     */
    public static boolean cameFromNotifiedAddress(String[] list , String fromJid) {
        String sanitizedNotifiedAddress;
        String sanitizedJid = fromJid.toLowerCase();
        for (String notifiedAddress : list) {
            sanitizedNotifiedAddress = notifiedAddress.toLowerCase();
            // If it's a fullJID, append a slash for security reasons
            if (sanitizedJid.startsWith(sanitizedNotifiedAddress + "/")
                    // A bare JID should be equals to one of the notified Address set
                    || sanitizedNotifiedAddress.equals(sanitizedJid)) {
                return true;
            }
        }
        return false;
    }


}
