package app.deal.com.dealdemo.model.remote.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Misteka on 12/11/2018.
 */

public class NotificationTokenRequest {

    @SerializedName("androidui")
    @Expose
    String androidui;
    @SerializedName("update_android_token")
    @Expose
    String update_android_token;
    @SerializedName("token")
    @Expose
    String token;


    public String getAndroidui() {
        return androidui;
    }

    public void setAndroidui(String androidui) {
        this.androidui = androidui;
    }

    public String getUpdate_android_token() {
        return update_android_token;
    }

    public void setUpdate_android_token(String update_android_token) {
        this.update_android_token = update_android_token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
