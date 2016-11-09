package com.chatapp.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chatapp.R;
import com.chatapp.WebActivity;
import com.chatapp.chromeCustomTabs.CustomTabActivityHelper;
import com.chatapp.interfaces.TryAgainInterface;
import com.chatapp.preferences.Preferences;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class BaseActivity extends AppCompatActivity {

    /**
     * function to display the short message in toast.
     *
     * @param message  message to display
     * @param duration duration of the message
     */
    protected void showToast(String message, int duration) {
        if (TextUtils.isEmpty(message)) message = "";
        if (duration == 0) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * function to output the message.
     *
     * @param message message to display
     */
    protected void showOutput(String message) {
        System.out.println(">>>>>>>>>>>>>>>>>>>");
        System.out.println(message);
        System.out.println("<<<<<<<<<<<<<<<<<<<");
    }

    /**
     * For Displaying the Log Output in Logcat
     *
     * @param tag     Tag of the Log
     * @param message Message of the Log
     */
    protected void showLog(String tag, String message) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>");
        Log.d(tag, message);
        System.out.println("<<<<<<<<<<<<<<<<<<<<<");
    }

    /**
     * function to check for the internet connection availability.
     *
     * @return boolean value
     */
    protected boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null);
    }

    /**
     * function to check if the value is alphanumeric
     *
     * @param s string to be checked
     * @return boolean value
     */
    protected boolean isAlphaNumeric(String s) {
        String pattern = "^[a-zA-Z0-9]*$";
        return s.matches(pattern);
    }

    private Snackbar snackbar;

    /**
     * function to display the message on Snackbar
     *
     * @param message message to display
     */
    protected void showCrouton(String message) {
        if (TextUtils.isEmpty(message)) message = "";
        snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    /**
     * function to display the message on Snackbar with TRY AGAIN
     *
     * @param message  message to display
     * @param callback callback function to invoke on TRY AGAIN
     * @param service  service tag
     */
    protected void tryAgainCrouton(String message, final TryAgainInterface callback, final String service) {
        snackbar = Snackbar
                .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.tryAgain), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.tryAgain(service);
                    }
                });

        snackbar.show();
    }

    /**
     * function to hide the Snackbar
     */
    protected void hideCrouton() {
        if (snackbar != null && snackbar.isShownOrQueued())
            snackbar.dismiss();
    }

    /**
     * function to get the FACEBOOK Key hashes
     */
    protected void getFacebookHashKey() {
        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash>>> :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * function to parse the date from server
     *
     * @param date date to parse
     * @return parsed date
     */
    protected String getDate(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.dateInputFormat), Locale.ENGLISH);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getResources().getString(R.string.timeZone)));
            Date myDate = simpleDateFormat.parse(date);

            SimpleDateFormat outputFormat = new SimpleDateFormat(getString(R.string.dateOutputFormat), Locale.ENGLISH);
            outputFormat.setTimeZone(TimeZone.getDefault());
            date = outputFormat.format(myDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    // fragments operations
    public static boolean isStateSaved = false;

    /**
     * function to show the fragment
     *
     * @param name fragment to be shown
     * @param tag  fragment tag
     */
    public void showFragment(Fragment name, String tag) {
        hideKeyboard();
        hideCrouton();
        hideAllDialogFragments();
        if (isStateSaved) {
            Preferences.getInstance(this).storeFragmentTag(this, tag);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            // check if the fragment is in back stack
            boolean fragmentPopped = fragmentManager.popBackStackImmediate(tag, 0);
            if (fragmentPopped) {
                // fragment is pop from backStack
            } else {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, name, tag);
                fragmentTransaction.addToBackStack(tag);
                fragmentTransaction.commit();
            }
        }
    }

    /**
     * function to hide all the opened dialog fragments
     */
    protected void hideAllDialogFragments() {
        // hide payment dialog
//        if (PaymentOptions.getInstance().getDialog() != null && PaymentOptions.getInstance().getDialog().isShowing()) {
//            PaymentOptions.getInstance().getDialog().dismiss();
//        }
    }

    /**
     * function to go back to previous fragment
     */
    protected void oneStepBack() {
        hideKeyboard();
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        FragmentManager fragmentManager = getSupportFragmentManager();
        showOutput("Back Stack Count>>>>>>>>>>" + fragmentManager.getBackStackEntryCount());
        if (fragmentManager.getBackStackEntryCount() >= 2) {
            fragmentManager.popBackStackImmediate();
            fts.commit();
        } else {
            doubleClickToExit();
        }
    }

    /**
     * Clearing the Fragment backStack
     */
    protected void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }


    /**
     * For Hiding the keyboard
     */
    protected void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // double back pressed function
    private static long back_pressed;

    protected void doubleClickToExit() {
        if ((back_pressed + 2000) > System.currentTimeMillis())
            finish();
        else
            showCrouton("Click again to exit");
        back_pressed = System.currentTimeMillis();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        isStateSaved = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isStateSaved = false;
        Preferences.getInstance(this).storeFragmentTag(this, "");
        showOutput("on resume super called");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // function to show the progress dialog
    private ProgressDialog pd;

    /**
     * function to display the progress dialog
     */
    protected void showProgressDialog() {
        hideProgressDialog();
        pd = new ProgressDialog(this, R.style.progressDialogTheme);
        pd.setCancelable(false);
        pd.show();
        View v = LayoutInflater.from(this).inflate(R.layout.default_progress_dialog, new LinearLayout(this), false);
        pd.setContentView(v);
    }

    /**
     * function to hide the progress dialog
     */
    protected void hideProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    /**
     * function to display all the extras of the intent
     *
     * @param extras bundle to extract
     */
    public void printIntentExtras(Bundle extras) {
        if (extras != null)
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                Log.d(getPackageName(), String.format("%s %s (%s)", key, value != null ? value.toString() : null, value != null ? value.getClass().getName() : null));
            }
        else
            showOutput("Empty Bundle");
    }

    /**
     * function to clear all the notifications
     */
    protected void clearNotifications() {
        try {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * function to clear the notification of particular ID
     *
     * @param id notification id
     */
    protected void clearNotifications(int id) {
        try {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * function to open the url
     *
     * @param url       url to open
     * @param myWebView url to open in custom web view if chrome tabs are not present
     */
    protected void openUrl(String url, final boolean myWebView) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        intentBuilder.setShowTitle(true);

        // set start and exit animations
        intentBuilder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        // build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();

        // launch the url
        CustomTabActivityHelper.openCustomTab(this, customTabsIntent, Uri.parse(url), new CustomTabActivityHelper.CustomTabFallback() {
            @Override
            public void openUri(Activity activity, Uri uri) {
                if (myWebView) { // open it in your own web view activity.
                    Intent intent = new Intent(BaseActivity.this, WebActivity.class);
                    intent.putExtra(getString(R.string.urlIntentExtra), uri.toString());
                    startActivity(intent);
                } else { // open the url in other browser
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(Intent.createChooser(intent, "Select"));
                }

            }
        });
    }

    /**
     * function to open the play store link of my app
     */
    protected void openMyAppOnPlayStore() {
        String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}