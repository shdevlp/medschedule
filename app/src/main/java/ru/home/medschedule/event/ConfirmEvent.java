package ru.home.medschedule.event;

/**
 * Created by Дмитрий on 28.03.2016.
 */
public class ConfirmEvent {
    private String fio;
    private String procedure;
    private String timeBegin;
    private String duration;

    public ConfirmEvent(String fio, String procedure, String timeBegin, String duration) {
        this.fio = fio;
        this.procedure = procedure;
        this.timeBegin = timeBegin;
        this.duration = duration;
    }

    public String getFio() {
        return this.fio;
    }

    public String getProcedure() {
        return this.procedure;
    }

    public String getTimeBegin() {
        return this.timeBegin;
    }

    public String getDuration() {
        return this.duration;
    }
}
