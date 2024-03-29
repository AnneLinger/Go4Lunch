package model.placedetailspojo;

import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import model.nearbysearchpojo.Viewport;

/**
 * Pojo model for geometry place details
 */

public class Geometry {

    @SerializedName("location")
    @Expose
    private Location location;

    @SerializedName("viewport")
    @Expose
    private Viewport viewport;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

}
