package io.github.dnivra26.kaito;

import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import io.github.dnivra26.kaito.models.Vandi;
import io.github.dnivra26.kaito.models.VandiRating;
import io.github.dnivra26.kaito.models.VandiReview;
import io.github.dnivra26.kaito.view_models.VandiPojo;

public class ParseHelper {
    public static void addVandi(Context context, final VandiPojo vandiPojo, final KadaiCreationCallback kadaiCreationCallback) {
        final Vandi vandi = new Vandi();
        vandi.setName(vandiPojo.getVandiName());
        vandi.setSpiceLevel((int) vandiPojo.getSpiceLevel());
        String[] locations = vandiPojo.getVandiLocation().split(",");
        vandi.setLocation(new ParseGeoPoint(Double.parseDouble(locations[0]), Double.parseDouble(locations[1])));
        final ParseFile parseFile = new ParseFile("kadai_image.jpg", vandiPojo.getVandiPhotoByteArray());
        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    vandi.setPhotoFile(parseFile);
                    vandi.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                VandiReview vandiReview = new VandiReview();
                                vandiReview.setReview(vandiPojo.getUserReview());
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
                                            vandiRating.setRating((int) vandiPojo.getRating());
                                            vandiRating.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        kadaiCreationCallback.onSuccess();
                                                    } else {
                                                        kadaiCreationCallback.onFailure();
                                                    }
                                                }
                                            });
                                        } else {
                                            kadaiCreationCallback.onFailure();
                                        }
                                    }
                                });


                            } else {
                                kadaiCreationCallback.onFailure();
                            }
                        }
                    });
                } else {
                    kadaiCreationCallback.onFailure();
                }
            }
        });
    }
}
