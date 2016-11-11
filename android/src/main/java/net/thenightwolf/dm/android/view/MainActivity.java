package net.thenightwolf.dm.android.view;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import net.thenightwolf.dm.android.DMApplication;
import net.thenightwolf.dm.android.R;
import net.thenightwolf.dm.android.endpoint.information.AuthenticationRequestHandler;
import net.thenightwolf.dm.android.endpoint.information.InformationRequestHandler;
import net.thenightwolf.dm.android.endpoint.information.ManifestRequestHandler;
import net.thenightwolf.dm.android.endpoint.information.UpdatesRequestHandler;
import net.thenightwolf.dm.android.endpoint.send.SMSSendRequestHandler;
import net.thenightwolf.dm.android.endpoint.thread.DefaultThreadRequestHandler;
import net.thenightwolf.dm.android.endpoint.thread.SpecificThreadRequestHandler;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.router.RouterNanoHTTPD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

    private static List<String> contacts = Arrays.asList("Jordan Knott", "Jenny Clopton", "Someone who");
    private static List<String> threads = Arrays.asList("913-323-232", "112-233-3123");

    private static final String VERSION = "0.0.0-ALPHA";

    @BindView(R.id.ipAddress)
    public TextView ipAddress;

    @BindView(R.id.version)
    public TextView version;

    @BindView(R.id.my_toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        logger.info("Creating view");

        ipAddress.setText(getIP());
        version.setText(VERSION);

        new ServerTask().execute();
    }



    public class ServerTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                new Nanolets().start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class Nanolets extends RouterNanoHTTPD {
        public Nanolets(){
            super(5001);
            /*
            try {
                super.makeSecure(NanoHTTPD.makeSSLSocketFactory("/cert/keystore.jks", "debugkey".toCharArray()), null);
            } catch (IOException e) {
                logger.error("IO Exception while loading keystore", e);
            }
            */
            addMappings();
            logger.info("Nanolet running! Running on port: 5001");
        }

        @Override
        public void addMappings(){
            super.addMappings();
            addRoute("/", InformationRequestHandler.class);
            addRoute("/", InformationRequestHandler.class);
            addRoute("/login", AuthenticationRequestHandler.class);
            addRoute("/manifest", ManifestRequestHandler.class);
            addRoute("/sms", SMSSendRequestHandler.class);
            addRoute("/thread", DefaultThreadRequestHandler.class);
            addRoute("/thread/:id", SpecificThreadRequestHandler.class);
            addRoute("/updates", UpdatesRequestHandler.class);
        }
    }

    private String getIP(){
        WifiManager wm = (WifiManager) DMApplication.getAppContext().getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                Log.i("Hello", "hello!");
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
}
