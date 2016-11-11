package net.thenightwolf.dm.android.information;

import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import com.google.gson.Gson;
import net.thenightwolf.dm.android.DMApplication;
import net.thenightwolf.dm.common.model.ServerInformation;
import org.nanohttpd.protocols.http.response.IStatus;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static android.content.Context.WIFI_SERVICE;

public class ServerInformationGenerator {

    public String getInformationJson() {
        ServerInformation information = new ServerInformation();
        information.hostname = "null";
        information.version = DMApplication.getVERSION();
        information.IPAddress = getIP();

        return new Gson().toJson(information);
    }

    private String getIP(){
        WifiManager wm = (WifiManager) DMApplication.getAppContext().getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }
}
