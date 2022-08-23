package model;

import android.net.Uri;

import java.util.List;

import javax.annotation.Nullable;

/**
*Model for users of the app
*/
public class User {
    private String userId;
    private String name;
    @Nullable
    private String pictureUrl;
    @Nullable
    private List<String> likedPlaces;

    public User(){
    }

    public User(String userId, String name, @androidx.annotation.Nullable String pictureUrl, @androidx.annotation.Nullable List<String> likedPlaces) {
        this.userId = userId;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.likedPlaces = likedPlaces;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @androidx.annotation.Nullable
    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(@androidx.annotation.Nullable String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Nullable
    public List<String> getLikedPlaces() {
        return likedPlaces;
    }

    public void setLikedPlaces(@Nullable List<String> likedPlaces) {
        this.likedPlaces = likedPlaces;
    }
}

