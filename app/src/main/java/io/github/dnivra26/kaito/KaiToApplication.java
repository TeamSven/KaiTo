package io.github.dnivra26.kaito;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import io.github.dnivra26.kaito.models.FoodItem;
import io.github.dnivra26.kaito.models.FoodRating;
import io.github.dnivra26.kaito.models.Vandi;
import io.github.dnivra26.kaito.models.VandiRating;
import io.github.dnivra26.kaito.models.VandiReview;

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

        ParseObject.registerSubclass(Vandi.class);
        ParseObject.registerSubclass(VandiReview.class);
        ParseObject.registerSubclass(VandiRating.class);
        ParseObject.registerSubclass(FoodItem.class);
        ParseObject.registerSubclass(FoodRating.class);
    }
}
