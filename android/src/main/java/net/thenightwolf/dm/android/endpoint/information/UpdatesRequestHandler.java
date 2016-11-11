package net.thenightwolf.dm.android.endpoint.information;

import com.google.gson.Gson;
import net.thenightwolf.dm.android.DMApplication;
import net.thenightwolf.dm.android.endpoint.request.Authenticator;
import net.thenightwolf.dm.android.endpoint.request.StandardRequestHandler;
import net.thenightwolf.dm.android.manifest.ManifestGenerator;
import net.thenightwolf.dm.common.model.Manifest;
import net.thenightwolf.dm.common.model.message.Sms;
import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.response.IStatus;
import org.nanohttpd.protocols.http.response.Status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdatesRequestHandler extends StandardRequestHandler {

    private IStatus status = Status.BAD_REQUEST;

    @Override
    public String postMethod(Map<String, String> params, IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {

        Authenticator auth = new Authenticator();
        if(auth.authenticate(session)) {

            List<Sms> latestSms = new ArrayList<Sms>();
            Sms current;
            while((current = DMApplication.getMessageQueue().poll()) != null){
                latestSms.add(current);
            }

            status = Status.OK;
            return new Gson().toJson(latestSms);
        } else {
            status = Status.UNAUTHORIZED;
            return auth.buildErrorResponse();
        }


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
        return null;
    }

    @Override
    public String getMimeType() {
        return "application/json";
    }

    @Override
    public IStatus getStatus() {
        return status;
    }
}
