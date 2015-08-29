package io.github.dnivra26.kaito.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by ganesshkumar on 29/08/15.
 */

@ParseClassName("FoodItem")
public class FoodItem extends ParseObject {
    public void setName(String name) {
        put("name", name);
    }

    public String getName() {
        return getString("name");
    }

    public void setPrice(int price) {
        put("price", price);
    }

    public int getPrice() {
        return getInt("prince");
    }

    public void setVandi(Vandi vandi) {
        setVandiId(vandi.getObjectId());
        put("vandi", vandi);
    }

    public void setTotalRating(int totalRating) {
        put("totalRating", totalRating);
    }

    public int getTotalRating() {
        return getInt("totalRating");
    }

    public void setNumberOfRating(int numberOfRating) {
        put("numberOfRating", numberOfRating);
    }

    public int getNumberOfRating() {
        return getInt("numberOfRating");
    }


    public Vandi getVandi() {
        return (Vandi) getParseObject("vandi");
    }

    public void setVandiId(String vandiId) {
        put("vandiId", vandiId);
    }
}