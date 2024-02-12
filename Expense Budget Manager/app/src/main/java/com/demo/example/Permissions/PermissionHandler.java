package com.demo.example.Permissions;

import android.content.Context;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;


public abstract class PermissionHandler {
    public abstract void onGranted();

    public void onDenied(Context context, ArrayList<String> arrayList) {
        if (C0518Permissions.loggingEnabled) {
            StringBuilder sb = new StringBuilder();
            sb.append("Denied:");
            Iterator<String> it = arrayList.iterator();
            while (it.hasNext()) {
                sb.append(" ");
                sb.append(it.next());
            }
            C0518Permissions.log(sb.toString());
        }
        Toast.makeText(context, "Permission Denied.", Toast.LENGTH_SHORT).show();
    }

    public boolean onBlocked(Context context, ArrayList<String> arrayList) {
        if (!C0518Permissions.loggingEnabled) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Set not to ask again:");
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            sb.append(" ");
            sb.append(it.next());
        }
        C0518Permissions.log(sb.toString());
        return false;
    }

    public void onJustBlocked(Context context, ArrayList<String> arrayList, ArrayList<String> arrayList2) {
        if (C0518Permissions.loggingEnabled) {
            StringBuilder sb = new StringBuilder();
            sb.append("Just set not to ask again:");
            Iterator<String> it = arrayList.iterator();
            while (it.hasNext()) {
                sb.append(" ");
                sb.append(it.next());
            }
            C0518Permissions.log(sb.toString());
        }
        onDenied(context, arrayList2);
    }
}
