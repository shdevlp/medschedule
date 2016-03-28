package ru.home.medschedule.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import ru.home.medschedule.AppConf;
import ru.home.medschedule.R;
import ru.home.medschedule.event.ConfirmEvent;

public class GuiFragment extends Fragment implements View.OnClickListener {

    private static int DELAY = 3000;

    private TextView tvFio;
    private TextView tvProcedure;
    private Spinner spDuration;
    private Spinner spMin;
    private Spinner spHour;
    private Button btGo;
    private Activity activity;

    public GuiFragment() {
        super();
        activity = null;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        clearFields();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gui, container, false);
        tvFio = (TextView) v.findViewById(R.id.et_fio);
        tvProcedure = (TextView) v.findViewById(R.id.et_procedure);
        spHour = (Spinner) v.findViewById(R.id.sp_hour);
        spMin = (Spinner) v.findViewById(R.id.sp_min);
        spDuration = (Spinner) v.findViewById(R.id.sp_duration);
        btGo = (Button) v.findViewById(R.id.bt_go);

        btGo.setOnClickListener(this);

        tvFio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                hideKeyboard(tvFio, DELAY);
            }
        });

        tvProcedure.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                hideKeyboard(tvProcedure, DELAY);
            }
        });

        return v;
    }

    private void hideKeyboard(final View v, int delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (AppConf.getContext() != null) {
                    InputMethodManager imm = (InputMethodManager) AppConf.getContext()
                            .getSystemService(AppConf.getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }, delay);
    }

    private void clearFields() {
        tvFio.setText("");
        tvProcedure.setText("");
        spHour.setSelection(0);
        spMin.setSelection(0);
        spDuration.setSelection(0);
        tvFio.requestFocus();
    }

    @Override
    public void onClick(View v) {
        if (activity != null) {
            final String fio = tvFio.getText().toString();
            final String proc = tvProcedure.getText().toString();

            if (fio.length() == 0) {
                AppConf.alertDialog(activity, R.string.ui_warning,
                        R.string.ui_fio_not_spec, R.mipmap.ic_launcher, null, 0,
                        R.string.ui_ok, null, null, null, null, null);
                return;
            }
            if (proc.length() == 0) {
                AppConf.alertDialog(activity, R.string.ui_warning,
                        R.string.ui_proc_not_spec, R.mipmap.ic_launcher, null, 0,
                        R.string.ui_ok, null, null, null, null, null);
                return;
            }


            String hour = spHour.getSelectedItem().toString();
            String minute = spMin.getSelectedItem().toString();
            String timeBegin = String.format("%s:%s", hour, minute);
            String duration = spDuration.getSelectedItem().toString();
            EventBus.getDefault().post(new ConfirmEvent(fio, proc, timeBegin, duration));
        }
    }
}
