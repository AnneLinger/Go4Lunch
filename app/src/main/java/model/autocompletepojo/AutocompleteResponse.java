package model.autocompletepojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Pojo model for geometry autocomplete
 */

public class AutocompleteResponse {

    @SerializedName("predictions")
    @Expose
    private List<Prediction> predictions;

    @SerializedName("status")
    @Expose
    private String status;

    public List<model.autocompletepojo.Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
