package net.thenightwolf.dm.android.endpoint.information;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.gson.Gson;
import net.thenightwolf.dm.android.DMApplication;
import net.thenightwolf.dm.android.R;
import net.thenightwolf.dm.android.TokenGenerator;
import net.thenightwolf.dm.android.endpoint.request.StandardRequestHandler;
import net.thenightwolf.dm.android.utils.SessionUtils;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.response.IStatus;
import org.nanohttpd.protocols.http.response.Status;

import java.io.IOException;
import java.util.Map;

public class AuthenticationRequestHandler extends StandardRequestHandler {

            @Override
            public String postMethod(Map<String, String> params, IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DMApplication.getAppContext());
                if(SessionUtils.keyExists("password", session)) {
                    Log.i("Auth", "Checking password: " + SessionUtils.getSingleParam("password", session) + " to " + preferences.getString("password_hash", null));
                    String attempt = SessionUtils.getSingleParam("password", session);
                    if (attempt != null && attempt.equals(preferences.getString("password_hash", null))) {
                        return new Gson().toJson(TokenGenerator.generateToken());
                    }
                }
                return new Gson().toJson("declined");
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
                return Status.OK;
            }
        }