/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.android.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;
import net.thenightwolf.dm.android.R;

/**
 * Created by burni_000 on 9/15/2016.
 */
public class SplashActivity extends AwesomeSplash {

    private final int DISPLAY_LENGTH = 2000;
    private final int LOGO_HEIGHT = 400;
    private final int LOGO_WIDTH = 400;

    @Override
    public void initSplash(ConfigSplash configSplash) {

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }

        configSplash.setBackgroundColor(R.color.colorPrimary);
        configSplash.setAnimCircularRevealDuration(DISPLAY_LENGTH - 1000);
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

        configSplash.setPathSplash(getString(R.string.logo_svg_path));
        configSplash.setOriginalHeight(LOGO_HEIGHT);
        configSplash.setOriginalWidth(LOGO_WIDTH);
        configSplash.setAnimPathStrokeDrawingDuration(DISPLAY_LENGTH);
        configSplash.setPathSplashStrokeSize(5);
        configSplash.setPathSplashStrokeColor(R.color.logoStroke);
        configSplash.setAnimPathFillingDuration(DISPLAY_LENGTH);
        configSplash.setPathSplashFillColor(R.color.logoFill);

        configSplash.setTitleSplash("DroidMessage");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(45f); //float value
        configSplash.setAnimTitleDuration(DISPLAY_LENGTH);
        configSplash.setAnimTitleTechnique(Techniques.SlideInUp);
        configSplash.setTitleFont("fonts/Infinity.ttf");
    }

    @Override
    public void animationsFinished() {


        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        boolean isFirstStart = prefs.getBoolean("firstStart", true);

        if(isFirstStart) {
            Intent mainIntent = new Intent(this, IntroActivity.class);
            startActivity(mainIntent);
        } else {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);

        }


        finish();
    }
}
