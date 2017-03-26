package de.chagemann.carsten.quiz;

import android.app.Activity;
import android.content.Context;

import de.mateware.snacky.Snacky;

/**
 * Created by carstenh on 24.03.2017.
 */

public class SnackbarHelper {

    public static void showSnackbar(Context context, String snackbarString) {
        Snacky.builder()
                .setActivty((Activity) context)
                .setText(snackbarString)
                .setDuration(Snacky.LENGTH_LONG)
                .setActionText("Ok")
                .build()
                .show();
    }
}
