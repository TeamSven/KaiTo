package io.github.dnivra26.kaito.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by ganesshkumar on 29/08/15.
 */

@ParseClassName("VandiReview")
public class VandiReview extends ParseObject {
    public void setVandi(Vandi vandi) {
        setVandiId(vandi.getObjectId());
        put("vandi", vandi);
    }

    public Vandi getVandi() {
        return (Vandi) getParseObject("vandi");
    }

    public String getReview() {
        return getString("review");
    }

    public void setReview(String review) {
        put("review", review);
    }

    public void setUser(ParseUser parseUser) {
        put("user", parseUser);
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setVandiId(String vandiId) {
        put("vandiId", vandiId);
    }
}
