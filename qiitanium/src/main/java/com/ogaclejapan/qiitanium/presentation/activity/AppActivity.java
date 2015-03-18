package com.ogaclejapan.qiitanium.presentation.activity;

import com.ogaclejapan.qiitanium.R;
import com.ogaclejapan.qiitanium.util.Objects;

//import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;

import rx.Subscription;
import rx.subscriptions.SerialSubscription;
import rx.subscriptions.Subscriptions;

abstract class AppActivity extends ActionBarActivity {

    private final SerialSubscription mSubscriptions = new SerialSubscription();

    private FragmentManager mFragmentManager;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getFragmentManager();
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSubscriptions.set(onBind());
    }

    @Override
    protected void onDestroy() {
        onUnbind();
        super.onDestroy();
    }

    @SuppressWarnings("unchecked")
    protected <T extends Fragment> T findFragmentById(int id) {
        final Fragment f = mFragmentManager.findFragmentById(id);
        return (f != null) ? Objects.<T>cast(f) : null;
    }

    protected void setFragment(int id, Fragment fragment) {
        final Fragment f = findFragmentById(id);
        if (f == null) {
            mFragmentManager.beginTransaction().add(id, fragment).commit();
        } else {
            mFragmentManager.beginTransaction().replace(id, fragment).commit();
        }
    }

    protected Subscription onBind() {
        return Subscriptions.empty();
    }

    protected void onUnbind() {
        mSubscriptions.set(Subscriptions.empty());
    }

}
