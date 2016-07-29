package com.peppe130.rominstaller;

import org.jetbrains.annotations.Contract;
import android.support.annotation.Nullable;
import android.content.Intent;
import android.net.Uri;

import com.mikepenz.entypo_typeface_library.Entypo;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.peppe130.rominstaller.core.Utils;


@SuppressWarnings("ConstantConditions")
public class ControlCenter {

    public static String[] DEVICE_COMPATIBILITY_LIST = new String[] {"SM-G920F"};
    public static String[] ROM_MD5_LIST = new String[] {"b14da8918a4359c809f83884c5ec2f5b"};
    public static String[] RECOVERY_MD5_LIST = new String[] {"5fb732eea3d3e2b407fa7685c27a5354"};

    public static Boolean TEST_MODE = false;
    public static Boolean TRIAL_MODE = true;
    public static Boolean BUTTON_UI = false;
    public static Boolean SHOULD_SHOW_SPLASH_SCREEN = true;
    public static Boolean SHOULD_SHOW_DISCLAIMER_SCREEN = true;

    public static Integer SPLASH_SCREEN_DELAY = 3000;
    public static Integer SPLASH_SCREEN_IMAGE = R.drawable.rom_logo_light;
    public static Integer AVAILABLE_DOWNLOADS_NUMBER = 9;

    public static IIcon SETTINGS_ICON = GoogleMaterial.Icon.gmd_settings;
    public static IIcon CHANGELOG_ICON = Ionicons.Icon.ion_ios_paper_outline;
    public static IIcon DEFAULT_VALUES_ICON = GoogleMaterial.Icon.gmd_settings_backup_restore;
    public static IIcon CLEAR_DOWNLOADS_ICON = Entypo.Icon.ent_trash;


    public static void DownloadROM() {

        Utils.FILE_NAME = "SampleROM.zip";

        Utils.StartDownloadROM(
                "http://www.mediafire.com/download/hc9r53djj82zncx/SampleROM.zip",
                Utils.ACTIVITY.getString(R.string.rom_folder));

    }

    public static void ROMUtils() {
        // Do something
    }

    public static void ROMDeveloperInfoAction() {
        // Do something
    }

    public static void ROMThemerInfoAction() {
        // Do something
    }

    public static void ROMThreadInfoAction() {
        Uri mUri = Uri.parse("http://forum.xda-developers.com/galaxy-s4/i9505-develop/rom-osiris-rom-v1-0-t3147053");
        Utils.ACTIVITY.startActivity(new Intent(Intent.ACTION_VIEW, mUri));
    }

    @Nullable
    @Contract(pure = true)
    public static String DownloadNameGetter(Integer mInt) {

        switch (mInt) {
            case 0:
                return "Download test\n(Without MD5 check)";
            case 1:
                return "Download test\n(With MD5 check - MD5 matches)";
            case 2:
                return "Download test\n(With MD5 check - MD5 does not match)";
            case 3:
                return "Download test\n(Download ROM)";
            case 4:
                return "Download test\n(Download Recovery)";
            case 5:
                return "Download test\n(Download Recovery with Add-Ons)";
            case 6:
                return "Download test\n(Multiple downloads without MD5 check)";
            case 7:
                return "Download test\n(Multiple downloads with MD5 check)";
            case 8:
                return "Download test\n(Multiple downloads mixed)";
        }

        return null;

    }

    public static void DownloadActionGetter(Integer mInt) {

        String mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5, mRecoveryPartition;

        switch (mInt) {
            case 0: // Single download without MD5 check
                mDownloadLink = "http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip";
                mDownloadDirectory = Utils.ACTIVITY.getString(R.string.rom_folder);
                mDownloadedFileFinalName = "Test.zip";
                mDownloadedFileMD5 = null;

                Utils.StartSingleDownload(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5);
                break;
            case 1: // Single download with MD5 check - MD5 matches
                mDownloadLink = "http://www.mediafire.com/download/a0jswcs84smbi8i/Test2.zip";
                mDownloadDirectory = Utils.ACTIVITY.getString(R.string.rom_folder);
                mDownloadedFileFinalName = "Test2.zip";
                mDownloadedFileMD5 = "3a416cafb312cb15ce6b3b09249fe6e6";

                Utils.StartSingleDownload(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5);
                break;
            case 2: // Single download with MD5 check - MD5 does not match
                mDownloadLink = "http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip";
                mDownloadDirectory = Utils.ACTIVITY.getString(R.string.rom_folder);
                mDownloadedFileFinalName = "Test3.zip";
                mDownloadedFileMD5 = "3a416cafb312cb15ce6b3b09249fe6e6";

                Utils.StartSingleDownload(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5);
                break;
            case 3: // Download ROM
                ControlCenter.DownloadROM();
                break;
            case 4: // Download Recovery
                mDownloadLink = "http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip";
                mDownloadDirectory = Utils.ACTIVITY.getString(R.string.rom_folder);
                mDownloadedFileFinalName = "Recovery.zip";
                mRecoveryPartition = "YourDeviceRecoveryPartition";

                Utils.StartFlashRecovery(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mRecoveryPartition);
                break;
            case 5: // Download Recovery with Add-Ons
                // Download Recovery
                mDownloadLink = "http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip";
                mDownloadDirectory = Utils.ACTIVITY.getString(R.string.rom_folder);
                mDownloadedFileFinalName = "Recovery.zip";
                mRecoveryPartition = "YourDeviceRecoveryPartition";

                // Download Add-On N°1
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip",
                        Utils.ACTIVITY.getString(R.string.rom_folder),
                        "Add-On.zip",
                        "5fb732eea3d3e2b407fa7685c27a5354");

                // Download Add-On N°2
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/a0jswcs84smbi8i/Test2.zip",
                        Utils.ACTIVITY.getString(R.string.rom_folder),
                        "Add-On2.zip",
                        "3a416cafb312cb15ce6b3b09249fe6e6");

                // Download Add-On N°3
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip",
                        Utils.ACTIVITY.getString(R.string.rom_folder),
                        "Add-On3.zip",
                        "f946055c11a6a25d202f81171944fa1e");

                Utils.StartFlashRecoveryWithAddons(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mRecoveryPartition);
                break;
            case 6: // Multiple downloads without MD5 check
                // Download N°1
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip",
                        Utils.ACTIVITY.getString(R.string.rom_folder),
                        "Test.zip", null);

                // Download N°2
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/a0jswcs84smbi8i/Test2.zip",
                        Utils.ACTIVITY.getString(R.string.rom_folder),
                        "Test2.zip", null);

                // Download N°3
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip",
                        Utils.ACTIVITY.getString(R.string.rom_folder),
                        "Test3.zip", null);

                Utils.StartMultipleDownloads();
                break;
            case 7: // Multiple downloads with MD5 check
                // Download N°1
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip",
                        Utils.ACTIVITY.getString(R.string.rom_folder),
                        "Test.zip",
                        "5fb732eea3d3e2b407fa7685c27a5354");

                // Download N°2
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/a0jswcs84smbi8i/Test2.zip",
                        Utils.ACTIVITY.getString(R.string.rom_folder),
                        "Test2.zip",
                        "3a416cafb312cb15ce6b3b09249fe6e6");

                // Download N°3
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip",
                        Utils.ACTIVITY.getString(R.string.rom_folder),
                        "Test3.zip",
                        "f946055c11a6a25d202f81171944fa1e");

                Utils.StartMultipleDownloads();
                break;
            case 8: // Multiple downloads mixed
                // Download N°1 - MD5 matches
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip",
                        Utils.ACTIVITY.getString(R.string.rom_folder),
                        "Test.zip",
                        "5fb732eea3d3e2b407fa7685c27a5354");

                // Download N°2 - MD5 does not match
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/a0jswcs84smbi8i/Test2.zip",
                        Utils.ACTIVITY.getString(R.string.rom_folder),
                        "Test2.zip",
                        "3a416cafb312cb15ce6b3b09249fe6e6sd");

                // Download N°3 - No MD5 check
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip",
                        Utils.ACTIVITY.getString(R.string.rom_folder),
                        "Test3.zip", null);

                // Download N°4 - MD5 matches
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip",
                        Utils.ACTIVITY.getString(R.string.rom_folder),
                        "Test4.zip",
                        "f946055c11a6a25d202f81171944fa1e");

                Utils.StartMultipleDownloads();
                break;
        }

    }

}