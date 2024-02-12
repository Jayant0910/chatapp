package com.demo.example.Utile;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.demo.example.R;


public class CustomDialog {
    private static CustomDialog INSTANCE = new CustomDialog();
    AlertDialog alertDialog;


    public interface DismissListenerWithStatus {
        void onDismissed(String str);
    }

    public static CustomDialog getInstance() {
        return INSTANCE;
    }

    public void ErrorDialog(Context context, String str) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.alert_dialog, (ViewGroup) null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.InvitationDialog);
        builder.setView(inflate);
        ((TextView) inflate.findViewById(R.id.mMessageText)).setText(str);
        builder.setPositiveButton(context.getString(17039370), (DialogInterface.OnClickListener) null);
        this.alertDialog = builder.create();
        this.alertDialog.show();
        this.alertDialog.getButton(-1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog.this.alertDialog.cancel();
            }
        });
    }

    public void OtherDialog(Context context, String str, final DismissListenerWithStatus dismissListenerWithStatus) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.alert_dialog, (ViewGroup) null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.InvitationDialog);
        builder.setView(inflate);
        ((TextView) inflate.findViewById(R.id.mMessageText)).setText(str);
        builder.setPositiveButton(context.getString(17039370), (DialogInterface.OnClickListener) null);
        builder.setNegativeButton(context.getString(17039360), (DialogInterface.OnClickListener) null);
        this.alertDialog = builder.create();
        this.alertDialog.setCancelable(false);
        this.alertDialog.setCanceledOnTouchOutside(false);
        this.alertDialog.show();
        this.alertDialog.getButton(-1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog.this.alertDialog.cancel();
                if (dismissListenerWithStatus != null) {
                    dismissListenerWithStatus.onDismissed("Ok");
                }
            }
        });
        this.alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (dismissListenerWithStatus != null) {
                    dismissListenerWithStatus.onDismissed("Cancel");
                }
            }
        });
    }
}
