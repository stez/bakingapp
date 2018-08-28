package it.and.stez78.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class BakingAppWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingAppWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}