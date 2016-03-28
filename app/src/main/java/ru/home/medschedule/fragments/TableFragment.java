package ru.home.medschedule.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ru.home.medschedule.R;
import ru.home.medschedule.event.BackEvent;
import ru.home.medschedule.event.ConfirmEvent;


public class TableFragment extends Fragment {
    private WebView webView;
    private Button btnBack;
    private String html;

    private static final String MINE = "text/html";
    private static final String ENCODING = "utf-8";

    private String[] hours;
    private String[] minutes;
    private String[] durations;

    private ConfirmEvent confirmEvent;
    private boolean viewCreated = false;

    public TableFragment() {
        html = null;
        webView = null;
        confirmEvent = null;
        EventBus.getDefault().register(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_table, container, false);
        webView = (WebView) v.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        btnBack = (Button) v.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BackEvent());
            }
        });
        hours = getResources().getStringArray(R.array.hours_array);
        minutes = getResources().getStringArray(R.array.minute_array);
        durations = getResources().getStringArray(R.array.duration_array);

        if (confirmEvent != null) {
            html = parseEvent(confirmEvent);
            webView.loadDataWithBaseURL(null, html, MINE, ENCODING, null);
        }

        viewCreated = true;

        return v;
    }

    @Subscribe
    public void onEvent(ConfirmEvent event) {
        confirmEvent = event;

        if (viewCreated) {
            html = parseEvent(event);
            webView.loadDataWithBaseURL(null, html, MINE, ENCODING, null);
        }
    }

    private class ParseThread implements Runnable {
        private ConfirmEvent confirmEvent;
        private Thread thread;
        private final String[] html = new String[1];

        public ParseThread(ConfirmEvent event) throws InterruptedException {
            this.confirmEvent = event;
            html[0] = null;
            thread = new Thread(this);
            thread.start();
            thread.join();
        }

        public String getHtml() {
            return html[0];
        }

        @Override
        public void run() {
            String[] timeBegin = confirmEvent.getTimeBegin().split("\\:");
            if (timeBegin.length != 2) {
                return;
            }

            final String hour = timeBegin[0];
            final String minute = timeBegin[1];
            final String duration = confirmEvent.getDuration();
            final String fio = confirmEvent.getFio();
            final String proc = confirmEvent.getProcedure();
            final String durationSize = String.valueOf(durations.length);

            final int iDurationSize = Integer.valueOf(durationSize);
            final int iDuration = Integer.valueOf(duration);

            String text = new String("<html><body><table bordercolor=\"red\" border=\"1\" width=\"300%\">");
            text += "<tr><th rowspan=\"2\">Время</th><th colspan=\"" + durationSize + "\">Длительность процедуры</th></tr>";
            text += "<tr>";
            for (String dur: durations) {
                text += String.format("<td align=\"center\">%s</td>", dur);
            }
            text += "</tr>";

            int spanCount = 0;
            int i;

            if (iDuration > 5) {
                for (i = 0; i < iDurationSize; i++) {
                    if (iDuration == Integer.valueOf(durations[i])) {
                        spanCount = i + 1;
                        break;
                    }
                }
            }

            for (String h: hours) {
                for (String m: minutes) {
                    text += String.format("<tr><td align=\"center\">%s:%s</td>", h, m);

                    for (i = 0; i < iDurationSize;) {
                        if (hour.endsWith(h) && minute.endsWith(m) && Integer.valueOf(durations[i]) <= iDuration) {
                            if (spanCount > 0) {
                                text += String.format("<td align=\"center\" colspan=\"" + spanCount
                                        + "\" bgcolor=\"#ffaa00\">%s/%s</td>", fio, proc);
                                i += spanCount;
                            } else {
                                text += String.format("<td align=\"center\" bgcolor=\"#ffaa00\">%s/%s</td>", fio, proc);
                                i++;
                            }
                        } else {
                            text +="<td></td>";
                            i++;
                        }
                    }

                    text += "</tr>";
                }
            }

            text += "</table></body></html>";
            html[0] = text;
        }
    }

    private String parseEvent(ConfirmEvent event) {
        ParseThread parseThread = null;
        try {
            parseThread = new ParseThread(event);
            return parseThread.getHtml();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
