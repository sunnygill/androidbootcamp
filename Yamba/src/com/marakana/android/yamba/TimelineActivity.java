package com.marakana.android.yamba;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.marakana.android.yamba.svc.YambaContract;


public class TimelineActivity extends BaseActivity {
    private static final String TAG = "A_STATUS";
    private static final String DETAIL_FRAGMENT = "Yamba.DETAILS";


    private boolean usingFragments;

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        if (usingFragments) { launchDetailFragment(intent); }
        else { startActivity(intent); }
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        Log.d(TAG, "in onCreate: " + state);

        setContentView(R.layout.activity_timeline);

        usingFragments = (null != findViewById(R.id.timeline_detail_fragment));

        if (usingFragments) {
            if (null == getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT)) {
                addFragment(state);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = new Intent(YambaContract.YAMBA_SERVICE);
        i.putExtra(YambaContract.SVC_PARAM_OP, YambaContract.SVC_OP_POLLING_ON);
        startService(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent i = new Intent(YambaContract.YAMBA_SERVICE);
        i.putExtra(YambaContract.SVC_PARAM_OP, YambaContract.SVC_OP_POLLING_OFF);
        startService(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (Build.VERSION_CODES.ICE_CREAM_SANDWICH <= Build.VERSION.SDK_INT) {
            if (android.R.id.home == id) { return true; }
        }
        if (R.id.menu_timeline == id) { return true; }
        return super.onOptionsItemSelected(item);
    }

    private void addFragment(Bundle state) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction xact = fragmentManager.beginTransaction();

        Fragment frag = TimelineDetailFragment.newInstance(state);

        xact.add(R.id.timeline_detail_fragment, frag, DETAIL_FRAGMENT);
        xact.commit();
    }

    private void launchDetailFragment(Intent intent) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction xact = fragmentManager.beginTransaction();

        Fragment frag = TimelineDetailFragment.newInstance(intent.getExtras());
        xact.addToBackStack(null);

        xact.replace(R.id.timeline_detail_fragment, frag, DETAIL_FRAGMENT);
        xact.commit();
    }

    public void sendMessageToTimelineFragment(String message) {
        TimelineFragment frag = (TimelineFragment) getSupportFragmentManager().findFragmentById(R.id.timeline_fragment);
        frag.onMessage(message);
    }
}
