package net.thenightwolf.dm.android.endpoint.information;

import com.google.gson.Gson;
import net.thenightwolf.dm.android.endpoint.request.Authenticator;
import net.thenightwolf.dm.android.endpoint.request.StandardRequestHandler;
import net.thenightwolf.dm.android.generator.manifest.IManifestGenerator;
import net.thenightwolf.dm.android.utils.SessionUtils;
import net.thenightwolf.dm.common.model.Manifest;
import net.thenightwolf.dm.android.generator.manifest.ManifestGenerator;
import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.response.IStatus;
import org.nanohttpd.protocols.http.response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class ManifestRequestHandler extends StandardRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(ManifestRequestHandler.class);

    private IManifestGenerator manifestGenerator;
    private Status status;

    @Override
    public String getMimeType() {
        return "application/json";
    }

    @Override
    public String getText() {
        return SessionUtils.buildError("400", "Must be off method type POST!");
    }

    @Override
    public IStatus getStatus() {
        return status;
    }

    @Override
    public String postMethod(Map<String, String> params, IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
        logger.info("Checking authentication...");
        Authenticator auth = new Authenticator();
        if(auth.authenticate(session)) {
            Manifest manifest = manifestGenerator.generate();
            status = Status.OK;
            return new Gson().toJson(manifest);
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
}
