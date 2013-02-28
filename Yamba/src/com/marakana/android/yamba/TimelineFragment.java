package com.marakana.android.yamba;

import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.marakana.android.yamba.svc.YambaContract;

/**
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class TimelineFragment extends ListFragment
implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = "TIME";

    private static final int TIMELINE_LOADER = 42;

    private static final String SORT = YambaContract.Timeline.Columns.TIMESTAMP + " DESC";

    private static final String[] PROJ = new String[] {
        YambaContract.Timeline.Columns.ID,
        YambaContract.Timeline.Columns.USER,
        YambaContract.Timeline.Columns.TIMESTAMP,
        YambaContract.Timeline.Columns.STATUS
    };

    private static final String[] FROM = new String[PROJ.length - 1];
    static { System.arraycopy(PROJ, 1, FROM, 0, FROM.length); }

    private static final int TO[] = new int [] {
        R.id.timeline_user,
        R.id.timeline_timestamp,
        R.id.timeline_status
    };

    class TimelineBinder implements SimpleCursorAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            int vid = view.getId();
            if (R.id.timeline_timestamp != vid) { return false; }

            String time = "long ago";
            long t = cursor.getLong(columnIndex);
            if (0 < t) { time = DateUtils.getRelativeTimeSpanString(t).toString(); }
            ((TextView) view).setText(time);

            return true;
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity().getApplicationContext(),
                YambaContract.Timeline.URI,
                PROJ,
                null,
                null,
                SORT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cur) {
        Log.d(TAG, "Load finished");
        ((SimpleCursorAdapter) getListAdapter()).swapCursor(cur);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        Log.d(TAG, "Load reset");
        ((SimpleCursorAdapter) getListAdapter()).swapCursor(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        View v = super.onCreateView(inflater, parent, state);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.timeline_row,
                null,
                FROM,
                TO,
                0);

        adapter.setViewBinder(new TimelineBinder());
        setListAdapter(adapter);

        getLoaderManager().initLoader(TIMELINE_LOADER, null, this);

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor c = (Cursor) getListAdapter().getItem(position);
        String status = c.getString(c.getColumnIndex(YambaContract.Timeline.Columns.STATUS));
        Intent i = new Intent(getActivity(), TimelineDetailActivity.class);
        i.putExtra(TimelineDetailActivity.TIMELINE_STATUS, status);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }

    @SuppressWarnings("unused")
    public void onMessage(String message) {
        // TODO do something with the message
    }
}
