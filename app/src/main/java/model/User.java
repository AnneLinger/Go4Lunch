package model;
/**
*Model for users of the app
*/
public class User {
    private String userId;
    private String name;
    private String pictureUrl;

    public User(String userId, String name, String pictureUrl) {
        this.userId = userId;
        this.name = name;
        this.pictureUrl = pictureUrl;
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

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
