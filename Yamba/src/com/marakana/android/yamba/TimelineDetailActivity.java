package com.marakana.android.yamba;

import android.os.Bundle;


/**
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class TimelineDetailActivity extends BaseActivity {
    public static final String TIMELINE_STATUS = "DETAIL";

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_timeline_detail);
    }
}
