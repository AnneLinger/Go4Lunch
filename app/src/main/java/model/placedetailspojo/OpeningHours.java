package model.placedetailspojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.Period;
import java.util.List;

/**
 * Pojo model for opening hours place details
 */

public class OpeningHours {

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;

    @SerializedName("periods")
    @Expose
    private List<Period> periods = null;

    @SerializedName("weekday_text")
    @Expose
    private List<String> weekdayText = null;

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public List<String> getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(List<String> weekdayText) {
        this.weekdayText = weekdayText;
    }

}
