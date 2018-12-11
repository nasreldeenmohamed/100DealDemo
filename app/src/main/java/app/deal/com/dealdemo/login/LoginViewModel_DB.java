package app.deal.com.dealdemo.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.os.Handler;
import android.support.annotation.NonNull;

import app.deal.com.dealdemo.BR;
import app.deal.com.dealdemo.model.User;

/**
 * Created by Misteka on 11/30/2018.
 */

public class LoginViewModel_DB extends BaseObservable {
    private String email;
    private String password;
    private int busy = 8;
    // data binding rot email and password.
    // not life cycle aware
    public final ObservableField<String> errorPassword = new ObservableField<>();
    public final ObservableField<String> errorEmail = new ObservableField<>();

    public LoginViewModel_DB() {
    }

    private MutableLiveData<User> userMutableLiveData;

    LiveData<User> getUser() {
        if (userMutableLiveData == null)
            userMutableLiveData = new MutableLiveData<User>();
        return userMutableLiveData;
    }

    @Bindable
    @NonNull
    public String getEmail() {
        return this.email;
    }


    @Bindable
    @NonNull
    public String getPassword() {
        return this.password;
    }

    @Bindable
    @NonNull
    public int getBusy() {
        return this.busy;
    }

    public void setEmail(@NonNull String Email) {
        this.email = Email;
        notifyPropertyChanged(BR.email);

    }

    public void setPassword(@NonNull String Password) {
        this.email = Password;
        notifyPropertyChanged(BR.password);

    }

    public void setBusy(int Busy) {
        this.busy = Busy;
        notifyPropertyChanged(BR.busy);
    }

    public void onLoginClicked() {

        // indicator to make view visiable;
        setBusy(0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                User user = new User(getEmail(), getPassword());
                if (!user.isEmailValid()) {
                    errorEmail.set("Enter a valid email address");
                } else {
                    errorEmail.set(null);
                }
                if (!user.isPasswordLengthGreaterThan5()) {
                    errorPassword.set("Password Length should be greater than 5");
                } else {
                    errorPassword.set(null);
                }
                //  i wrap user object in mutable live data when any change in user obejct main activity
                // will be notified
                userMutableLiveData.setValue(user);
                setBusy(8); // indicate view gone.
            }
        }, 5000);
        // 5 sec
    }
}

