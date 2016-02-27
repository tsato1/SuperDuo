package barqsoft.footballscores.service;

/**
 * Created by tsato on 2/25/16.
 */
import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.WidgetDataProvider;

@SuppressLint("NewApi")
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        WidgetDataProvider dataProvider = new WidgetDataProvider(getApplicationContext(), intent);
        return dataProvider;
    }

}
