package com.demo.example.Utile;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import java.util.Locale;


public class LocaleHelper {
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static Context onAttach(Context context) {
        return setLocale(context, getPersistedData(context, Locale.getDefault().getLanguage()));
    }

    public static Context onAttach(Context context, String str) {
        return setLocale(context, getPersistedData(context, str));
    }

    public static String getLanguage(Context context) {
        return getPersistedData(context, Locale.getDefault().getLanguage());
    }

    public static Context setLocale(Context context, String str) {
        persist(context, str);
        if (Build.VERSION.SDK_INT >= 24) {
            return updateResources(context, str);
        }
        return updateResourcesLegacy(context, str);
    }

    private static String getPersistedData(Context context, String str) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SELECTED_LANGUAGE, str);
    }

    private static void persist(Context context, String str) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString(SELECTED_LANGUAGE, str);
        edit.apply();
    }

    @TargetApi(24)
    private static Context updateResources(Context context, String str) {
        context.getResources();
        Locale locale = new Locale(str);
        Locale.setDefault(Locale.Category.DISPLAY, locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        return context.createConfigurationContext(configuration);
    }

    private static Context updateResourcesLegacy(Context context, String str) {
        Locale locale = new Locale(str);
        if (Build.VERSION.SDK_INT >= 24) {
            Locale.setDefault(Locale.Category.DISPLAY, locale);
        } else {
            Locale.setDefault(locale);
        }
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLayoutDirection(locale);
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }
}
