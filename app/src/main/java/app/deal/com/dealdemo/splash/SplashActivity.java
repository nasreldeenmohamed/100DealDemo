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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import app.deal.com.dealdemo.MainActivity;
import app.deal.com.dealdemo.R;
import app.deal.com.dealdemo.login.LoginActivity;
import app.deal.com.dealdemo.utils.SharedPref;

public class SplashActivity extends AppCompatActivity {

    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPref = new SharedPref(this);
        printKeyHash();

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

    private void gotoNextScreen() {
        if (sharedPref.getBoolean("isLoggedIn")) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
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
