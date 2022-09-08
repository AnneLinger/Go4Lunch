package model.autocompletepojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Pojo model for main text matched substring autocomplete
 */

public class MainTextMatchedSubstring {

    @SerializedName("length")
    @Expose
    private Integer length;

    @SerializedName("offset")
    @Expose
    private Integer offset;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
