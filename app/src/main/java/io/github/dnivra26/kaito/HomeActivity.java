package io.github.dnivra26.kaito;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import io.github.dnivra26.kaito.models.KadaiListAdapter;
import io.github.dnivra26.kaito.models.Vandi;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

@EActivity(R.layout.activity_home)
public class HomeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.kadai_list)
    ListView kadaiList;

    @ViewById(R.id.create_new_kadai_fab)
    FloatingActionButton floatingActionButton;
    private AlertDialog alertDialog;
    private ReactiveLocationProvider reactiveLocationProvider;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private ParseGeoPoint currentGeoPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reactiveLocationProvider = new ReactiveLocationProvider(this);
    }

    @AfterViews
    public void setupToolbar() {
        buildGoogleApiClient();
        if (toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.title_activity_home));
            setSupportActionBar(toolbar);
        }


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Click(R.id.create_new_kadai_fab)
    public void createNewKadai() {
        startActivity(new Intent(HomeActivity.this, NewKadaiActivity_.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Click(R.id.button_nearby)
    public void buttonNearby() {
        startActivity(new Intent(this, MapActivity_.class));
    }

    @Click(R.id.button_search)
    public void buttonSearch() {
        startActivity(new Intent(HomeActivity.this, Placesearch_.class));
    }

    @Click(R.id.button_food)
    public void buttonFood() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Add Favorite Food")
                .setView(R.layout.add_favorite_food)
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextView menuName = (TextView) alertDialog.findViewById(R.id.fav_food_name);
                        addGeofence();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog = builder.create();

        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            final ProgressDialog progressDialog = UiUtil.buildProgressDialog(this);
            progressDialog.show();
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(),
                                "Successfully Logged Out",
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(HomeActivity.this, LoginSignupActivity_.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Log Out Error", Toast.LENGTH_LONG)
                                .show();
                    }
                    progressDialog.dismiss();
                }
            });

            return true;
        } else if (id == R.id.action_nearby) {
            startActivity(new Intent(this, MapActivity_.class));
        } else if (id == R.id.action_fav_food) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Add Favorite Food")
                    .setView(R.layout.add_favorite_food)
                    .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TextView menuName = (TextView) alertDialog.findViewById(R.id.fav_food_name);
                            addGeofence();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            alertDialog = builder.create();

            alertDialog.show();
        } else if (id == R.id.action_search_location) {
            startActivity(new Intent(HomeActivity.this, Placesearch_.class));
        }


        return super.onOptionsItemSelected(item);
    }

    private PendingIntent createNotificationBroadcastPendingIntent() {
        return PendingIntent.getBroadcast(this, 0, new Intent(this, GeofenceBroadcastReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private void addGeofence() {
        final GeofencingRequest geofencingRequest = createGeofencingRequest();
        if (geofencingRequest == null) return;

        final PendingIntent pendingIntent = createNotificationBroadcastPendingIntent();
        reactiveLocationProvider
                .removeGeofences(pendingIntent)
                .flatMap(new Func1<Status, Observable<Status>>() {
                    @Override
                    public Observable<Status> call(Status pendingIntentRemoveGeofenceResult) {
                        return reactiveLocationProvider.addGeofences(pendingIntent, geofencingRequest);
                    }
                })
                .subscribe(new Action1<Status>() {
                    @Override
                    public void call(Status addGeofenceResult) {
                        toast("Geofence added, success: " + addGeofenceResult.isSuccess());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        toast("Error adding geofence.");
                        Log.d("GMS", "Error adding geofence.", throwable);
                    }
                });
    }

    private GeofencingRequest createGeofencingRequest() {
        try {
            double longitude = 77.598353;
            double latitude = 12.978630;

            Geofence geofence = new Geofence.Builder()
                    .setRequestId("GEOFENCE")
                    .setCircularRegion(latitude, longitude, 1000)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();
            return new GeofencingRequest.Builder().addGeofence(geofence).build();
        } catch (NumberFormatException ex) {
            toast("Error parsing input.");
            return null;
        }
    }

    private void toast(String text) {
        Toast.makeText(HomeActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        currentGeoPoint = new ParseGeoPoint();
        currentGeoPoint.setLatitude(mLastLocation.getLatitude());
        currentGeoPoint.setLongitude(mLastLocation.getLongitude());

        final ProgressDialog progressDialog = UiUtil.buildProgressDialog(this);
        final KadaiListAdapter kadaiListAdapter = new KadaiListAdapter(this, currentGeoPoint);
        kadaiListAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Vandi>() {
            @Override
            public void onLoading() {
                progressDialog.show();
            }

            @Override
            public void onLoaded(List<Vandi> list, Exception e) {
                if (list.size() > 0) {
                    //noActiveIssuesLabel.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
            }
        });
        kadaiList.setAdapter(kadaiListAdapter);
        kadaiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this, VandiDetailActivity_.class);
                intent.putExtra("vandi_id", kadaiListAdapter.getItem(position).getObjectId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
