package barqsoft.footballscores;

/**
 * Created by tsato on 2/25/16.
 */
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;
import android.widget.TextView;

@SuppressLint("NewApi")
public class WidgetDataProvider implements RemoteViewsFactory {

    List<WidgetItem> mCollections = new ArrayList();

    Context mContext = null;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mCollections.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews mView = new RemoteViews(mContext.getPackageName(), R.layout.scores_list_item);

        mView.setTextViewText(R.id.home_name, mCollections.get(position).home_name);
        mView.setTextColor(R.id.home_name, Color.BLACK);
        mView.setTextViewText(R.id.score_textview, mCollections.get(position).score);
        mView.setTextColor(R.id.score_textview, Color.BLACK);
        mView.setTextViewText(R.id.data_textview, mCollections.get(position).date);
        mView.setTextColor(R.id.data_textview, Color.BLACK);
        mView.setTextViewText(R.id.away_name, mCollections.get(position).away_name);
        mView.setTextColor(R.id.away_name, Color.BLACK);
        mView.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(mCollections.get(position).home_name));
        mView.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(mCollections.get(position).away_name));

        final Intent fillInIntent = new Intent();
        fillInIntent.setAction(CustomAppWidgetProvider.ACTION_TOAST);
        final Bundle bundle = new Bundle();
        bundle.putString(CustomAppWidgetProvider.EXTRA_STRING, "list pushed");
        fillInIntent.putExtras(bundle);
        mView.setOnClickFillInIntent(android.R.id.text1, fillInIntent);

        return mView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
        Thread thread = new Thread() {
            public void run() {
                initData();
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void onDataSetChanged() {
        Thread thread = new Thread() {
            public void run() {
                initData();
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
    }

    private void initData() {
        if (mContext != null) {
            mCollections.clear();

            Cursor cursor = mContext.getContentResolver().query(DatabaseContract.BASE_CONTENT_URI, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    WidgetItem mWidgetItem = new WidgetItem();
                    mWidgetItem.home_name = cursor.getString(scoresAdapter.COL_HOME);
                    mWidgetItem.away_name = cursor.getString(scoresAdapter.COL_AWAY);
                    mWidgetItem.date = cursor.getString(scoresAdapter.COL_MATCHTIME);
                    mWidgetItem.score = Utilies.getScores(cursor.getInt(scoresAdapter.COL_HOME_GOALS), cursor.getInt(scoresAdapter.COL_AWAY_GOALS));
                    mWidgetItem.match_id = String.valueOf(cursor.getDouble(scoresAdapter.COL_ID));

                    mCollections.add(mWidgetItem);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    @Override
    public void onDestroy() {

    }
}