package app.deal.com.dealdemo.services;

import app.deal.com.dealdemo.model.remote.requests.LoginRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Misteka on 11/30/2018.
 */

public interface APIRequest {
    @POST("/?q=andriodapi")
    Call<String> doLogin(@Body LoginRequest loginRequest);

}
