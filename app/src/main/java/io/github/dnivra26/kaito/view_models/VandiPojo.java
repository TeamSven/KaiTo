package io.github.dnivra26.kaito.view_models;

import java.util.List;


public class VandiPojo {
    String vandiName;
    float spiceLevel;
    float rating;
    String vandiLocation;
    String userReview;
    byte[] vandiPhotoByteArray;
    List<MenuItemPojo> menuItems;

    public VandiPojo(String vandiName, float spiceLevel, float rating, String vandiLocation, String userReview, byte[] vandiPhotoByteArray, List<MenuItemPojo> menuItems) {
        this.vandiName = vandiName;
        this.spiceLevel = spiceLevel;
        this.rating = rating;
        this.vandiLocation = vandiLocation;
        this.userReview = userReview;
        this.vandiPhotoByteArray = vandiPhotoByteArray;
        this.menuItems = menuItems;
    }

    public String getVandiName() {
        return vandiName;
    }

    public void setVandiName(String vandiName) {
        this.vandiName = vandiName;
    }

    public float getSpiceLevel() {
        return spiceLevel;
    }

    public void setSpiceLevel(float spiceLevel) {
        this.spiceLevel = spiceLevel;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getVandiLocation() {
        return vandiLocation;
    }

    public void setVandiLocation(String vandiLocation) {
        this.vandiLocation = vandiLocation;
    }

    public String getUserReview() {
        return userReview;
    }

    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }

    public byte[] getVandiPhotoByteArray() {
        return vandiPhotoByteArray;
    }

    public void setVandiPhotoByteArray(byte[] vandiPhotoByteArray) {
        this.vandiPhotoByteArray = vandiPhotoByteArray;
    }

    public List<MenuItemPojo> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItemPojo> menuItems) {
        this.menuItems = menuItems;
    }
}
