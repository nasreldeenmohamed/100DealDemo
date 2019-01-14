package app.deal.com.dealdemo.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import app.deal.com.dealdemo.R;
import app.deal.com.dealdemo.login.LoginActivity;
import app.deal.com.dealdemo.utils.SharedPref;

public class MainActivity extends AppCompatActivity {

    private WebView wv1;
    String Url = "https://www.100deal.net";

    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBarWebView);
        SharedPref sharedPref = new SharedPref(this);



        wv1 = findViewById(R.id.webView);

        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv1.getSettings().setSupportMultipleWindows(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //  wv1.loadUrl(Url);

//        setContentView(wv1);
        String postData = null;
        try {
            //kshsflkfhkjfhdskjsbdjjd32qedw32rg
            postData = "andriodui=" + URLEncoder.encode(sharedPref.getString("userID"), "UTF-8") + "&andriod_token=" + URLEncoder.encode(sharedPref.getString("pass"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        wv1.postUrl(Url,postData.getBytes());



//       wv1.setWebViewClient(new MyBrowser());
        wv1.setWebViewClient(new MyBrowser() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
             /*   wv1.loadUrl("javascript:{" +
                        "ins=document.getElementsByTagName('input');" +
                        "ins[0].value='"+userName+"';" +
                        "ins[1].value='"+pwd+"';" +
                        "ins[2].value=true;" +
                        "document.getElementsByTagName('form')[0].submit();" +
                        "};" );*/
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (wv1.canGoBack()) {
            wv1.goBack();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.nav_sign_out:
                signOut();
                break;
            default:
                break;
        }

        return true;
    }

    private void signOut() {

        AlertDialog.Builder builder;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
//        } else {
        builder = new AlertDialog.Builder(MainActivity.this);
//        }
        builder.setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        new SharedPref(MainActivity.this).setString("userID", "");
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }


}