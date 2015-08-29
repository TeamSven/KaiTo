package io.github.dnivra26.kaito.view_models;

import lombok.Getter;
import lombok.Setter;



public class MenuItemPojo {

    public MenuItemPojo(String name, int price, float rating) {
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    String name;

    int price;
    float rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
