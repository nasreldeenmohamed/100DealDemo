package app.deal.com.dealdemo.model;

import android.text.TextUtils;

/**
 * Created by Wafaa Aly on 12/16/2018.
 */
public class RegisterInfo extends MainUserModel {
    public String name;

    public RegisterInfo(String name, String email, String password) {
        super(email, password);
        setName(name);
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNameValid() {
        return !TextUtils.isEmpty(getName());
    }

    public boolean isRegistrationInfoValid() {
//        return isNameValid() && isEmailValid() && isPasswordLengthGreaterThan5();
        return true;
    }
}
