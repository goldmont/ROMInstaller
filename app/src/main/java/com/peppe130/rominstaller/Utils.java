package com.peppe130.rominstaller;

import android.app.DownloadManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.mikepenz.entypo_typeface_library.Entypo;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.peppe130.rominstaller.core.CustomFileChooser;
import com.peppe130.rominstaller.core.Download;


@SuppressWarnings("ResultOfMethodCallIgnored, unused")
public class Utils {

    public static Toast TOAST;
    public static AppCompatActivity ACTIVITY;
    public static File ZIP_FILE;

    public static String MODEL;
    public static String FILE_NAME;
    public static String[] DEVICE_COMPATIBILITY_LIST = new String[] {"SM-G920F"};
    public static String[] ROM_MD5_LIST = new String[] {"4ba678ca02e60557757d29e91e1e792f"};
    public static String[] RECOVERY_MD5_LIST = new String[] {"5fb732eea3d3e2b407fa7685c27a5354"};

    public static Boolean TEST_MODE = false;
    public static Boolean TRIAL_MODE = true;
    public static Boolean BUTTON_UI = false;
    public static Boolean SHOULD_SHOW_SPLASH_SCREEN = true;
    public static Boolean SHOULD_CLOSE_ACTIVITY = false;
    public static Boolean SHOULD_LOCK_UI = true;

    public static Integer SPLASH_SCREEN_DELAY = 3000;
    public static Integer SPLASH_SCREEN_IMAGE = R.drawable.rom_logo;
    public static Integer APP_ICON_MULTITASKING = R.mipmap.ic_launcher;
    public static Integer APP_HEADER_COLOR_MULTITASKING = R.color.colorPrimaryDark;
    public static Integer PROGRESS_BAR_COLOR = R.color.colorPrimary;
    public static Integer FILE_CHOOSER_TITLE_COLOR = Color.WHITE;
    public static Integer FILE_CHOOSER_CONTENT_COLOR = Color.WHITE;
    public static Integer FILE_CHOOSER_BACKGROUND_COLOR = R.color.colorBackground;

    public static IIcon SETTINGS_ICON = GoogleMaterial.Icon.gmd_settings;
    public static IIcon CHANGELOG_ICON = Ionicons.Icon.ion_ios_paper_outline;
    public static IIcon DEFAULT_OPTIONS_ICON = GoogleMaterial.Icon.gmd_settings_backup_restore;
    public static IIcon CLEAR_DOWNLOADS_ICON = Entypo.Icon.ent_trash;

    public static ArrayList<DownloadManager.Request> DOWNLOAD_REQUEST_LIST = new ArrayList<>();
    public static ArrayList<Uri> DOWNLOAD_LINK_LIST = new ArrayList<>();
    public static ArrayList<File> DOWNLOAD_DIRECTORY_LIST = new ArrayList<>();
    public static ArrayList<String> DOWNLOADED_FILE_NAME_LIST = new ArrayList<>();
    public static ArrayList<String> DOWNLOADED_FILE_MD5_LIST = new ArrayList<>();


    public static void ROMDownloadLinkGenerator() {

        Uri mDownloadLink = Uri.parse("http://www.mediafire.com/download/3a7gefzmxj44lv1/SampleROM.zip");
        File mDownloadDirectory = new File(ACTIVITY.getString(R.string.rom_download_folder));
        FILE_NAME = "SampleROM.zip";

        /*switch (Arrays.asList(DEVICE_COMPATIBILITY_LIST).indexOf(MODEL)) {
            case 0:
                mDownloadLink = Uri.parse("");
                mDownloadDirectory = new File("");
                FILE_NAME = "";
                break;
            case 1:
                mDownloadLink = Uri.parse("");
                mDownloadDirectory = new File("");
                FILE_NAME = "";
                break;

        }*/

        DownloadManager.Request mRequest = new DownloadManager.Request(mDownloadLink);
        mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), FILE_NAME);

        new Download(
                mRequest,
                mDownloadDirectory,
                FILE_NAME, true).execute();

    }

    public static void Sleep(long mTime) {

        try {
            Thread.sleep(mTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void ToastShort(Context mContext, String message) {

        if (TOAST != null) {
            TOAST.cancel();
            TOAST = null;
        }

        TOAST = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        TOAST.show();

    }

    public static void ToastLong(Context mContext, String message) {

        if (TOAST != null) {
            TOAST.cancel();
            TOAST = null;
        }

        TOAST = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
        TOAST.show();

    }

    public static void FileChooser() {

        CustomFileChooser mCustomFileChooserDialog = new CustomFileChooser.Builder(ACTIVITY)
                .initialPath(Environment.getExternalStorageDirectory().getPath())
                .build();
        mCustomFileChooserDialog.setCancelable(false);
        mCustomFileChooserDialog.show(ACTIVITY);

    }

    public static void deleteFolderRecursively(String path) {

        File mFile = new File(path);

        if (mFile.exists()) {
            String mCommand = "rm -r " + path;
            Runtime mRuntime = Runtime.getRuntime();
            try {
                mRuntime.exec(mCommand);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean copyAssetFolder(AssetManager assetManager, String fromAssetPath, String toPath) {

        try {
            String[] files = assetManager.list(fromAssetPath);
            new File(toPath).mkdirs();
            boolean res = true;
            for (String file : files)
                if (file.contains("."))
                    res &= copyAsset(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
                else
                    res &= copyAssetFolder(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean copyAsset(AssetManager assetManager, String fromAssetPath, String toPath) {

        InputStream in;
        OutputStream out;

        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {

        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }

    }

}