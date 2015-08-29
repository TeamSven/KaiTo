package io.github.dnivra26.kaito.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by ganesshkumar on 29/08/15.
 */

@ParseClassName("VandiRating")
public class VandiRating extends ParseObject {
    public void setVandi(Vandi vandi) {
        setVandiId(vandi.getObjectId());
        put("vandi", vandi);
    }

    public Vandi getVandi() {
        return (Vandi) getParseObject("vandi");
    }

    public int getRating() {
        return getInt("rating");
    }

    public void setRating(int rating) {
        put("rating", rating);
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
