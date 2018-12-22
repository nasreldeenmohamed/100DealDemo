package app.deal.com.dealdemo.services;

/**
 * Created by Misteka on 11/30/2018.
 */


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.deal.com.dealdemo.model.remote.requests.LoginRequest;
import app.deal.com.dealdemo.model.remote.requests.NotificationTokenRequest;
import app.deal.com.dealdemo.model.remote.requests.RegisterRequest;
import app.deal.com.dealdemo.model.remote.responses.LoginResponse;
import app.deal.com.dealdemo.model.remote.responses.RegisterResponse;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRepository {
    String HTTPS_API_Deal_Demo_URL = "https://www.100deal.net";

    private APIRequest APIRequest;
    private static RetrofitRepository retrofitRepository;
    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    private RetrofitRepository() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HTTPS_API_Deal_Demo_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        APIRequest = retrofit.create(APIRequest.class);
    }

    public synchronized static RetrofitRepository getInstance() {

        if (retrofitRepository == null) {
            retrofitRepository = new RetrofitRepository();
        }

        return retrofitRepository;
    }

    public LiveData<RegisterResponse> performRegister(RegisterRequest registerRequest) {
        final MutableLiveData<RegisterResponse> data = new MutableLiveData<>();

        APIRequest.performRegister(registerRequest.getName(), registerRequest.getPassword(),
                registerRequest.getSignup(), registerRequest.getEmail()).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                //        simulateDelay();
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                    Log.d("regiterrequest", " response = " + response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.d("regiterrequest", " failed = " + t.getMessage());
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<LoginResponse> performLogin(LoginRequest loginRequest) {

        final MutableLiveData<LoginResponse> data = new MutableLiveData<>();
        Log.d("loginrequest", " response = " + loginRequest.getName());
        Log.d("loginrequest", " response = " + loginRequest.getMobile_token());
        Log.d("loginrequest", " response = " + loginRequest.getPassword());
        Log.d("loginrequest", " response = " + loginRequest.getLogin());
        //  Log.d("loginrequest", " response = " + loginRequest.get);


        APIRequest.doLogin(loginRequest.getName(), loginRequest.getPassword(), loginRequest.getLogin(),
                loginRequest.getMobile_token()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("login", " response = " + response.isSuccessful());
                Log.d("login", " response = " + response.body());
                Log.d("login", " response = " + response.message());
                Log.d("login", " response = " + response.errorBody());
                Log.d("login", " response = " + response.raw());
                Log.d("login", " response = " + response.code());


                simulateDelay();
                // data.postValue(response.body());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.d("login failure", " = " + call.request().url());
                Log.d("login failure", " = " + t.getLocalizedMessage());
                data.setValue(null);
            }
        });

        return data;
    }

    public String[] updateToken(NotificationTokenRequest notificationTokenRequest) {

        final String[] result = {""};
        // APIRequest.updateDeviceToken(notificationTokenRequest).enqueue(new Call<String>());

        APIRequest.updateDeviceToken(notificationTokenRequest.getAndroidui(),
                notificationTokenRequest.getUpdate_android_token(), notificationTokenRequest.getToken())
                .enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    result[0] = "token updatedSuccessfully";
                    Log.i("emannotification body", " = " + response.body().toString());
                    Log.i("emannotification body", " = " + response.body());


                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("emannotificationError", " = " + call.request().url());
                Log.i("emannotificationError", " = " + t.getLocalizedMessage());
                result[0] = "Failure Token not updated";
            }
        });
        return result;
    }


    private void simulateDelay() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
