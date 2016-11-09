package com.chatapp.base;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chatapp.R;
import com.chatapp.WebActivity;
import com.chatapp.chromeCustomTabs.CustomTabActivityHelper;
import com.chatapp.interfaces.TryAgainInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class BaseFragment extends Fragment {

    private Context context;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    /**
     * function to display the short message in toast.
     *
     * @param message  message to display
     * @param duration duration of the message
     */
    protected void showToast(String message, int duration) {
        try {
            if (TextUtils.isEmpty(message)) message = "";
            if (duration == 0) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        if (s.matches(pattern)) {
            return true;
        }
        return false;
    }

    // function to displaySnackBar
    protected Snackbar snackbar = null;

    /**
     * function to display the message on Snackbar
     *
     * @param rootView view
     * @param message  message to display
     */
    protected void showCrouton(View rootView, String message) {
        snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    /**
     * function to display the message on Snackbar with TRY AGAIN
     *
     * @param rootView view
     * @param message  message to display
     * @param callback callback function to invoke on TRY AGAIN
     * @param service  service tag
     */
    protected void tryAgainCrouton(View rootView, String message, final TryAgainInterface callback, final String service) {
        snackbar = Snackbar
                .make(rootView, message, Snackbar.LENGTH_INDEFINITE)
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

//    // bottom sheet dialog
//    View dialogView = null;
//    TextView messageTextView;
//    Button retry, cancel;
//    BottomSheetDialog bottomSheetDialog;
//
//    protected void showDialog(String message, final TryAgainInterface callback, final String service) {
//        if (dialogView == null) {
//            dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.retry_bottom_sheet, null);
//            messageTextView = (TextView) dialogView.findViewById(R.id.message);
//            retry = (Button) dialogView.findViewById(R.id.tryAgain);
//            cancel = (Button) dialogView.findViewById(R.id.cancel);
//        }
//
//        messageTextView.setText(message);
//        retry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetDialog.dismiss();
//                callback.tryAgain(service);
//            }
//        });
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetDialog.dismiss();
//            }
//        });
//        bottomSheetDialog = new BottomSheetDialog(getActivity());
//        bottomSheetDialog.setTitle("Hello");
//        bottomSheetDialog.setContentView(dialogView);
//        bottomSheetDialog.setCanceledOnTouchOutside(false);
//        bottomSheetDialog.setCancelable(false);
//        bottomSheetDialog.show();
//    }

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

    /**
     * Function to get the current date
     *
     * @return current date
     */
    protected String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat outputFormat = new SimpleDateFormat(getString(R.string.dateOutputFormat), Locale.ENGLISH);
        return outputFormat.format(c.getTime());
    }

    /**
     * function to clear all the notifications
     */
    protected void clearNotifications() {
        try {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
        intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        intentBuilder.setShowTitle(true);

        // set start and exit animations
        intentBuilder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        intentBuilder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        // build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();

        // launch the url
        CustomTabActivityHelper.openCustomTab(getActivity(), customTabsIntent, Uri.parse(url), new CustomTabActivityHelper.CustomTabFallback() {
            @Override
            public void openUri(Activity activity, Uri uri) {
                if (myWebView) { // open it in your own web view activity.
                    Intent intent = new Intent(context, WebActivity.class);
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
        String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * function to open the play store link of other app
     *
     * @param packageName package name of the app
     */
    protected void openAppOnPlayStore(String packageName) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
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
                Log.d(getActivity().getPackageName(), String.format("%s %s (%s)", key, value != null ? value.toString() : null, value != null ? value.getClass().getName() : null));
            }
        else
            showOutput("Empty Bundle");
    }

    // function to show the progress dialog
    ProgressDialog pd;

    /**
     * function to display the progress dialog
     */
    protected void showProgressDialog() {
        hideProgressDialog();
        pd = new ProgressDialog(context, R.style.progressDialogTheme);
        pd.setCancelable(false);
        pd.show();
        View v = LayoutInflater.from(context).inflate(R.layout.default_progress_dialog, new LinearLayout(getContext()), false);
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

    @Override
    public void onDestroyView() {
        hideCrouton();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        hideCrouton();
        super.onDestroy();
    }
}
