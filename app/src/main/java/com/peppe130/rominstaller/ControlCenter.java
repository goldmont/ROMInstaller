package com.peppe130.rominstaller;

import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.mikepenz.entypo_typeface_library.Entypo;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.peppe130.rominstaller.core.Download;
import com.peppe130.rominstaller.core.Utils;


@SuppressWarnings("ConstantConditions")
public class ControlCenter {

    public static String[] DEVICE_COMPATIBILITY_LIST = new String[] {"SM-G920F"};
    public static String[] ROM_MD5_LIST = new String[] {"4ba678ca02e60557757d29e91e1e792f"};
    public static String[] RECOVERY_MD5_LIST = new String[] {"5fb732eea3d3e2b407fa7685c27a5354"};

    public static Boolean TEST_MODE = false;
    public static Boolean TRIAL_MODE = true;
    public static Boolean BUTTON_UI = false;
    public static Boolean SHOULD_SHOW_SPLASH_SCREEN = true;
    public static Boolean SHOULD_SHOW_DISCLAIMER_SCREEN = true;

    public static Integer SPLASH_SCREEN_DELAY = 3000;
    public static Integer SPLASH_SCREEN_IMAGE = R.drawable.rom_logo_light;

    public static IIcon SETTINGS_ICON = GoogleMaterial.Icon.gmd_settings;
    public static IIcon CHANGELOG_ICON = Ionicons.Icon.ion_ios_paper_outline;
    public static IIcon DEFAULT_OPTIONS_ICON = GoogleMaterial.Icon.gmd_settings_backup_restore;
    public static IIcon CLEAR_DOWNLOADS_ICON = Entypo.Icon.ent_trash;


    public static void DownloadROM() {

        Uri mDownloadLink = Uri.parse("http://www.mediafire.com/download/3a7gefzmxj44lv1/SampleROM.zip");
        File mDownloadDirectory = new File(Utils.ACTIVITY.getString(R.string.rom_folder));
        Utils.FILE_NAME = "SampleROM.zip";

        DownloadManager.Request mRequest = new DownloadManager.Request(mDownloadLink);
        mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), Utils.FILE_NAME);

        new Download(
                mRequest,
                mDownloadDirectory,
                Utils.FILE_NAME, true).execute();

    }

    public static void ExportPreferences() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(Utils.ACTIVITY);

        try {
            FileWriter mFileWriter = new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/" + Utils.ACTIVITY.getString(R.string.rom_name) + "/" + "preferences.prop");
            BufferedWriter mBufferedWriter = new BufferedWriter(mFileWriter);
            mBufferedWriter.write(
                            "wipe=" + (String.valueOf(SP.getBoolean("wipe", false))) +
                            "\nkernel=" + (SP.getString("listKernel", "1")) +
                            "\nstatusbar=" + (SP.getString("listStatusBar", "1")) +
                            "\nsf_qc=" + String.valueOf(SP.getBoolean("SF-QC", false)) +
                            "\nmy_files=" + SP.getString("listMyFile", "1") +
                            "\nkeyboard=" + (SP.getString("listKey", "1")) +
                            "\nmms=" + SP.getString("listMMS", "1") +
                            "\nfonts=" + SP.getString("listFonts", "1") +
                            "\naccuweather=" + SP.getString("listAccuweather", "1") +
                            "\nsounds=" + SP.getString("listSound", "1") +
                            "\nboot_animation=" + SP.getString("listBootanimation", "1") +
                            "\nscrolling_wallpaper=" + String.valueOf(SP.getBoolean("scrolling_wallpaper", false)) +
                            "\nincreasing_ringtone=" + String.valueOf(SP.getBoolean("increasing_ringtone", true)) +
                            "\nsec_symbols=" + String.valueOf(SP.getBoolean("sec_symbol", false)) +
                            "\nvoice_call=" + String.valueOf(SP.getBoolean("voice_call", true)) +
                            "\ncall_button=" + String.valueOf(SP.getBoolean("call_button", true)) +
                            "\nscheduled_messages=" + String.valueOf(SP.getBoolean("scheduled_messages", true)) +
                            "\nshutter_sound=" + String.valueOf(SP.getBoolean("shutter_sound", false)) +
                            "\nboot_sound=" + String.valueOf(SP.getBoolean("boot_sound", false)) +
                            "\nxposed=" + String.valueOf(SP.getBoolean("xposed", false)) +
                            "\ncsc1=" + String.valueOf(SP.getBoolean("csc1", true)) +
                            "\ncsc2=" + String.valueOf(SP.getBoolean("csc2", true)) +
                            "\ncsc3=" + String.valueOf(SP.getBoolean("csc3", true)) +
                            "\ncsc4=" + String.valueOf(SP.getBoolean("csc4", true)) +
                            "\ncsc5=" + String.valueOf(SP.getBoolean("csc5", true)) +
                            "\ncsc6=" + String.valueOf(SP.getBoolean("csc6", true)) +
                            "\ncsc7=" + String.valueOf(SP.getBoolean("csc7", true)) +
                            "\ncsc8=" + String.valueOf(SP.getBoolean("csc8", true)) +
                            "\nbloat1=" + String.valueOf(SP.getBoolean("bloat1", true)) +
                            "\nbloat2=" + String.valueOf(SP.getBoolean("bloat2", true)) +
                            "\nbloat3=" + String.valueOf(SP.getBoolean("bloat3", false)) +
                            "\nbloat4=" + String.valueOf(SP.getBoolean("bloat4", false)) +
                            "\nbloat5=" + String.valueOf(SP.getBoolean("bloat5", true)) +
                            "\nbloat6=" + String.valueOf(SP.getBoolean("bloat6", false)) +
                            "\nbloat7=" + String.valueOf(SP.getBoolean("bloat7", false))
            );
            mBufferedWriter.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void DefaultValues() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(Utils.ACTIVITY);
        SharedPreferences.Editor mEditor = SP.edit();

        // CheckBoxes
        mEditor.putBoolean("wipe", false).apply();
        mEditor.putBoolean("SF-QC", false).apply();
        mEditor.putBoolean("scrolling_wallpaper", false).apply();
        mEditor.putBoolean("increasing_ringtone", true).apply();
        mEditor.putBoolean("sec_symbols", false).apply();
        mEditor.putBoolean("voice_call", true).apply();
        mEditor.putBoolean("shutter_sound", false).apply();
        mEditor.putBoolean("boot_sound", false).apply();
        mEditor.putBoolean("call_button", true).apply();
        mEditor.putBoolean("xposed", false).apply();
        mEditor.putBoolean("csc1", false).apply();
        mEditor.putBoolean("csc2", false).apply();
        mEditor.putBoolean("csc3", false).apply();
        mEditor.putBoolean("csc4", false).apply();
        mEditor.putBoolean("csc5", false).apply();
        mEditor.putBoolean("csc6", false).apply();
        mEditor.putBoolean("csc7", false).apply();
        mEditor.putBoolean("csc8", false).apply();
        mEditor.putBoolean("csc10", false).apply();
        mEditor.putBoolean("csc11", false).apply();
        mEditor.putBoolean("bloat1", false).apply();
        mEditor.putBoolean("bloat2", false).apply();
        mEditor.putBoolean("bloat3", false).apply();
        mEditor.putBoolean("bloat4", false).apply();
        mEditor.putBoolean("bloat5", false).apply();
        mEditor.putBoolean("bloat6", false).apply();
        mEditor.putBoolean("bloat7", false).apply();
        // Lists
        mEditor.putString("listKernel", "1").apply();
        mEditor.putString("listStatusBar", "1").apply();
        mEditor.putString("listMyFiles", "1").apply();
        mEditor.putString("listKey", "1").apply();
        mEditor.putString("listMMS", "1").apply();
        mEditor.putString("listFonts", "1").apply();
        mEditor.putString("listAccuweather", "1").apply();
        mEditor.putString("listSound", "1").apply();
        mEditor.putString("listBootanimation", "1").apply();
    }

    @Nullable
    public static Integer IconColorChooser() {
        Integer mTheme = null;

        try {
            mTheme = Utils.ACTIVITY.getPackageManager().getPackageInfo(Utils.ACTIVITY.getPackageName(), 0).applicationInfo.theme;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        switch (mTheme) {
            case R.style.AppTheme_Light:
                return R.color.colorPrimary_Theme_Light;
            case R.style.AppTheme_Dark:
                return android.R.color.white;
            default:
                return null;
        }
    }

    @Nullable
    public static Integer ButtonBorderColorChooser() {
        Integer mTheme = null;

        try {
            mTheme = Utils.ACTIVITY.getPackageManager().getPackageInfo(Utils.ACTIVITY.getPackageName(), 0).applicationInfo.theme;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        switch (mTheme) {
            case R.style.AppTheme_Light:
                return R.color.colorPrimaryDark_Theme_Light;
            case R.style.AppTheme_Dark:
                return R.color.colorAccent_Theme_Dark;
            default:
                return null;
        }
    }

}