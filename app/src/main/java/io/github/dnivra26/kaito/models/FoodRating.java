package io.github.dnivra26.kaito.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("FoodRating")
public class FoodRating extends ParseObject {
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

    public void setFoodItem(FoodItem foodItem) {
        setFoodItemId(foodItem.getObjectId());
        put("foodItem", foodItem);
    }


    public FoodItem getFoodItem() {
        return (FoodItem) getParseObject("foodItem");
    }


    public void setFoodItemId(String foodItemId) {
        put("foodItemId", foodItemId);
    }
}
