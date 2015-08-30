package io.github.dnivra26.kaito.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("StomachRating")
public class StomachRating extends ParseObject {

    public int getRating() {
        return getInt("rating");
    }

    public void setRating(int rating) {
        put("rating", rating);
    }

    public void setUser(ParseUser parseUser) {
        put("userId", parseUser.getObjectId());

    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setVandiId(String vandiId) {
        put("vandiId", vandiId);
    }
}
