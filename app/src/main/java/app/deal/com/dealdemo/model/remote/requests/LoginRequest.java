package app.deal.com.dealdemo.model.remote.requests;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Patterns;

import app.deal.com.dealdemo.BR;

/**
 * Created by Misteka on 11/30/2018.
 */

public class LoginRequest extends BaseObservable {

    String name;
    String password;
    String login;
    String mobile_token;
    boolean isSocialMediaLogin;

    @Bindable
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
        notifyPropertyChanged(BR.emailError);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
        notifyPropertyChanged(BR.error);

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMobile_token() {
        return mobile_token;
    }

    public void setMobile_token(String mobile_token) {
        this.mobile_token = mobile_token;
    }


    public boolean isSocialMediaLogin() {
        return isSocialMediaLogin;
    }

    public void setSocialMediaLogin(boolean socialMediaLogin) {
        isSocialMediaLogin = socialMediaLogin;
    }

    @Bindable
    public String getError() {
        if (password == null || password.length() < 5)
            return "Too short!";
        return null;


    }

    @Bindable
    public String getEmailError() {

        if (name == null)
            return "Please Enter username or email";

        if (!Patterns.EMAIL_ADDRESS.matcher(name).matches())
            return "Please, Eneter a valid Email";

        return null;


    }
}