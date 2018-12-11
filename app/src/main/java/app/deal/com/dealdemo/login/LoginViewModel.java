package app.deal.com.dealdemo.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.AndroidViewModel;

import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.os.Handler;
import android.support.annotation.NonNull;

import app.deal.com.dealdemo.model.User;
import app.deal.com.dealdemo.model.remote.requests.LoginRequest;
import app.deal.com.dealdemo.model.remote.responses.LoginResponse;
import app.deal.com.dealdemo.services.RetrofitRepository;

/**
 * Created by Misteka on 11/30/2018.
 */

public class LoginViewModel extends AndroidViewModel {

    public MutableLiveData<String> errorPassword = new MutableLiveData<>();
    public MutableLiveData<String> errorEmail = new MutableLiveData<>();

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<Integer> busy;



    private final LiveData<LoginResponse> loginResponseObservable;




    public ObservableField<LoginResponse> loginResponseField = new ObservableField<>();


    public MutableLiveData<Integer> getBusy() {

        if (busy == null) {
            busy = new MutableLiveData<>();
            busy.setValue(8);
        }
        return busy;
    }

    public LoginViewModel(@NonNull Application application, LoginRequest loginRequest) {
        super(application);

        this.loginResponseObservable = RetrofitRepository.getInstance().performLogin(loginRequest);

    }


    public LiveData<LoginResponse> getObservableLoginResponse() {
        return loginResponseObservable;
    }

    public void setLoginResponse(LoginResponse loginResponse) {
        this.loginResponseField.set(loginResponse);
    }


    private MutableLiveData<User> userMutableLiveData;

    LiveData<User> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }

        return userMutableLiveData;
    }


    public void onLoginClicked() {

        getBusy().setValue(0); //View.VISIBLE
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                User user = new User(email.getValue(), password.getValue());

                if (!user.isEmailValid()) {
                    errorEmail.setValue("Enter a valid email address");
                } else {
                    errorEmail.setValue(null);
                }

                if (!user.isPasswordLengthGreaterThan5())
                    errorPassword.setValue("Password Length should be greater than 5");
                else {
                    errorPassword.setValue(null);
                }

                userMutableLiveData.setValue(user);
                busy.setValue(8); //8 == View.GONE

            }
        }, 3000);
    }


    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final LoginRequest loginRequest;

        public Factory(@NonNull Application application, LoginRequest loginRequest) {
            this.application = application;
            this.loginRequest = loginRequest;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new LoginViewModel(application, loginRequest);
        }
    }
}
