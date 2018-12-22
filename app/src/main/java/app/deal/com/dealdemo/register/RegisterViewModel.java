package app.deal.com.dealdemo.register;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.SharedPreferences;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.widget.Toast;

import app.deal.com.dealdemo.model.RegisterInfo;
import app.deal.com.dealdemo.model.remote.requests.RegisterRequest;
import app.deal.com.dealdemo.model.remote.responses.RegisterResponse;
import app.deal.com.dealdemo.services.RetrofitRepository;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Wafaa Aly on 12/15/2018.
 */
public class RegisterViewModel extends AndroidViewModel {

    private String errorMessage = "Invalid Data";
    private RegisterInfo registerInfo;
    public final ObservableField<String> errorEmail = new ObservableField<>();
    private LiveData<RegisterResponse> registerResponseLiveData;

    public RegisterViewModel(Application application) {
        super(application);
        registerInfo = new RegisterInfo("", "", "");
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
    public boolean performRegisterApiCall(RegisterRequest registerRequest) {
        if (registerInfo.isRegistrationInfoValid()) {
            registerResponseLiveData = RetrofitRepository.getInstance().performRegister(registerRequest);
            return true;
        } else {
            return false;
        }
    }

    private RegisterRequest generateRequest() {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName(registerInfo.getName());
        registerRequest.setEmail(registerInfo.getEmail());
        registerRequest.setPassword(registerInfo.getPassword());
//        registerRequest.setName("wafaa");
//        registerRequest.setEmail("Wafaa@gmail.com");
//        registerRequest.setPassword("123456");
        registerRequest.setSignup("android");

        return registerRequest;
    }

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
        SharedPreferences.Editor editor = getApplication().getSharedPreferences("UserInfo", MODE_PRIVATE).edit();
        editor.putBoolean("UserSignedIn", true);
        editor.putString("UserId", userID);
        editor.apply();
    }

    static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;


        public Factory(@NonNull Application application) {
            this.application = application;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new RegisterViewModel(application);
        }
    }
}
