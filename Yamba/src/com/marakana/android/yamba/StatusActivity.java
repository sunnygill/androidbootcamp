package com.marakana.android.yamba;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


public class StatusActivity extends BaseActivity {
    private static final String TAG = "A_STATUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "in onCreate: " + savedInstanceState);

        setContentView(R.layout.activity_status);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.menu_status == item.getItemId()) { return true; }
        return super.onOptionsItemSelected(item);
    }
}
