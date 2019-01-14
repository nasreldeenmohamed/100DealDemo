package app.deal.com.dealdemo.login;

import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleOwner;
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
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;


import app.deal.com.dealdemo.R;
import app.deal.com.dealdemo.databinding.ActivityLoginBinding;
import app.deal.com.dealdemo.model.remote.requests.LoginRequest;
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

    public static Context loginContext;

    Observer<String> ErrorMessageLiveData;

    Button login_btn;
    public static ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginRequest = new LoginRequest();

        sharedPref = new SharedPref(this);

        loginContext = LoginActivity.this;

        activityLoginBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        init();

        registerFacebookLogin();
        registerGoogleSignIn();
    }

//    public void onLoginClicked() {
//        Log.i("mainActivity", "onLoginClicked");
//        if (loginRequest.getName().equals(""))
//            activityLoginBinding.inEmail.setError("Name is Empty!");
//       else if (loginRequest.getPassword().equals(""))
//            activityLoginBinding.inEmail.setError("Password is Empty!");
//       else if (loginRequest.getPassword().length() < 6)
//           activityLoginBinding.inPassword.setError("too short!");
//       else
//           viewModel.onLoginClicked();
//    }

    /******************  ---  Google Sign In --- ********************/
    private void registerGoogleSignIn() {

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setColorScheme(2);
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_sign_in_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (sharedPref.getString("userID").equals("")) {
            account = GoogleSignIn.getLastSignedInAccount(this);
            if (account != null) {
                mGoogleSignInClient.signOut();
            }
        }


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
            String name = account.getEmail().split("@")[0];
            callLoginAPI(name, account.getId(), sharedPref.getString("deviceToken"));

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Log.w(TAG, "signInResult:failed code=" + e.getMessage());
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

        facebookPost();

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
                Toast.makeText(LoginActivity.this, "facebook canceled",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "facebook error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();

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

    private void facebookPost() {
        //check login
        if (sharedPref.getString("userID").equals("")) {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if (accessToken == null) {
                Log.d(TAG, ">>>" + "Signed Out");
            } else {
                Log.d(TAG, ">>>" + "Signed In");
                LoginManager.getInstance().logOut();
            }
        }
    }

    private void callLoginAPI(String Email, String Password, String DeviceToken) {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

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

        viewModel = ViewModelProviders.of(this, factory).get(LoginViewModel.class);
        activityLoginBinding.setLoginRequest(loginRequest);
//        activityLoginBinding.setLoginViewModel(viewModel);

        ErrorMessageLiveData = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s.equals("emptyEmail"))
                    activityLoginBinding.inEmail.setError("Name is Empty!");
                else if (s.equals("emptyEmail"))
                    activityLoginBinding.inEmail.setError("Password is Empty!");
            }
        };

        login_btn = findViewById(R.id.button);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Login");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                viewModel.onLoginClicked();
            }
        });
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
