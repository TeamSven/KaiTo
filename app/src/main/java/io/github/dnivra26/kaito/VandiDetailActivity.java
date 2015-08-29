package io.github.dnivra26.kaito;

import android.app.Activity;
import android.os.Bundle;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by ganesshkumar on 30/08/15.
 */
@EActivity(R.layout.vandi_detail)
public class VandiDetailActivity extends Activity {

    private String vandiId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vandiId = getIntent().getStringExtra("vandi_id");
    }

    @AfterViews
    void init() {

    }
}
