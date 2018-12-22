package app.deal.com.dealdemo.login;

import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import app.deal.com.dealdemo.R;
import app.deal.com.dealdemo.databinding.ActivityLoginBinding;
import app.deal.com.dealdemo.model.remote.requests.LoginRequest;
import app.deal.com.dealdemo.model.remote.responses.LoginResponse;
import app.deal.com.dealdemo.register.RegisterActivity;
import app.deal.com.dealdemo.utils.SharedPref;

/**
 * Created by Misteka on 11/30/2018.
 */

public class LoginActivity extends AppCompatActivity implements LifecycleOwner {


    private ActivityLoginBinding activityLoginBinding;

    LoginRequest loginRequest;
    LoginViewModel viewModel;

    SharedPref sharedPref;

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "MainActivity";
    GoogleSignInClient mGoogleSignInClient;

    GoogleSignInAccount account;
    SignInButton signInButton;

    String UID = "", ProfilePhotoURL = "", Email = "", FName = "", LName = "";
    CallbackManager callbackManager;
    LoginButton loginButton;
    ProgressDialog progressDialog;

    public static Context loginContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginRequest = new LoginRequest();

        sharedPref = new SharedPref(this);

        loginContext = LoginActivity.this;
        init();
        activityLoginBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        activityLoginBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final LiveData<LoginResponse> loginResponseLiveData = viewModel.onLoginClicked();
                if (loginResponseLiveData != null) {
                    activityLoginBinding.progressBar.setVisibility(View.VISIBLE);
                    loginResponseLiveData.observe(LoginActivity.this, new Observer<LoginResponse>() {
                        @Override
                        public void onChanged(@Nullable LoginResponse loginResponse) {

                            activityLoginBinding.progressBar.setVisibility(View.GONE);

                            viewModel.loggedinSuccessfully(loginResponse);
                        }
                    });
                }
            }
        });


        registerFacebookLogin();
        registerGoogleSignIn();
    }

    /******************  ---  Google Sign In --- ********************/
    private void registerGoogleSignIn() {

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setColorScheme(2);
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        account = GoogleSignIn.getLastSignedInAccount(this);
//        if (account != null) {
////            updateUI(account);
//            gotoMainActivity(account);
//        }


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            callLoginAPI(account.getEmail(), account.getId(), sharedPref.getString("deviceToken"));

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
            Toast.makeText(this, "Login failed, please try again..", Toast.LENGTH_SHORT).show();
        }
    }

    /******************  ---  Google Sign In --- ********************/

    /******************  ---  Facebook Login --- ********************/


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
            public void onError(FacebookException error) {

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

            loginRequest.setLogin("android");
            loginRequest.setMobile_token(sharedPref.getString("deviceToken"));
            loginRequest.setName(Email);
            loginRequest.setPassword(UID);
            loginRequest.setSocialMediaLogin(true);

            viewModel.onLoginClicked();//connectToBE(loginRequest);
            //            callLoginAPI(Email, UID, "raghdafawzy73@gmail.com");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callLoginAPI(String Email, String Password, String DeviceToken) {
        loginRequest.setLogin("android");
        loginRequest.setMobile_token(DeviceToken);
        loginRequest.setName(Email);
        loginRequest.setPassword(Password);
        loginRequest.setSocialMediaLogin(true);

        viewModel.onLoginClicked();

    }


    private void init() {
//        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
//        loginRequest = new LoginRequest();


        loginRequest.setName(activityLoginBinding.inEmail.getText().toString());
        loginRequest.setPassword(activityLoginBinding.inPassword.getText().toString());
        loginRequest.setLogin("android");
        //loginRequest.setMobile_token(GettingDeviceTokenService.deviceToken);
        loginRequest.setMobile_token(sharedPref.getString("deviceToken"));

        LoginViewModel.Factory factory = new LoginViewModel.Factory(this.getApplication(), loginRequest,
                LoginActivity.this);

        viewModel =
                ViewModelProviders.of(this, factory).get(LoginViewModel.class);
        activityLoginBinding.setLoginRequest(loginRequest);
        activityLoginBinding.setLoginViewModel(viewModel);
        //activityLoginBinding.setLifecycleOwner(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

}
