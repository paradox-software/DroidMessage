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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class AuthenticationRequestHandler extends StandardRequestHandler {

    private Status status;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationRequestHandler.class);

        @Override
        public String postMethod(Map<String, String> params, IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DMApplication.getAppContext());
            if(SessionUtils.keyExists("password", session)) {
                String attempt = SessionUtils.getSingleParam("password", session);
                if (attempt != null && attempt.equals(preferences.getString("password_hash", null))) {
                    logger.info("Login successful! 200 OK code returned");
                    status = Status.OK;
                    return new Gson().toJson(TokenGenerator.generateToken());
                }
            }
            logger.info("Login failed! 403 FORBIDDEN code returned");
            status = Status.FORBIDDEN;
            return "";
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
        public Status getStatus() {
            return status;
        }
        }