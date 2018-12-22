package app.deal.com.dealdemo.model.remote.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Roles {

    @SerializedName("2")
    @Expose
    private String _2;
    @SerializedName("7")
    @Expose
    private String _7;

    public String get2() {
        return _2;
    }

    public void set2(String _2) {
        this._2 = _2;
    }

    public String get7() {
        return _7;
    }

    public void set7(String _7) {
        this._7 = _7;
    }

}