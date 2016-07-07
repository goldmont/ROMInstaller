package com.peppe130.rominstaller;

import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.app.DownloadManager;
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

        Utils.FILE_NAME = "SampleROM.zip";

        Utils.StartDownloadROM(
                "http://www.mediafire.com/download/3a7gefzmxj44lv1/SampleROM.zip",
                Utils.ACTIVITY.getString(R.string.rom_folder));

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

}