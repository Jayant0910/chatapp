package com.demo.example.Permissions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;

@TargetApi(23)

public class PermissionsActivity extends Activity {
    static final String EXTRA_OPTIONS = "options";
    static final String EXTRA_PERMISSIONS = "permissions";
    static final String EXTRA_RATIONALE = "rationale";
    private static final int RC_PERMISSION = 6937;
    private static final int RC_SETTINGS = 6739;
    static PermissionHandler permissionHandler;
    private ArrayList<String> allPermissions;
    private ArrayList<String> deniedPermissions;
    private ArrayList<String> noRationaleList;
    private C0518Permissions.Options options;

    @Override
    @TargetApi(23)
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setFinishOnTouchOutside(false);
        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_PERMISSIONS)) {
            finish();
            return;
        }
        getWindow().setStatusBarColor(0);
        this.allPermissions = (ArrayList) intent.getSerializableExtra(EXTRA_PERMISSIONS);
        this.options = (C0518Permissions.Options) intent.getSerializableExtra(EXTRA_OPTIONS);
        if (this.options == null) {
            this.options = new C0518Permissions.Options();
        }
        this.deniedPermissions = new ArrayList<>();
        this.noRationaleList = new ArrayList<>();
        boolean z = true;
        Iterator<String> it = this.allPermissions.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (checkSelfPermission(next) != PackageManager.PERMISSION_GRANTED) {
                this.deniedPermissions.add(next);
                if (shouldShowRequestPermissionRationale(next)) {
                    z = false;
                } else {
                    this.noRationaleList.add(next);
                }
            }
        }
        if (this.deniedPermissions.isEmpty()) {
            grant();
            return;
        }
        String stringExtra = intent.getStringExtra(EXTRA_RATIONALE);
        if (z || TextUtils.isEmpty(stringExtra)) {
            C0518Permissions.log("No rationale.");
            requestPermissions(toArray(this.deniedPermissions), RC_PERMISSION);
            return;
        }
        C0518Permissions.log("Show rationale.");
        showRationale(stringExtra);
    }

    private void showRationale(String str) {
        DialogInterface.OnClickListener r0 = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    PermissionsActivity.this.requestPermissions(PermissionsActivity.this.toArray(PermissionsActivity.this.deniedPermissions), PermissionsActivity.RC_PERMISSION);
                } else {
                    PermissionsActivity.this.deny();
                }
            }
        };
        new AlertDialog.Builder(this).setTitle(this.options.rationaleDialogTitle).setMessage(str).setPositiveButton(17039370, r0).setNegativeButton(17039360, r0).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                PermissionsActivity.this.deny();
            }
        }).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (iArr.length == 0) {
            deny();
            return;
        }
        this.deniedPermissions.clear();
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (iArr[i2] != 0) {
                this.deniedPermissions.add(strArr[i2]);
            }
        }
        if (this.deniedPermissions.size() == 0) {
            C0518Permissions.log("Just allowed.");
            grant();
            return;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> arrayList2 = new ArrayList<>();
        ArrayList arrayList3 = new ArrayList();
        Iterator<String> it = this.deniedPermissions.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (shouldShowRequestPermissionRationale(next)) {
                arrayList3.add(next);
            } else {
                arrayList.add(next);
                if (!this.noRationaleList.contains(next)) {
                    arrayList2.add(next);
                }
            }
        }
        if (arrayList2.size() > 0) {
            PermissionHandler permissionHandler2 = permissionHandler;
            finish();
            if (permissionHandler2 != null) {
                permissionHandler2.onJustBlocked(getApplicationContext(), arrayList2, this.deniedPermissions);
            }
        } else if (arrayList3.size() > 0) {
            deny();
        } else if (permissionHandler == null || permissionHandler.onBlocked(getApplicationContext(), arrayList)) {
            finish();
        } else {
            sendToSettings();
        }
    }

    private void sendToSettings() {
        if (!this.options.sendBlockedToSettings) {
            deny();
            return;
        }
        C0518Permissions.log("Ask to go to settings.");
        new AlertDialog.Builder(this).setTitle(this.options.settingsDialogTitle).setMessage(this.options.settingsDialogMessage).setPositiveButton(this.options.settingsText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PermissionsActivity.this.startActivityForResult(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", PermissionsActivity.this.getPackageName(), null)), PermissionsActivity.RC_SETTINGS);
            }
        }).setNegativeButton(17039360, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PermissionsActivity.this.deny();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                PermissionsActivity.this.deny();
            }
        }).create().show();
    }

    @Override
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i == RC_SETTINGS && permissionHandler != null) {
            C0518Permissions.check(this, toArray(this.allPermissions), (String) null, this.options, permissionHandler);
        }
        super.finish();
    }

    
    public String[] toArray(ArrayList<String> arrayList) {
        int size = arrayList.size();
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            strArr[i] = arrayList.get(i);
        }
        return strArr;
    }

    @Override
    public void finish() {
        permissionHandler = null;
        super.finish();
    }

    
    public void deny() {
        PermissionHandler permissionHandler2 = permissionHandler;
        finish();
        if (permissionHandler2 != null) {
            permissionHandler2.onDenied(getApplicationContext(), this.deniedPermissions);
        }
    }

    private void grant() {
        PermissionHandler permissionHandler2 = permissionHandler;
        finish();
        if (permissionHandler2 != null) {
            permissionHandler2.onGranted();
        }
    }
}
