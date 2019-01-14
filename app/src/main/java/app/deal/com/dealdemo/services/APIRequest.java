package app.deal.com.dealdemo.services;

import app.deal.com.dealdemo.model.remote.requests.RegisterRequest;
import app.deal.com.dealdemo.model.remote.responses.LoginResponse;
import app.deal.com.dealdemo.model.remote.responses.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Misteka on 11/30/2018.
 */
public interface APIRequest {
    //@Headers("Content-Type:text/html; charset=UTF-8")
    //@Headers({"Accept: application/json"})
    @Headers({
            "Accept: application/x-www-form-urlencoded;",
    })
    @FormUrlEncoded
    @POST("/?q=andriodapi")
    Call<LoginResponse> doLogin(@Field("name") String name, @Field("password") String password,
                                @Field("login") String login, @Field("mobile_token") String mobile_token);

    @FormUrlEncoded
    @POST("/?q=andriodapi")
    Call<String> updateDeviceToken(@Field("androidui") String androidui,
                                   @Field("update_android_token") String update_android_token, @Field("token") String token);


    @FormUrlEncoded
    @POST(".")
    Call<RegisterResponse> performRegister(@Field("name") String name, @Field("password") String password,
                                           @Field("signup") String platform, @Field("mail") String mail);
}


