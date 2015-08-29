package io.github.dnivra26.kaito;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;

import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

public class UiUtil {
    public static CircularProgressDrawable getProgressDrawable(Context context) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable
                .Builder(context)
                .color(context.getResources().getColor(R.color.base))
                .sweepSpeed(1f)
                .strokeWidth(4)
                .style(CircularProgressDrawable.Style.ROUNDED).build();
        return circularProgressDrawable;
    }

    public static ProgressDialog buildProgressDialog(Context context) {

        ProgressDialog progressDialog = new ProgressDialog(context, R.style.CustomDialogTheme);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.setIndeterminateDrawable(UiUtil.getProgressDrawable(context));
        progressDialog.setIndeterminate(true);
        return progressDialog;

    }
}
