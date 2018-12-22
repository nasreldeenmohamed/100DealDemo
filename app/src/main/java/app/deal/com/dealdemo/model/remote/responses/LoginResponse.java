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
    @SerializedName("user_id")
    @Expose
    private String userID;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}