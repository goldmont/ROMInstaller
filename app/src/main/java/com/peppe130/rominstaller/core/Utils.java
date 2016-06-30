package com.peppe130.rominstaller.core;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.afollestad.materialdialogs.MaterialDialog;


@SuppressWarnings("ResultOfMethodCallIgnored, unused, ConstantConditions")
public class Utils {

    public static Toast TOAST;
    public static AppCompatActivity ACTIVITY;
    public static File ZIP_FILE;

    public static String MODEL;
    public static String FILE_NAME;

    public static Boolean SHOULD_CLOSE_ACTIVITY = false;
    public static Boolean SHOULD_LOCK_UI = true;

    public static ArrayList<DownloadManager.Request> DOWNLOAD_REQUEST_LIST = new ArrayList<>();
    public static ArrayList<Uri> DOWNLOAD_LINK_LIST = new ArrayList<>();
    public static ArrayList<File> DOWNLOAD_DIRECTORY_LIST = new ArrayList<>();
    public static ArrayList<String> DOWNLOADED_FILE_NAME_LIST = new ArrayList<>();
    public static ArrayList<String> DOWNLOADED_FILE_MD5_LIST = new ArrayList<>();


    public static void Sleep(long mTime) {

        try {
            Thread.sleep(mTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void ToastShort(Context mContext, String mString) {

        if (TOAST != null) {
            TOAST.cancel();
            TOAST = null;
        }

        TOAST = Toast.makeText(mContext, mString, Toast.LENGTH_SHORT);
        TOAST.show();

    }

    public static void ToastLong(Context mContext, String mString) {

        if (TOAST != null) {
            TOAST.cancel();
            TOAST = null;
        }

        TOAST = Toast.makeText(mContext, mString, Toast.LENGTH_LONG);
        TOAST.show();

    }

    public static void FileChooser() {

        CustomFileChooser mCustomFileChooserDialog = new CustomFileChooser.Builder(ACTIVITY)
                .initialPath(Environment.getExternalStorageDirectory().getPath())
                .build();
        mCustomFileChooserDialog.setCancelable(false);
        mCustomFileChooserDialog.show(ACTIVITY);

    }

    public static void FollowMeDialog(String[] items, final String[] links) {
        new MaterialDialog.Builder(ACTIVITY)
                .items(items)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        ACTIVITY.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(links[which])));
                    }
                })
                .show();
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

    public static boolean copyAssetFolder(AssetManager manager, String fromPath, String toPath) {

        try {
            String[] files = manager.list(fromPath);
            new File(toPath).mkdirs();
            boolean res = true;
            for (String file : files)
                if (file.contains("."))
                    res &= copyAsset(manager,
                            fromPath + "/" + file,
                            toPath + "/" + file);
                else
                    res &= copyAssetFolder(manager,
                            fromPath + "/" + file,
                            toPath + "/" + file);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean copyAsset(AssetManager manager, String fromPath, String toPath) {

        InputStream in;
        OutputStream out;

        try {
            in = manager.open(fromPath);
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