package app.deal.com.dealdemo.login;

import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import app.deal.com.dealdemo.R;
import app.deal.com.dealdemo.databinding.ActivityLoginBinding;
import app.deal.com.dealdemo.model.remote.requests.LoginRequest;
import app.deal.com.dealdemo.model.remote.responses.LoginResponse;

/**
 * Created by Misteka on 11/30/2018.
 */

public class LoginActivity extends AppCompatActivity implements LifecycleOwner {


    private ActivityLoginBinding activityLoginBinding;

    String UID = "", ProfilePhotoURL = "", Email = "", FName = "", LName = "";
    CallbackManager callbackManager;
    LoginButton loginButton;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         activityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
         //.setContentView(this, R.layout.activity_login);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setName("emozemozo");
        loginRequest.setPassword("12345678");
        loginRequest.setLogin("android");
        loginRequest.setMobile_token("raghdafawzy73@gmail.com");
        LoginViewModel.Factory factory = new LoginViewModel.Factory(
                this.getApplication(), loginRequest);


        final LoginViewModel viewModel =
                ViewModelProviders.of(this,factory).get(LoginViewModel.class);

        observeViewModel(viewModel);
        //LoginViewModel_DB loginViewModel = new LoginViewModel_DB();

//        activityLoginBinding.setLoginViewModel(loginViewModel);
//        loginViewModel.getUser().observe(this, new Observer<User>() {
//            @Override
//            public void onChanged(@Nullable User user) {
//                if (user.getEmail().length() > 0 || user.getPassword().length() > 0)
//                    Toast.makeText(getApplicationContext(), "email : " + user.getEmail() + " password " + user.getPassword(), Toast.LENGTH_SHORT).show();
//            }
//        });

        registerFacebookLogin();
    }

    private void registerFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Retrieving data...");
                progressDialog.setTitle("Facebook Login");
                progressDialog.show();

                String accessToken = loginResult.getAccessToken().getToken();

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                getFacebookData(object);
                                Log.e("response", object.toString());
                                progressDialog.dismiss();
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, email, first_name, last_name");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        if (AccessToken.getCurrentAccessToken() != null) {
            Log.e("UserID", AccessToken.getCurrentAccessToken().getUserId());
        }
    }

    private void getFacebookData(JSONObject object) {
        try {
            URL profilePictureURL = new URL("https://graph.facebook.com/" + object.getString("id") +
                    "/picture?width=250&height=250");
            ProfilePhotoURL = profilePictureURL.toString();
//            sharedPref.setString("user_fb_pp_url", ProfilePhotoURL);

            UID = object.getString("id");
            Email = object.getString("email");
            FName = object.getString("first_name");
            LName = object.getString("last_name");

            Log.e("UID", UID);
            Log.e("ProfilePhoto", ProfilePhotoURL);
            Log.e("Email", Email);
            Log.e("Name", FName);

//            callLoginAPI();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void observeViewModel(LoginViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getObservableLoginResponse().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(@Nullable LoginResponse loginResponse) {
                if (loginResponse != null) {
                    Log.i("eman","login response is "+loginResponse.getResult());
                }else
                {
                    Log.i("eman","login response is null");
                }
            }
        });
    }

}
