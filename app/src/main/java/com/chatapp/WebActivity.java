package com.chatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.chatapp.base.BaseActivity;

public class WebActivity extends BaseActivity {
    private TextView toolbarTitle;
    private WebView webView;
    private Toolbar mToolbar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        inflateToolbar();

        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebClient());

        loadUrl();

    }

    private class WebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showProgressDialog();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideProgressDialog();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

            showCrouton(error.toString());
        }
    }

    private void inflateToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        setToolbarTitle(getString(R.string.app_name), true);
    }

    // function to set the title of default_toolbar
    public void setToolbarTitle(String title, boolean isBack) {
        toolbarTitle.setText(title);
        if (getSupportActionBar() != null) {
            if (isBack) {
//                mToolbar.setNavigationIcon(ContextCompat.getDrawable(this,  R.drawable.back));
                getSupportActionBar().setHomeButtonEnabled(true);
            } else {
                getSupportActionBar().setHomeButtonEnabled(false);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        finish();
    }

    private void loadUrl() {
        if (getIntent().getExtras() != null) {
            if (!TextUtils.isEmpty(getIntent().getExtras().getString(getString(R.string.urlIntentExtra)))) {
                webView.loadUrl(getIntent().getExtras().getString(getString(R.string.urlIntentExtra)));
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        loadUrl();
    }
}
