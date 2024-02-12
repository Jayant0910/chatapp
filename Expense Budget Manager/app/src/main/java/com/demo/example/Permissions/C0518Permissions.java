package com.demo.example.Permissions;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;



public class C0518Permissions {
    static boolean loggingEnabled = true;

    public static void disableLogging() {
        loggingEnabled = false;
    }

    
    public static void log(String str) {
        if (loggingEnabled) {
            Log.d("Permissions", str);
        }
    }

    public static void check(Context context, String str, String str2, PermissionHandler permissionHandler) {
        check(context, new String[]{str}, str2, (Options) null, permissionHandler);
    }

    public static void check(Context context, String str, int i, PermissionHandler permissionHandler) {
        String str2;
        try {
            str2 = context.getString(i);
        } catch (Exception unused) {
            str2 = null;
        }
        check(context, new String[]{str}, str2, (Options) null, permissionHandler);
    }

    public static void check(Context context, String[] strArr, String str, Options options, PermissionHandler permissionHandler) {
        if (Build.VERSION.SDK_INT < 23) {
            permissionHandler.onGranted();
            log("Android version < 23");
            return;
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        Collections.addAll(linkedHashSet, strArr);
        boolean z = true;
        Iterator it = linkedHashSet.iterator();

        boolean aaaa = true;
        while (aaaa) {
            if (it.hasNext()) {
                if (context.checkSelfPermission((String) it.next()) != PackageManager.PERMISSION_GRANTED) {
                    z = false;
                    break;
                }
            } else {
                aaaa = false;
                break;
            }
        }
        if (z) {
            permissionHandler.onGranted();
            StringBuilder sb = new StringBuilder();
            sb.append("Permission(s) ");
            sb.append(PermissionsActivity.permissionHandler == null ? "already granted." : "just granted from settings.");
            log(sb.toString());
            PermissionsActivity.permissionHandler = null;
            return;
        }
        PermissionsActivity.permissionHandler = permissionHandler;
        Intent putExtra = new Intent(context, PermissionsActivity.class).putExtra("permissions", new ArrayList(linkedHashSet)).putExtra("rationale", str).putExtra("options", options);
        if (options != null && options.createNewTask) {
            putExtra.addFlags(268435456);
        }
        context.startActivity(putExtra);
    }

    public static void check(Context context, String[] strArr, int i, Options options, PermissionHandler permissionHandler) {
        String str;
        try {
            str = context.getString(i);
        } catch (Exception unused) {
            str = null;
        }
        check(context, strArr, str, options, permissionHandler);
    }

    
    
    public static class Options implements Serializable {
        String settingsText = "Settings";
        String rationaleDialogTitle = "Permissions Required";
        String settingsDialogTitle = "Permissions Required";
        String settingsDialogMessage = "Required permission(s) have been set not to ask again! Please provide them from settings.";
        boolean sendBlockedToSettings = true;
        boolean createNewTask = false;

        public Options setSettingsText(String str) {
            this.settingsText = str;
            return this;
        }

        public Options setCreateNewTask(boolean z) {
            this.createNewTask = z;
            return this;
        }

        public Options setRationaleDialogTitle(String str) {
            this.rationaleDialogTitle = str;
            return this;
        }

        public Options setSettingsDialogTitle(String str) {
            this.settingsDialogTitle = str;
            return this;
        }

        public Options setSettingsDialogMessage(String str) {
            this.settingsDialogMessage = str;
            return this;
        }

        public Options sendDontAskAgainToSettings(boolean z) {
            this.sendBlockedToSettings = z;
            return this;
        }
    }
}
