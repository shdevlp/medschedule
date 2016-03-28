package ru.home.medschedule;

import android.app.Activity;
import android.os.Bundle;
import android.test.suitebuilder.annotation.Suppress;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ru.home.medschedule.event.BackEvent;
import ru.home.medschedule.event.ConfirmEvent;
import ru.home.medschedule.fragments.GuiFragment;
import ru.home.medschedule.fragments.TableFragment;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity:";

    private GuiFragment guiFragment;
    private TableFragment tableFragment;

    public MainActivity() {
        super();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppConf.setContext(this.getApplicationContext());

        guiFragment = new GuiFragment();
        tableFragment = new TableFragment();
        guiFragment.setActivity(this);
        getFragmentManager().beginTransaction().add(R.id.fragment_id, guiFragment).commit();
    }

    @Subscribe
    public void onEvent(ConfirmEvent event) {
        getFragmentManager().beginTransaction().replace(R.id.fragment_id, tableFragment).commit();
    }

    @Subscribe
    public void onEvent(BackEvent event) {
        getFragmentManager().beginTransaction().replace(R.id.fragment_id, guiFragment).commit();
    }
}
