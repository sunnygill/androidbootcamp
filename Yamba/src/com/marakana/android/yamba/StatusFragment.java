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
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.marakana.android.yamba.svc.YambaContract;


/**
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class StatusFragment extends Fragment {
    public static final String TAG = "STATUS";

    private static final int MIN_CHARS = 0;
    private static final int WARN_CHARS = 10;
    private static final int MAX_CHARS = 140;

    static Poster poster;

    // if two different threads share access to the same mutable state
    // they must do it holding the same lock.
    static class Poster extends AsyncTask<String, Void, Void> {
        private final ContentResolver resolver;

        public Poster(ContentResolver resolver) { this.resolver = resolver; }

        @Override
        protected Void doInBackground(String... args) {
            String status = args[0];
            if (BuildConfig.DEBUG) { Log.d(TAG, "posting status: " + status); }

            ContentValues vals = new ContentValues();
            vals.put(YambaContract.Posts.Columns.STATUS, status);

            resolver.insert(YambaContract.Posts.URI, vals);

            return null;
        }

        @Override
        protected void onCancelled() { poster = null; }

        @Override
        protected void onPostExecute(Void result) { poster = null; }
    }


    private TextView count;
    private EditText status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);
        Log.d(TAG, "in onCreateView: " + state);

        View v = inflater.inflate(R.layout.fragment_status, parent, false);

        count = (TextView) v.findViewById(R.id.status_count);

        status = (EditText) v.findViewById(R.id.status_text);
        status.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void beforeTextChanged(CharSequence cs, int s, int c, int a) { }
            @Override
            public void onTextChanged(CharSequence cs, int s, int b, int c) { setCount(); }
        });

        ((Button) v.findViewById(R.id.status_submit)).setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(View v1) { submit(); }
                });

        return v;
    }

    void submit() {
        String msg = status.getText().toString();
        if (TextUtils.isEmpty(msg)) { return; }

        if (null != poster) { return; }
        poster = new Poster(getActivity().getContentResolver());

        status.setText("");

        poster.execute(msg);
    }

    void setCount() {
        int n = MAX_CHARS - status.getText().toString().length();
        count.setText(String.valueOf(n));

        int color = Color.GREEN;
        if (MIN_CHARS >= n) { color = Color.RED; }
        else if (WARN_CHARS >= n) { color = Color.YELLOW; }
        count.setTextColor(color);
    }
}
