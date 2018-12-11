package app.deal.com.dealdemo.model.remote.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Misteka on 11/30/2018.
 */

public class LoginResponse {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("userID")
    @Expose
    private Integer userID;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }
}
