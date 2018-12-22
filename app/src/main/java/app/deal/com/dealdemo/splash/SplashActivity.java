package app.deal.com.dealdemo.splash;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import app.deal.com.dealdemo.R;
import app.deal.com.dealdemo.login.LoginActivity;
import app.deal.com.dealdemo.main.MainActivity;
import app.deal.com.dealdemo.utils.SharedPref;

public class SplashActivity extends AppCompatActivity {

    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*FirebaseApp.initializeApp(SplashActivity.this);
        String DeviceToken = FirebaseInstanceId.getInstance().getToken();
*/
        sharedPref = new SharedPref(this);
//        printKeyHash();

        checkDeviceToken();

        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                gotoNextScreen();
            }
        }.start();

    }

    private void checkDeviceToken() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sharedPref.getString("deviceToken");//, refreshedToken);
//        Log.e("refreshedToken", refreshedToken);
//        if (!sharedPref.getString("token").equals(refreshedToken))
//        if (refreshedToken != null)
        Log.e("DeviceToken ==>", "nasr " + sharedPref.getString("deviceToken"));
    }


    private void gotoNextScreen() {
        if (sharedPref.getString("userID") != null && !sharedPref.getString("userID").equals("")) {
            Log.e("userID", sharedPref.getString("userID"));
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("app.deal.com.dealdemo",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
