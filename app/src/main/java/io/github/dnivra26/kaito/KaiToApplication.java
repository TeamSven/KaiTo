package io.github.dnivra26.kaito;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class KaiToApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "UpGUiLqb4Kz2IbuNGRxloKbEnZmR6aUExSSucmSd", "B3oYsXxVZoBaqw3VQPSodhO2VuOrVnmAvCOoxwBu");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
}
