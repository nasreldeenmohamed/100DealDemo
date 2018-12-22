package app.deal.com.dealdemo.login;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import app.deal.com.dealdemo.main.MainActivity;
import app.deal.com.dealdemo.model.remote.requests.LoginRequest;
import app.deal.com.dealdemo.model.remote.requests.RegisterRequest;
import app.deal.com.dealdemo.model.remote.responses.LoginResponse;
import app.deal.com.dealdemo.register.RegisterViewModel;
import app.deal.com.dealdemo.services.RetrofitRepository;
import app.deal.com.dealdemo.utils.SharedPref;

/**
 * Created by Misteka on 11/30/2018.
 */

public class LoginViewModel extends AndroidViewModel {


    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public LiveData<LoginResponse> loginResponseObservable = new MutableLiveData<>();
    LoginRequest loginRequest;
    Context Contextt;

    public LoginViewModel(@NonNull Application application, LoginRequest loginRequest, Context context) {
        super(application);

        this.loginRequest = loginRequest;
        Contextt = context;
//        loginResponseObservable = RetrofitRepository.getInstance().performLogin(loginRequest);
    }


    public LiveData<LoginResponse> getObservableLoginResponse() {
        return loginResponseObservable;
    }


    public MutableLiveData<String> getEmail() {
        return email;
    }


    public LiveData<LoginResponse> onLoginClicked() {
        Log.i("loginclicked", "clicked");

        loginResponseObservable = RetrofitRepository.getInstance().performLogin(loginRequest);


        loginResponseObservable.observeForever(new Observer<LoginResponse>() {
            @Override
            public void onChanged(@Nullable LoginResponse loginResponse) {
                if (loginResponse != null) {
                    Log.i("nasr", "nasr is " + loginResponse.getResult());

                }

            }
        });


        return loginResponseObservable;

       /* loginResponseObservable.observeForever(new Observer<LoginResponse>() {
            @Override
            public void onChanged(@Nullable LoginResponse loginResponse) {
                Log.i("observeViewModel", "login response is full"+loginResponse.getUserID());


            }
        });*/
        //Log.i("loginResponseObservable","is "+loginResponseObservable.getValue().getUserID());

    }


    public void loggedinSuccessfully(LoginResponse loginResponse) {

        if (loginResponse.getUserID() != null) {
            new SharedPref(Contextt).setString("userID", loginResponse.getUserID());

            Log.i("loginclicked", "clicked" + loginResponse.getUserID());
            Intent intent = new Intent(Contextt/*.getApplicationContext()*/, MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Contextt.startActivity(intent);
            ((Activity) Contextt).finish();
        } else if (loginResponse.getResult() != null) {
            Toast.makeText(Contextt.getApplicationContext(), "User Not Authorized", Toast.LENGTH_LONG).show();

            if (loginRequest.isSocialMediaLogin()) {
                // call register
                RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setName(loginRequest.getName());
                registerRequest.setEmail(loginRequest.getName());
                registerRequest.setPassword(loginRequest.getPassword());
                registerRequest.setMobile_token(loginRequest.getMobile_token());
                registerRequest.setSignup(loginRequest.getLogin());

                RegisterViewModel registerViewModel = new RegisterViewModel(getApplication());
                registerViewModel.performRegisterApiCall(registerRequest);
            }

        } else

        {
            Toast.makeText(Contextt.getApplicationContext(), "Server is Down Please Try Again Later ..",
                    Toast.LENGTH_LONG).show();
        }

    }


    public void connectToBE(LoginRequest loginRequest) {
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final LoginRequest loginRequest;
        private final Context contextt;

        public Factory(@NonNull Application application, LoginRequest loginRequest, Context context) {
            this.application = application;
            this.loginRequest = loginRequest;
            this.contextt = context;

        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new LoginViewModel(application, loginRequest, contextt);
        }
    }


}