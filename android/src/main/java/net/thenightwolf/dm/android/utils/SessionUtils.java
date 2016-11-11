package net.thenightwolf.dm.android.utils;

import android.support.annotation.Nullable;
import com.google.gson.Gson;
import net.thenightwolf.dm.common.model.error.ErrorStatus;
import org.nanohttpd.protocols.http.IHTTPSession;

public class SessionUtils {

    @Nullable
    public static String getSingleParam(String key, IHTTPSession session){
        if(session.getParameters().containsKey(key))
            return session.getParameters().get(key).get(0);
        else
            return null;
    }

    public static boolean keyExists(String key, IHTTPSession session){
        return session.getParameters().containsKey(key);
    }

    public static int getSingleParamInt(String key, IHTTPSession session){
        int param = 0;
        if(session.getParameters().containsKey(key)){
            try {
                param = Integer.parseInt(session.getParameters().get(key).get(0));
            } catch (Exception e){
                return (param = -1);
            }
        }
        return param;
    }

    public static String buildError(String errorCode, String desc){
        ErrorStatus status = new ErrorStatus();
        status.errorCode = errorCode;
        status.description = desc;
        return new Gson().toJson(status);
    }
}
