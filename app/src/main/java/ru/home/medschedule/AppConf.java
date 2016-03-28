package ru.home.medschedule;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;

import ru.home.medschedule.interfaces.BtnCallBack;

/**
 * Created by Дмитрий on 28.03.2016.
 */
public class AppConf {
    private static Context context;

    static {
        context = null;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context cxt) {
        context = cxt;
    }

    public static void alertDialog(final Activity activity,
                                   Integer titleId, Integer messageId, int icon, View view, final int btnTextSize,
                                   final Integer neuBtnId, final BtnCallBack neuBtnCallBack,
                                   final Integer negBtnId, final BtnCallBack negBtnCallBack,
                                   final Integer posBtnId, final BtnCallBack posBtnCallBack) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if (titleId != null) {
            builder.setTitle(context.getString(titleId));
        }
        if (messageId != null) {
            builder.setMessage(context.getString(messageId));
        }
        if (icon != 0) {
            builder.setIcon(icon);
        }
        if (view != null) {
            builder.setView(view);
        }
        builder.setCancelable(false);

        if (neuBtnId != null) {
            builder.setNeutralButton(context.getString(neuBtnId), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (neuBtnCallBack != null) {
                        neuBtnCallBack.onBtnClick();
                    }
                    dialog.cancel();
                }
            });
        }
        if (negBtnId != null) {
            builder.setNegativeButton(context.getString(negBtnId), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (negBtnCallBack != null) {
                        negBtnCallBack.onBtnClick();
                    }
                    dialog.cancel();
                }
            });
        }
        if (posBtnId != null) {
            builder.setPositiveButton(context.getString(posBtnId), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (posBtnCallBack != null) {
                        posBtnCallBack.onBtnClick();
                    }
                    dialog.cancel();
                }
            });
        }

        final AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                if (neuBtnId != null) {
                    alert.getButton(DialogInterface.BUTTON_NEUTRAL)
                            .setTextColor(activity.getResources().getColor(R.color.colorPrimary));
                    if (btnTextSize != 0) {
                        alert.getButton(DialogInterface.BUTTON_NEUTRAL).setTextSize(btnTextSize);
                    }
                }
                if (negBtnId != null) {
                    alert.getButton(DialogInterface.BUTTON_NEGATIVE)
                            .setTextColor(activity.getResources().getColor(R.color.colorPrimary));
                    if (btnTextSize != 0) {
                        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(btnTextSize);
                    }
                }
                if (posBtnId != null) {
                    alert.getButton(DialogInterface.BUTTON_POSITIVE)
                            .setTextColor(activity.getResources().getColor(R.color.colorPrimary));
                    if (btnTextSize != 0) {
                        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(btnTextSize);
                    }
                }
            }
        });
        alert.show();
    }
}
