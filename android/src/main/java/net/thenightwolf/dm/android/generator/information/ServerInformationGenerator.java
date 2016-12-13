package net.thenightwolf.dm.android.generator.information;

import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import com.google.gson.Gson;
import net.thenightwolf.dm.android.DMApplication;
import net.thenightwolf.dm.common.model.ServerInformation;

import static android.content.Context.WIFI_SERVICE;

public class ServerInformationGenerator implements IServerInformationGenerator {

    public ServerInformation getServerInformation() {
        ServerInformation information = new ServerInformation();
        information.hostname = "null";
        information.version = DMApplication.getVERSION();
        information.IPAddress = getIP();

        return information;
    }

    private String getIP(){
        WifiManager wm = (WifiManager) DMApplication.getAppContext().getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }
}
