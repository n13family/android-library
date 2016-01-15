package com.hellofyc.base.compat;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hellofyc.base.content.IntentHelper;
import com.hellofyc.util.AndroidUtils;

import java.lang.reflect.Field;

/**
 * Created on 2015/11/23.
 *
 * @author Yucun Fang
 */
public class OsCompat {

    public static final String OS_DEFAULT            = "Android";
    public static final String OS_MIUI               = "MIUI";
    public static final String OS_FLYME              = "Flyme";
    public static final String OS_COLOR_OS           = "ColorOS";
    public static final String OS_H2_OS              = "H2OS";
    public static final String OS_EMUI               = "EMUI";
    public static final String OS_SMARTISAN_OS       = "SmartisanOS";
    public static final String OS_360_UI             = "360UI";
    public static final String OS_FUNTOUCH_OS        = "FuntouchOS";

    private static final String mOS;

    interface OsCompatImpl {
        Intent getOpenPermissionManagerActivityIntent(Context context);
        Intent getOpenPermissionActivityIntent(Context context);
        Intent getOpenPermissionActivityIntent(Context context, String packageName);
        Intent getOpenPureBackgroundActivityIntent(Context context);
        void setLauncherIconCornerMark(Context context, int notifyId, int count);
        boolean isFloatWindowPermissionAllowed(Context context);
    }

    private static final OsCompatImpl IMPL;

    static class OsCompatImplBase implements OsCompatImpl {

        @Override
        public Intent getOpenPermissionManagerActivityIntent(Context context) {
            return IntentHelper.getOpenSettingsActivityIntent(context);
        }

        @Override
        public Intent getOpenPermissionActivityIntent(Context context) {
            return IntentHelper.getOpenSettingsActivityIntent(context);
        }

        @Override
        public Intent getOpenPermissionActivityIntent(Context context, String packageName) {
            return IntentHelper.getOpenSettingsActivityIntent(context);
        }

        @Override
        public Intent getOpenPureBackgroundActivityIntent(Context context) {
            return IntentHelper.getOpenSettingsActivityIntent(context);
        }

        @Override
        public void setLauncherIconCornerMark(Context context, int notifyId, int count) {
        }

        @Override
        public boolean isFloatWindowPermissionAllowed(Context context) {
            return true;
        }

    }

    static class OsCompatImplSmartisanOs extends OsCompatImplBase {

        @Override
        public Intent getOpenPermissionManagerActivityIntent(Context context) {
            return SmarisanOsCompat.getOpenPermissionManagerActivityIntent(context);
        }
    }

    static class OsCompatImplMIUI extends OsCompatImplBase {

        @Override
        public Intent getOpenPermissionManagerActivityIntent(Context context) {
            return MiuiCompat.getOpenPermissionManagerActivityIntent(context);
        }

        @Override
        public Intent getOpenPermissionActivityIntent(Context context) {
            return MiuiCompat.getOpenPermissionActivityIntent(context);
        }

        @Override
        public Intent getOpenPermissionActivityIntent(Context context, String packageName) {
            return MiuiCompat.getOpenPermissionActivityIntent(context, packageName);
        }

        @Override
        public void setLauncherIconCornerMark(Context context, int notifyId, int count) {
            MiuiCompat.setLauncherIconCornerMark(context, notifyId, count);
        }

        @Override
        public boolean isFloatWindowPermissionAllowed(Context context) {
            return MiuiCompat.isFloatWindowPermissionAllowed(context);
        }
    }

    static class OsCompatImplFlyme extends OsCompatImplBase {
    }

    static class OsCompatImplFuntouchOs extends OsCompatImplBase {
    }

    static class OsCompatImplColorOS extends OsCompatImplBase {

        @Override
        public Intent getOpenPureBackgroundActivityIntent(Context context) {
            return ColorOsCompat.getOpenPureBackgroundActivityIntent(context);
        }

        @Override
        public Intent getOpenPermissionManagerActivityIntent(Context context) {
            return ColorOsCompat.getOpenPermissionManagerActivityIntent(context);
        }
    }

    static class OsCompatImplEMUI extends OsCompatImplBase {
    }

    static class OsCompatImpl360UI extends OsCompatImplBase {
    }

    static class OsCompatImplH2OS extends OsCompatImplBase {
    }

    public static Intent getOpenPermissionManagerActivityIntent (@NonNull Context context) {
        return IMPL.getOpenPermissionManagerActivityIntent(context);
    }

    public static Intent getOpenPermissionActivityIntent(@NonNull Context context) {
        return IMPL.getOpenPermissionActivityIntent(context);
    }

    public static Intent getOpenPermissionActivityIntent(@NonNull Context context, @NonNull String packageName) {
        return IMPL.getOpenPermissionActivityIntent(context, packageName);
    }

    public static Intent getOpenPureBackgroundActivityIntent(@NonNull Context context) {
        return IMPL.getOpenPureBackgroundActivityIntent(context);
    }

    public static void setLauncherIconCornerMark(@NonNull Context context, int notifyId, int count) {
        IMPL.setLauncherIconCornerMark(context, notifyId, count);
    }

    public static boolean isFloatWindowPermissionAllowed(@NonNull Context context) {
        return IMPL.isFloatWindowPermissionAllowed(context);
    }

    static {
        mOS = checkOS();
        if (OS_SMARTISAN_OS.equals(mOS)) {
            IMPL = new OsCompatImplSmartisanOs();
        } else if (OS_MIUI.equals(mOS)) {
            IMPL = new OsCompatImplMIUI();
        } else if (OS_EMUI.equals(mOS)) {
            IMPL = new OsCompatImplEMUI();
        } else if (OS_FLYME.equals(mOS)) {
            IMPL = new OsCompatImplFlyme();
        } else if (OS_COLOR_OS.equals(mOS)) {
            IMPL = new OsCompatImplColorOS();
        } else if (OS_EMUI.equals(mOS)) {
            IMPL = new OsCompatImplEMUI();
        } else if (OS_H2_OS.equals(mOS)) {
            IMPL = new OsCompatImplH2OS();
        } else if (OS_360_UI.equals(mOS)) {
            IMPL = new OsCompatImpl360UI();
        } else if (OS_FUNTOUCH_OS.equals(mOS)) {
            IMPL = new OsCompatImplFuntouchOs();
        } else {
            IMPL = new OsCompatImplBase();
        }
    }

    public static String checkOS() {
        if (isMiui()) {
            return OS_MIUI;
        } else if (isColorOS()) {
            return OS_COLOR_OS;
        } else if (isFlyme()) {
            return OS_FLYME;
        } else if (isSmartisanOS()) {
            return OS_SMARTISAN_OS;
        } else if (isEmui()) {
            return OS_EMUI;
        } else if (isH2OS()) {
            return OS_H2_OS;
        } else if (is360UI()) {
            return OS_360_UI;
        } else if (isFuntouchOs()) {
            return OS_FUNTOUCH_OS;
        } else {
            return OS_DEFAULT;
        }
    }

    public static boolean isMiui() {
        String versionName = AndroidUtils.getSystemProperty("ro.miui.ui.version.name");
        return !TextUtils.isEmpty(versionName);
    }

    public static boolean isSmartisanOS() {
        String versionName = AndroidUtils.getSystemProperty("ro.smartisan.version");
        return !TextUtils.isEmpty(versionName);
    }

    public static boolean isH2OS() {
        String versionName = AndroidUtils.getSystemProperty("ro.build.ota.versionname");
        return versionName != null && versionName.contains("Hydrogen");
    }

    public static boolean isColorOS() {
        try {
            Field field = Build.class.getDeclaredField("ROM_DIFF_VERSION");
            field.setAccessible(true);
            String romStr = field.get(null).toString();
            if (romStr.startsWith("ColorOS")) return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean is360UI() {
        String versionName = AndroidUtils.getSystemProperty("ro.build.uiversion");
        return (versionName != null && versionName.contains("360UI"));
    }

    public static boolean isEmui() {
        String versionName = AndroidUtils.getSystemProperty("ro.build.version.emui");
        return (versionName != null && versionName.contains("EmotionUI"));
    }

    public static boolean isFlyme() {
        String versionName = AndroidUtils.getSystemProperty("ro.build.version.incremental");
        return versionName != null && versionName.contains("Flyme");
    }

    public static boolean isFuntouchOs() {
        String versionName = AndroidUtils.getSystemProperty("ro.vivo.os.name");
        return versionName != null && versionName.contains("Funtouch");
    }

}
