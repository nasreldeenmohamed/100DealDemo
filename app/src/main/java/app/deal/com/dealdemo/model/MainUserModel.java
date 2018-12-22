package app.deal.com.dealdemo.model;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by Wafaa Aly on 12/16/2018.
 */
public class MainUserModel {

    private String email;
    private String password;


    public MainUserModel(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getEmail() {
        if (email == null) {
            return "";
        }
        return email;
    }


    public String getPassword() {

        if (password == null) {
            return "";
        }
        return password;
    }



    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches();
    }


    public boolean isPasswordLengthGreaterThan5() {
        return getPassword().length() > 5;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
