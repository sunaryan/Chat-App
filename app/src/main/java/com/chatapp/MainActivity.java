package com.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.chatapp.base.BaseActivity;
import com.chatapp.fragments.MatchedUserViewProfileFragment;
import com.chatapp.fragments.MatchedUsersFragment;
import com.chatapp.preferences.Preferences;

public class MainActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView toolBarTitle;
    private boolean isBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        showFragment(new MatchedUsersFragment(), MatchedUsersFragment.class.getSimpleName());
    }

    /**
     * Function to set the toolbar title
     *
     * @param title  toolbar title
     * @param isBack whether to enable back button or not
     */
    public void setToolbarTitle(String title, boolean isBack) {
        toolBarTitle.setText(title);
        this.isBack = isBack;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            if (isBack) {
                toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.back));
            } else {
                toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.menu));
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isBack)
                    oneStepBack();
                else {
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        oneStepBack();
    }

    @Override
    protected void onResume() {
        showOutput("OnResumed Main called");
        if (isStateSaved) {
            String tag = Preferences.getInstance(this).getFragmentTag(this);
            super.onResume();
            if (tag.equals(MatchedUsersFragment.class.getSimpleName())) {
                showFragment(new MatchedUsersFragment(), MatchedUsersFragment.class.getSimpleName());
            } else if (tag.equals(MatchedUserViewProfileFragment.class.getSimpleName())) {
                showFragment(new MatchedUserViewProfileFragment(), MatchedUserViewProfileFragment.class.getSimpleName());
            }
        } else {
            super.onResume();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getSupportFragmentManager().findFragmentById(R.id.fragment_container).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showFragment(Fragment name, String tag) {
        super.showFragment(name, tag);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getSupportFragmentManager().findFragmentById(R.id.fragment_container).onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
