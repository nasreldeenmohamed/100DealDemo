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
import app.deal.com.dealdemo.model.remote.responses.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRepository {
    String HTTPS_API_Deal_Demo_URL = "https://www.100deal.net";

    private APIRequest APIRequest;
    private static RetrofitRepository retrofitRepository;

    private RetrofitRepository() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HTTPS_API_Deal_Demo_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        APIRequest = retrofit.create(APIRequest.class);
    }

    public synchronized static RetrofitRepository getInstance() {

            if (retrofitRepository == null) {
                retrofitRepository = new RetrofitRepository();
            }

        return retrofitRepository;
    }

    public LiveData<LoginResponse> performLogin(LoginRequest loginRequest) {
        final MutableLiveData<LoginResponse> data = new MutableLiveData<>();

        APIRequest.doLogin(loginRequest).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
        //        simulateDelay();
               // data.setValue(response.body());
                Log.i("eman view model"," = "+response.code());
                Log.i("eman view model"," = "+response.toString());
                Log.i("eman view model"," = "+response.errorBody());
                Log.i("eman view model"," = "+response.isSuccessful());
                Log.i("eman view model"," = "+response.code());
                Log.i("eman view model"," = "+response.message());
                Log.i("eman view model"," = "+response.raw());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                data.setValue(null);
                Log.i("eman view model"," = "+call.request().url());
                Log.i("eman view model"," = "+t.getLocalizedMessage());
            }
        });

        return data;
    }

    private void simulateDelay() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
