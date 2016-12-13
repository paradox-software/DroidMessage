package net.thenightwolf.dm.android.endpoint.thread;

import android.util.Log;
import com.google.gson.Gson;
import net.thenightwolf.dm.android.DMApplication;
import net.thenightwolf.dm.android.generator.thread.IThreadGenerator;
import net.thenightwolf.dm.android.message.Mms;
import net.thenightwolf.dm.android.message.MmsManager;
import net.thenightwolf.dm.android.message.SmsManager;
import net.thenightwolf.dm.android.endpoint.request.StandardRequestHandler;
import net.thenightwolf.dm.android.utils.SessionUtils;
import net.thenightwolf.dm.common.model.message.Message;
import net.thenightwolf.dm.common.model.message.ConversationMessageBundle;
import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.response.IStatus;
import org.nanohttpd.protocols.http.response.Status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpecificThreadRequestHandler extends StandardRequestHandler {

    private IThreadGenerator threadGenerator;

    @Override
    public String postMethod(Map<String, String> params, IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
        int threadID = Integer.parseInt(params.get("id"));
        int offset = SessionUtils.getSingleParamInt("offset", session);
        int limit = SessionUtils.getSingleParamInt("limit", session);

        return new Gson().toJson(threadGenerator.getMessageBundle(threadID, offset, limit));
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
}
