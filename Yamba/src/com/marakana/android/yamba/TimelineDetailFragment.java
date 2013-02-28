/* $Id: $
   Copyright 2013, G. Blake Meike

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.marakana.android.yamba;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class TimelineDetailFragment extends Fragment {
    private static final String TAG = "DETAILS";

    public static TimelineDetailFragment newInstance(Bundle state) {
        TimelineDetailFragment instance = new TimelineDetailFragment();
        instance.setArguments(state);
        return instance;
    }


    private TextView details;
    private String status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        super.onCreateView(inflater, container, state);

        details = (TextView) inflater.inflate(R.layout.timeline_detail, container, false);

        if (null == state) { state = getActivity().getIntent().getExtras(); }
        if (null == state) { state = getArguments(); }

        status = "";
        if (null != state) { status = state.getString(TimelineDetailActivity.TIMELINE_STATUS); }
        details.setText(status);
        Log.d(TAG, "in Detatil Fragment with: " + status);

        return details;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString(TimelineDetailActivity.TIMELINE_STATUS, status);
    }

    //  !!! Cast will fail for the TimelineDetailActivity
    // Define an interface implemented by all owning activites to fix.
    @SuppressWarnings("unused")
    private void sendToTimeline(String message) {
        ((TimelineActivity) getActivity()).sendMessageToTimelineFragment(message);
    }
}
