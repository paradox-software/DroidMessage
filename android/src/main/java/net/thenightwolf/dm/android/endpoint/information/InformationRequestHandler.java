package net.thenightwolf.dm.android.endpoint.information;

import com.google.gson.Gson;
import net.thenightwolf.dm.android.generator.information.IServerInformationGenerator;
import net.thenightwolf.dm.android.generator.information.ServerInformationGenerator;
import org.nanohttpd.protocols.http.response.IStatus;
import org.nanohttpd.protocols.http.response.Status;
import org.nanohttpd.router.RouterNanoHTTPD;

public class InformationRequestHandler extends RouterNanoHTTPD.DefaultHandler {

    private IServerInformationGenerator serverInformationGenerator;

    @Override
    public String getText() {
        return new Gson().toJson(serverInformationGenerator.getServerInformation());
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
