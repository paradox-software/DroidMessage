package net.thenightwolf.dm.android.generator.authentication;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import net.thenightwolf.dm.android.DMApplication;
import net.thenightwolf.dm.android.TokenGenerator;
import net.thenightwolf.dm.android.utils.SessionUtils;
import org.nanohttpd.protocols.http.response.Status;

public class AuthenticationGenerator implements IAuthGenerator {

    @Override
    public boolean isValid(String passwordAttempt) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DMApplication.getAppContext());
        if (passwordAttempt != null && passwordAttempt.equals(preferences.getString("password_hash", null))) {
            return true;
        }
        return false;
    }

    @Override
    public String getToken() {
        return new Gson().toJson(TokenGenerator.generateToken());
    }
}
