package net.thenightwolf.dm.android.endpoint.information;

import net.thenightwolf.dm.android.information.ServerInformationGenerator;
import org.nanohttpd.protocols.http.response.IStatus;
import org.nanohttpd.protocols.http.response.Status;
import org.nanohttpd.router.RouterNanoHTTPD;

public class InformationRequestHandler extends RouterNanoHTTPD.DefaultHandler {
    @Override
    public String getText() {
        return new ServerInformationGenerator().getInformationJson();
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
