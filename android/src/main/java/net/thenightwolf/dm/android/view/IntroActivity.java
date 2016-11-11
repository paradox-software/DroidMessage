package net.thenightwolf.dm.android.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.Window;
import android.view.WindowManager;
import com.github.paolorotolo.appintro.AppIntro;
import net.thenightwolf.dm.android.R;
import net.thenightwolf.dm.android.view.fragments.intro.FinishedSlideFragment;
import net.thenightwolf.dm.android.view.fragments.intro.PermissionSlideFragment;
import net.thenightwolf.dm.android.view.fragments.intro.SecuritySlideFragment;
import net.thenightwolf.dm.android.view.fragments.intro.WelcomeSlideFragment;

public class IntroActivity extends AppIntro {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }

        addSlide(new WelcomeSlideFragment());
        addSlide(new PermissionSlideFragment());
        addSlide(new SecuritySlideFragment());
        addSlide(new FinishedSlideFragment());

        askForPermissions(getPermissionList(), 2);

        showSkipButton(false);

    }

    private void startMainActivity() {
        Intent target = new Intent(this, MainActivity.class);
        startActivity(target);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        SharedPreferences.Editor e = prefs.edit();
        e.putBoolean("firstStart", false);
        e.apply();

        startMainActivity();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

    private String[] getPermissionList(){
        return new String[] {
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_LOGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.RECEIVE_MMS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,

        };
    }
}
