package com.peppe130.rominstaller;

import org.jetbrains.annotations.Contract;
import android.support.annotation.Nullable;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.peppe130.rominstaller.core.Utils;
import com.peppe130.rominstaller.core.FragmentsCollector;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.entypo_typeface_library.Entypo;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import mehdi.sakout.fancybuttons.FancyButton;


@SuppressWarnings("ConstantConditions")
public class ControlCenter {

    //•• MD5 ••/////////////////////////////////////////////////////////////////////////////////////

    public static String[] DEVICE_COMPATIBILITY_LIST = new String[] {"Any"};
    public static String[] ROM_MD5_LIST = new String[] {"b14da8918a4359c809f83884c5ec2f5b"};
    public static String[] RECOVERY_MD5_LIST = new String[] {"5fb732eea3d3e2b407fa7685c27a5354"};

    //•• Modes & UI ••//////////////////////////////////////////////////////////////////////////////

    public static Boolean PREVIEW_MODE = true;
    public static Boolean TRIAL_MODE = true;
    public static Boolean BUTTON_UI = true;

    //•• Manage Screens ••//////////////////////////////////////////////////////////////////////////

    public static Boolean SHOULD_SHOW_SPLASH_SCREEN = true;
    public static Boolean SHOULD_SHOW_DISCLAIMER_SCREEN = true;

    //•• Splash Screen ••///////////////////////////////////////////////////////////////////////////

    public static Integer SPLASH_SCREEN_DELAY = 3000;
    public static Integer SPLASH_SCREEN_IMAGE_LIGHT = R.drawable.rom_logo_light;
    public static Integer SPLASH_SCREEN_IMAGE_DARK = R.drawable.rom_logo_dark;

    //•• Toolbar Icons ••///////////////////////////////////////////////////////////////////////////

    public static IIcon SETTINGS_ICON = GoogleMaterial.Icon.gmd_settings;
    public static IIcon CHANGELOG_ICON = Ionicons.Icon.ion_ios_paper_outline;
    public static IIcon DEFAULT_VALUES_ICON = GoogleMaterial.Icon.gmd_settings_backup_restore;
    public static IIcon CLEAR_DOWNLOADS_ICON = Entypo.Icon.ent_trash;

    //•• Fragments ••///////////////////////////////////////////////////////////////////////////////

    public static Boolean SHOULD_SHOW_HOME_FRAGMENT = true;

    public static Integer[] FRAGMENTS_RESOURCES = new Integer[] {
            R.xml.first_fragment, R.xml.second_fragment, R.xml.third_fragment
    };

    @SuppressWarnings("unused")
    public static void HomeFragment(Context context, FragmentsCollector.HomeFragment fragment, View view) {

        TextView mTextView = (TextView) view.findViewById(R.id.textView_home_fragment);

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.ToastLong(Utils.ACTIVITY, "This is a TextView!");

            }
        });

        ImageView mImageView = (ImageView) view.findViewById(R.id.imageView_home_fragment);

        if (Utils.FetchTheme(Utils.ACTIVITY) == 0) {

            mImageView.setImageResource(R.drawable.app_icon_home_fragment_light);

        } else {

            mImageView.setImageResource(R.drawable.app_icon_home_fragment_dark);

        }

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.ToastLong(Utils.ACTIVITY, "This is an ImageView!");

            }
        });

        final FancyButton mButton = (FancyButton) view.findViewById(R.id.button_home_fragment);

        mButton.setBackgroundColor(ContextCompat.getColor(Utils.ACTIVITY, Utils.FetchPrimaryColor()));

        mButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        mButton.setBorderColor(ContextCompat.getColor(Utils.ACTIVITY, Utils.FetchButtonBorderColor()));

                        break;

                    case MotionEvent.ACTION_UP:

                        mButton.setBorderColor(0);

                        break;

                    case MotionEvent.ACTION_CANCEL:

                        mButton.setBorderColor(0);

                        break;

                }

                return false;

            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.ToastLong(Utils.ACTIVITY, "This is a Button!");

            }
        });

    }

    //•• Download Center ••/////////////////////////////////////////////////////////////////////////

    public static Integer AVAILABLE_DOWNLOADS_NUMBER = 8;

    public static void DownloadROM() {

        Utils.StartDownloadROM(
                "http://www.mediafire.com/download/hc9r53djj82zncx/SampleROM.zip",
                Utils.ROM_DOWNLOAD_FOLDER, "SampleROM.zip");

    }

    @Nullable
    @Contract(pure = true)
    public static String DownloadNameGetter(Integer ID) {

        switch (ID) {

            case 0:

                return "Download test\n(Without MD5 check)";

            case 1:

                return "Download test\n(With MD5 check - MD5 matches)";

            case 2:

                return "Download test\n(With MD5 check - MD5 does not match)";

            case 3:

                return "Download test\n(Download Recovery)";

            case 4:

                return "Download test\n(Download Recovery with Add-Ons)";

            case 5:

                return "Download test\n(Multiple downloads without MD5 check)";

            case 6:

                return "Download test\n(Multiple downloads with MD5 check)";

            case 7:

                return "Download test\n(Multiple downloads mixed)";

        }

        return null;

    }

    public static void DownloadActionGetter(Integer ID) {

        String mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5, mRecoveryPartition;

        switch (ID) {

            case 0: // Single download without MD5 check

                mDownloadLink = "http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip";
                mDownloadDirectory = Utils.ROM_DOWNLOAD_FOLDER;
                mDownloadedFileFinalName = "Test.zip";
                mDownloadedFileMD5 = null;

                Utils.StartSingleDownload(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5);

                break;

            case 1: // Single download with MD5 check - MD5 matches

                mDownloadLink = "http://www.mediafire.com/download/a0jswcs84smbi8i/Test2.zip";
                mDownloadDirectory = Utils.ROM_DOWNLOAD_FOLDER;
                mDownloadedFileFinalName = "Test2.zip";
                mDownloadedFileMD5 = "3a416cafb312cb15ce6b3b09249fe6e6";

                Utils.StartSingleDownload(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5);

                break;

            case 2: // Single download with MD5 check - MD5 does not match

                mDownloadLink = "http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip";
                mDownloadDirectory = Utils.ROM_DOWNLOAD_FOLDER;
                mDownloadedFileFinalName = "Test3.zip";
                mDownloadedFileMD5 = "3a416cafb312cb15ce6b3b09249fe6e6";

                Utils.StartSingleDownload(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5);

                break;

            case 3: // Download Recovery

                mDownloadLink = "http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip";
                mDownloadDirectory = Utils.ROM_DOWNLOAD_FOLDER;
                mDownloadedFileFinalName = "Recovery.zip";
                mRecoveryPartition = "YourDeviceRecoveryPartition";

                Utils.StartFlashRecovery(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mRecoveryPartition);

                break;

            case 4: // Download Recovery with Add-Ons

                // Download Recovery
                mDownloadLink = "http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip";
                mDownloadDirectory = Utils.ROM_DOWNLOAD_FOLDER;
                mDownloadedFileFinalName = "Recovery.zip";
                mRecoveryPartition = "YourDeviceRecoveryPartition";

                // Download Add-On N°1
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip",
                        Utils.ROM_DOWNLOAD_FOLDER,
                        "Add-On.zip",
                        "5fb732eea3d3e2b407fa7685c27a5354");

                // Download Add-On N°2
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/a0jswcs84smbi8i/Test2.zip",
                        Utils.ROM_DOWNLOAD_FOLDER,
                        "Add-On2.zip",
                        "3a416cafb312cb15ce6b3b09249fe6e6");

                // Download Add-On N°3
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip",
                        Utils.ROM_DOWNLOAD_FOLDER,
                        "Add-On3.zip",
                        "f946055c11a6a25d202f81171944fa1e");

                Utils.StartFlashRecoveryWithAddons(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mRecoveryPartition);

                break;

            case 5: // Multiple downloads without MD5 check

                // Download N°1
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip",
                        Utils.ROM_DOWNLOAD_FOLDER,
                        "Test.zip", null);

                // Download N°2
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/a0jswcs84smbi8i/Test2.zip",
                        Utils.ROM_DOWNLOAD_FOLDER,
                        "Test2.zip", null);

                // Download N°3
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip",
                        Utils.ROM_DOWNLOAD_FOLDER,
                        "Test3.zip", null);

                Utils.StartMultipleDownloads();

                break;

            case 6: // Multiple downloads with MD5 check

                // Download N°1
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip",
                        Utils.ROM_DOWNLOAD_FOLDER,
                        "Test.zip",
                        "5fb732eea3d3e2b407fa7685c27a5354");

                // Download N°2
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/a0jswcs84smbi8i/Test2.zip",
                        Utils.ROM_DOWNLOAD_FOLDER,
                        "Test2.zip",
                        "3a416cafb312cb15ce6b3b09249fe6e6");

                // Download N°3
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip",
                        Utils.ROM_DOWNLOAD_FOLDER,
                        "Test3.zip",
                        "f946055c11a6a25d202f81171944fa1e");

                Utils.StartMultipleDownloads();

                break;

            case 7: // Multiple downloads mixed

                // Download N°1 - MD5 matches
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip",
                        Utils.ROM_DOWNLOAD_FOLDER,
                        "Test.zip",
                        "5fb732eea3d3e2b407fa7685c27a5354");

                // Download N°2 - MD5 does not match
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/a0jswcs84smbi8i/Test2.zip",
                        Utils.ROM_DOWNLOAD_FOLDER,
                        "Test2.zip",
                        "3a416cafb312cb15ce6b3b09249fe6e6sd");

                // Download N°3 - No MD5 check
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip",
                        Utils.ROM_DOWNLOAD_FOLDER,
                        "Test3.zip", null);

                // Download N°4 - MD5 matches
                Utils.EnqueueDownload(
                        "http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip",
                        Utils.ROM_DOWNLOAD_FOLDER,
                        "Test4.zip",
                        "f946055c11a6a25d202f81171944fa1e");

                Utils.StartMultipleDownloads();

                break;

        }

    }

    //•• Others ••//////////////////////////////////////////////////////////////////////////////////

    public static void ROMUtils() {
        // Write here the code you want to run in the onResume() method of each activity
    }

    public static void ROMDeveloperInfoAction() {
        // Write here the code you want to run when a user click on the ROM developer header
    }

    public static void ROMThemerInfoAction() {
        // Write here the code you want to run when a user click on the ROM themer header
    }

    public static void ROMThreadInfoAction() {
        // Write here the code you want to run when a user click on the ROM thread header
    }

    public static void ROMGitHubInfoAction() {
        // Write here the code you want to run when a user click on the ROM GitHub header
    }

}