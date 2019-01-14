package app.deal.com.dealdemo.register;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import app.deal.com.dealdemo.main.MainActivity;
import app.deal.com.dealdemo.model.RegisterInfo;
import app.deal.com.dealdemo.model.remote.requests.RegisterRequest;
import app.deal.com.dealdemo.model.remote.responses.RegisterResponse;
import app.deal.com.dealdemo.services.RetrofitRepository;
import app.deal.com.dealdemo.utils.ConnectionDetector;
import app.deal.com.dealdemo.utils.SharedPref;

/**
 * Created by Wafaa Aly on 12/15/2018.
 */
public class RegisterViewModel extends AndroidViewModel {

    private String errorMessage = "Invalid Data";
    private RegisterInfo registerInfo;
    public final ObservableField<String> errorEmail = new ObservableField<>();
    private LiveData<RegisterResponse> registerResponseLiveData;

    Context context;

    public RegisterViewModel(Application application, Context context) {
        super(application);
        registerInfo = new RegisterInfo("", "", "");
        this.context = context;
    }


    public LiveData<RegisterResponse> onRegisterClicked() {
        if (performRegisterApiCall()) {
            return registerResponseLiveData;
        } else {
            setToastMessage();
            return null;
        }
    }

    private void setToastMessage() {
        Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    public boolean performRegisterApiCall() {
        if (registerInfo.isRegistrationInfoValid()) {
            registerResponseLiveData = RetrofitRepository.getInstance().performRegister(generateRequest());
            return true;
        } else {
            return false;
        }
    }

    public boolean performRegisterApiCall(final RegisterRequest registerRequest) {

        if (ConnectionDetector.checkInternetConnection(context)) {
            if (registerInfo.isRegistrationInfoValid()) {
                registerResponseLiveData = RetrofitRepository.getInstance().performRegister(registerRequest);
                registerResponseLiveData.observeForever(new Observer<RegisterResponse>() {
                    @Override
                    public void onChanged(@Nullable RegisterResponse registerResponse) {
                        if(registerResponse != null ) {
                            if (registerResponse.getResult().getUid() != null || !registerResponse.getResult().getUid().equals("")) {
                                saveUserSignedUp(registerResponse.getResult().getUid());


                                new SharedPref(context).setString("pass",registerResponse.getResult().getPass());
                                gotoMainActivity();
                            }
                        }else {

                            Toast.makeText(context, "Server is Down please try again later...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                return true;
            } else {
                return false;
            }
        } else {
            Toast.makeText(context, "No Internet Connection...", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    private RegisterRequest generateRequest() {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName(registerInfo.getName());
        registerRequest.setMail(registerInfo.getEmail());
        registerRequest.setPassword(registerInfo.getPassword());
        registerRequest.setSignup("android");

        return registerRequest;
    }

//    public void afterNameTextChanged(CharSequence s) {
//        registerInfo.setName(s.toString());
//        registerInfo.setEmail(s.toString());
//    }
//
//    public void afterEmailTextChanged(CharSequence s) {
//        registerInfo.setEmail(s.toString());
//    }

    public void afterNameTextChanged(CharSequence s) {
        registerInfo.setName(s.toString());
    }

    public void afterEmailTextChanged(CharSequence s) {
        registerInfo.setEmail(s.toString());
    }

    public void afterPasswordTextChanged(CharSequence s) {
        registerInfo.setPassword(s.toString());
    }

    public void saveUserSignedUp(String userID) {
        new SharedPref(getApplication()).setString("userID", userID);

    }

    static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final Context contextt;


        public Factory(@NonNull Application application, Context context) {
            this.application = application;
            this.contextt = context;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new RegisterViewModel(application, contextt);
        }
    }
}
