package app.deal.com.dealdemo.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Misteka on 11/30/2018.
 */
public class Constant {

    public static ProgressDialog getProgressDialog(Context context, String msg) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        return progressDialog;
    }



}
