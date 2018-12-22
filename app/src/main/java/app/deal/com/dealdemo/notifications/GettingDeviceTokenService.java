package app.deal.com.dealdemo.notifications;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import app.deal.com.dealdemo.model.remote.requests.NotificationTokenRequest;
import app.deal.com.dealdemo.services.RetrofitRepository;
import app.deal.com.dealdemo.utils.SharedPref;

/**
 * Created by Misteka on 11/24/2018.
 */
public class GettingDeviceTokenService extends FirebaseInstanceIdService {
    //public MutableLiveData<String> deviceToken = new MutableLiveData<>();

    public static String deviceToken;
    SharedPref sharedPref;

    @Override
    public void onTokenRefresh() {
        sharedPref = new SharedPref(this);
        String DeviceToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("DeviceToken ==> ", "nasr service" + DeviceToken);
        //    deviceToken.setValue(DeviceToken);
        //deviceToken.postValue(DeviceToken);
        this.deviceToken = DeviceToken;
        sharedPref.setString("deviceToken", DeviceToken);

//        sharedPref = new SharedPref(this)
        if (sharedPref.getString("userID") != null && !sharedPref.getString("userID").equals("")) {
            NotificationTokenRequest tokenRequest = new NotificationTokenRequest();
            tokenRequest.setAndroidui(sharedPref.getString("userID"));
            tokenRequest.setUpdate_android_token("1");
            tokenRequest.setToken(DeviceToken);
            RetrofitRepository.getInstance().updateToken(tokenRequest);

        }

    }

}