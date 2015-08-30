package io.github.dnivra26.kaito;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.dnivra26.kaito.view_models.MenuItemPojo;
import io.github.dnivra26.kaito.view_models.VandiPojo;

@EActivity(R.layout.activity_new_kadai)
public class NewKadaiActivity extends AppCompatActivity implements KadaiCreationCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int IMAGE_REQUEST_CODE = 111;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.kadai_image)
    ImageView kadaiImage;

    @ViewById(R.id.kadai_title)
    EditText kadaiTitle;

    @ViewById(R.id.kadai_menu)
    EditText kadaiMenu;

    @ViewById(R.id.user_review)
    EditText userReview;

    @ViewById(R.id.kadai_location)
    EditText kadaiLocation;

    @ViewById(R.id.spice_rating)
    RatingBar spiceRating;

    @ViewById(R.id.kadai_rating)
    RatingBar kadaiRating;

    @ViewById(R.id.add_menu)
    Button addMenu;

    List<MenuItemPojo> menuItemPojoList = new ArrayList<>();
    private byte[] imageViewByteArray;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @AfterViews
    public void setupToolbar() {
        buildGoogleApiClient();
        if (toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.title_activity_new_kadai));
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

    @Click(R.id.kadai_image)
    public void takePicture() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    @Click(R.id.add_menu)
    public void addMenu() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Add Item")
                .setView(R.layout.add_menu)
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextView menuName = (TextView) alertDialog.findViewById(R.id.menu_name);
                        TextView menuPrice = (TextView) alertDialog.findViewById(R.id.menu_price);
                        RatingBar menuRating = (RatingBar) alertDialog.findViewById(R.id.menu_rating);
                        float rating = menuRating.getRating();
                        String name = menuName.getText().toString();
                        int price = Integer.parseInt(menuPrice.getText().toString());
                        menuItemPojoList.add(new MenuItemPojo(name, price, rating));
                        kadaiMenu.setText(kadaiMenu.getText().append(name + "\n"));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_kadai, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && data.getExtras() != null) {

            Bitmap bp = (Bitmap) data.getExtras().get("data");
            kadaiImage.setImageBitmap(bp);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imageViewByteArray = stream.toByteArray();


            ParseFile parseFile = new ParseFile("kadai_image.jpg", imageViewByteArray);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_submit) {
            String kadaiName = kadaiTitle.getText().toString();
            float spiceRating = this.spiceRating.getRating();
            String kadaiLocat = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();
            String userReview = this.userReview.getText().toString();
            float kadaiRating = this.kadaiRating.getRating();
            String address = kadaiLocation.getText().toString();
            VandiPojo vandiPojo = new VandiPojo(kadaiName, spiceRating, kadaiRating, kadaiLocat, userReview, imageViewByteArray, menuItemPojoList, address);
            progressDialog = UiUtil.buildProgressDialog(this);
            progressDialog.show();
            ParseHelper.addVandi(this, vandiPojo, this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess() {
        progressDialog.dismiss();
        Toast.makeText(this, "Shop added!", Toast.LENGTH_LONG).show();

        finish();
    }

    @Override
    public void onFailure() {
        progressDialog.dismiss();
        Toast.makeText(this, "Shop add failed!", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            ParseGeoPoint geoPoint = new ParseGeoPoint(0, 0);
            geoPoint.setLatitude(mLastLocation.getLatitude());
            geoPoint.setLongitude(mLastLocation.getLongitude());
            String address = convertToAddress(geoPoint);
            kadaiLocation.setText(address);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public String convertToAddress(ParseGeoPoint geoPoint) {
        String address = null;
        Geocoder geocoder;
        geocoder = new Geocoder(this);
        String city = null;
        List<Address> fromLocation = null;
        try {
            fromLocation = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        address = fromLocation.get(0).getAddressLine(0);
        city = fromLocation.get(0).getAddressLine(1);

        return address + "," + city;
    }
}
