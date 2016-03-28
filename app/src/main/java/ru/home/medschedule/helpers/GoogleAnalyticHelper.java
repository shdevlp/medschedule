package ru.home.medschedule.helpers;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.content.Context;

import ru.home.medschedule.interfaces.IAnalytics;
import ru.home.medschedule.R;

/**
 * Created by Дмитрий on 28.03.2016.
 */
public class GoogleAnalyticHelper implements IAnalytics {
    private static final String TAG = "GoogleAnalyticService:";

    private GoogleAnalytics analytics;
    private Tracker tracker;
    private Context context;

    public GoogleAnalyticHelper(Context context) {
        analytics = null;
        tracker = null;
        this.context = context;
        onStart();
    }

    public boolean sendScreen(String tag, String screenName) {
        if (tracker != null) {
            tracker.setScreenName(tag + " : " + screenName);
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
            return true;
        }
        return false;
    }

    public boolean sendCategoryAction(String category, String action) {
        if (tracker != null) {
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(category)
                    .setAction(action)
                    .build());
            return true;
        }
        return false;
    }

    @Override
    public void onStart() {
        if (tracker == null) {
            tracker = getDefaultTracker();
        }
    }

    @Override
    public void onStop() {
        if (tracker != null) {
            tracker = null;
        }
        if (analytics != null) {
            analytics = null;
        }
    }

    synchronized private Tracker getDefaultTracker() {
        if (tracker == null) {
            analytics = GoogleAnalytics.getInstance(context);
            tracker = analytics.newTracker(R.xml.app_tracker);
        }
        return tracker;
    }
}
