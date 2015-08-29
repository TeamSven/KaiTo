package io.github.dnivra26.kaito.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by ganesshkumar on 29/08/15.
 */

@ParseClassName("Vandi")
public class Vandi extends ParseObject {

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint location) {
        put("location", location);
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }

    public void setPhotoFile(ParseFile photo) {
        put("photo", photo);
    }

    public void setSpiceLevel(int level) {
        put("spiceLevel", level);
    }
    
    public void set(int level) {
        put("spiceLevel", level);
    }
}
