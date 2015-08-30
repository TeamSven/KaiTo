package io.github.dnivra26.kaito;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import io.github.dnivra26.kaito.adapter.FoodItemAdapter;
import io.github.dnivra26.kaito.adapter.ReviewAdapter;
import io.github.dnivra26.kaito.models.Vandi;
import io.github.dnivra26.kaito.models.VandiRating;
import io.github.dnivra26.kaito.models.VandiReview;

/**
 * Created by ganesshkumar on 30/08/15.
 */
@EActivity(R.layout.vandi_detail)
public class VandiDetailActivity extends AppCompatActivity {

    private String vandiId;
    private Vandi vandi;

    @ViewById(R.id.vandi_image)
    ParseImageView vandiImage;

    @ViewById(R.id.vandi_name)
    TextView vandiName;

    @ViewById(R.id.vandi_location)
    TextView vandiLocation;

    @ViewById(R.id.vandi_avg_rating)
    RatingBar vandiAverageRating;

    @ViewById(R.id.user_rating)
    RatingBar userRating;

    @ViewById(R.id.menu)
    ListView menu;

    @ViewById(R.id.kadai_review)
    ListView kadaiReview;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.add_review)
    Button addReview;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vandiId = getIntent().getStringExtra("vandi_id");
    }

    @Click(R.id.add_review)
    public void addReview() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Add Review")
                .setView(R.layout.add_review)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        progressDialog = UiUtil.buildProgressDialog(VandiDetailActivity.this);
                        progressDialog.show();
                        TextView review = (TextView) alertDialog.findViewById(R.id.user_review);
                        final RatingBar rating = (RatingBar) alertDialog.findViewById(R.id.kadai_rating);

                        VandiReview vandiReview = new VandiReview();
                        vandiReview.setReview(review.getText().toString());
                        vandiReview.setUser(ParseUser.getCurrentUser());
                        vandiReview.setVandi(vandi);
                        vandiReview.setVandiId(vandi.getObjectId());
                        vandiReview.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    VandiRating vandiRating = new VandiRating();
                                    vandiRating.setUser(ParseUser.getCurrentUser());
                                    vandiRating.setVandiId(vandi.getObjectId());
                                    vandiRating.setVandi(vandi);
                                    vandiRating.setRating((int) rating.getRating());
                                    vandiRating.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                Toast.makeText(VandiDetailActivity.this, "Review!", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(VandiDetailActivity.this, "Failed!", Toast.LENGTH_LONG).show();
                                                ParseException parseException = e;
                                                Log.d("Failed", e.getMessage());
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(VandiDetailActivity.this, "Failed!", Toast.LENGTH_LONG).show();
                                    ParseException parseException = e;
                                    Log.d("Failed", e.getMessage());
                                }

                                progressDialog.dismiss();
                            }


                        });


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

    @AfterViews
    void init() {

        if (toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.title_vandi_detail));
            setSupportActionBar(toolbar);
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vandi");
        query.whereEqualTo("objectId", vandiId);
        final ProgressDialog progressDialog = UiUtil.buildProgressDialog(this);
        progressDialog.show();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject v : list) {
                        vandi = (Vandi) v;
                        break;
                    }

                    ParseFile photoFile = vandi.getParseFile("photo");
                    if (photoFile != null) {
                        vandiImage.setParseFile(photoFile);
                        vandiImage.loadInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                // nothing to do
                            }
                        });
                    }

                    vandiName.setText(vandi.getName());
                    vandiLocation.setText(vandi.getLocation().toString());
                    vandiAverageRating.setRating((float) vandi.getAvgRating());
                    //userRating.setRating();
                    menu.setAdapter(new FoodItemAdapter(getApplicationContext(), vandiId));
                    kadaiReview.setAdapter(new ReviewAdapter(getApplicationContext(), vandiId));
                }
                progressDialog.dismiss();
            }
        });

    }
}
