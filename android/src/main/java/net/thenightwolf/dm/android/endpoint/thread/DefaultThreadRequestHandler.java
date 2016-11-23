package net.thenightwolf.dm.android.endpoint.thread;

import com.google.gson.Gson;
import net.thenightwolf.dm.android.DMApplication;
import net.thenightwolf.dm.android.message.SmsManager;
import net.thenightwolf.dm.android.endpoint.request.StandardRequestHandler;
import net.thenightwolf.dm.android.utils.SessionUtils;
import net.thenightwolf.dm.common.model.message.Conversation;
import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.response.IStatus;
import org.nanohttpd.protocols.http.response.Status;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DefaultThreadRequestHandler extends StandardRequestHandler {

    @Override
    public String getText() {
        return SessionUtils.buildError("400", "Must supply parameters!");
    }

    @Override
    public String getMimeType() {
        return "application/json";
    }

    @Override
    public IStatus getStatus() {
        return Status.OK;
    }

    @Override
    public String postMethod(Map<String, String> params, IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
        SmsManager smsManager = new SmsManager(DMApplication.getAppContext());
        List<Conversation> threads = smsManager.getAllThreads();
        return new Gson().toJson(threads);
    }

    @Override
    public String getMethod(Map<String, String> params, IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
        return null;
    }

    @Override
    public String putMethod(Map<String, String> params, IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
        return null;
    }

    @Override
    public String updateMethod(Map<String, String> params, IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
        return null;
    }
}
