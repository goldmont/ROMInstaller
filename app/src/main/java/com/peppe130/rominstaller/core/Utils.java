/*

    Copyright © 2016, Giuseppe Montuoro.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package com.peppe130.rominstaller.core;

import android.annotation.SuppressLint;
import org.jetbrains.annotations.Contract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.ComponentName;
import android.os.Handler;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.BuildConfig;
import com.peppe130.rominstaller.ControlCenter;
import com.peppe130.bouncingdialogs.BouncingDialog;
import com.peppe130.rominstaller.activities.MainActivity;
import com.peppe130.rominstaller.activities.InfoActivity;
import com.peppe130.rominstaller.activities.SplashScreenActivity;
import com.afollestad.assent.Assent;
import com.afollestad.assent.AssentCallback;
import com.afollestad.assent.PermissionResultSet;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;


@SuppressLint("CommitPrefEdits, StaticFieldLeak")
@SuppressWarnings("ResultOfMethodCallIgnored, unused, ConstantConditions")
public class Utils {

    static Toast TOAST;
    public static Snackbar SNACKBAR;
    public static AppCompatActivity ACTIVITY;
    private static File ZIP_FILE;
    public static SharedPreferences SP;
    public static SharedPreferences.Editor SP_EDITOR;

    private static String MODEL;
    private static String FILE_NAME;
    public static String ROM_FOLDER;
    public static String ROM_DOWNLOAD_FOLDER;
    public static String TAG = "ROM_INSTALLER";

    public static Boolean SHOULD_LOCK_UI = true;
    static Boolean GRANT_INITIAL_ROOT_ACCESS = false;

    static ArrayList<Uri> DOWNLOAD_LINK_LIST = new ArrayList<>();
    static ArrayList<File> DOWNLOAD_DIRECTORY_LIST = new ArrayList<>();
    static ArrayList<String> DOWNLOADED_FILE_NAME_LIST = new ArrayList<>();
    static ArrayList<String> DOWNLOADED_FILE_MD5_LIST = new ArrayList<>();
    static ArrayList<DownloadManager.Request> DOWNLOAD_REQUEST_LIST = new ArrayList<>();


    public enum Action {

        FILE_CHOOSER, INSTALL_DIALOG

    }

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

    public static void Snackbar(View view, CharSequence text, Integer duration) {

        if (SNACKBAR != null) {

            SNACKBAR.dismiss();

            SNACKBAR = null;

        }

        SNACKBAR = Snackbar.make(view, text, duration);

        SNACKBAR.show();

    }

    public static void InitializeActivity(AppCompatActivity mActivity) {

        ACTIVITY = mActivity;

        SP = PreferenceManager.getDefaultSharedPreferences(mActivity);

        SP_EDITOR = SP.edit();

    }

    public static void EnvironmentChecker(AppCompatActivity mActivity) {

        ACTIVITY = mActivity;

        SP = PreferenceManager.getDefaultSharedPreferences(mActivity);

        SP_EDITOR = SP.edit();

        GenerateROMFolder();

        ControlCenter.ROMUtils();

        File mROMFolder = new File(Environment.getExternalStorageDirectory().getPath() + "/" + ROM_DOWNLOAD_FOLDER);

        File mSampleZIP = new File(mROMFolder.getPath() + "/" + "Sample.zip");

        if (!mROMFolder.exists()) {

            mROMFolder.mkdirs();

        }

        if (ControlCenter.TRIAL_MODE && !mSampleZIP.exists()) {

            CopyAssetFolder(ACTIVITY.getAssets(), "sample", mROMFolder.toString());

        }

    }

    public static void CheckPermissions(final Action action) {

        if (!Assent.isPermissionGranted(Assent.WRITE_EXTERNAL_STORAGE)) {

            Assent.requestPermissions(new AssentCallback() {
                @Override
                public void onPermissionResult(PermissionResultSet result) {

                    if (result.allPermissionsGranted()) {

                        ActionChooser(action);

                    } else {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                BouncingDialog bouncingDialog = new BouncingDialog(ACTIVITY, BouncingDialog.ERROR_TYPE)
                                        .title(ACTIVITY.getString(R.string.permission_denied_title))
                                        .content(ACTIVITY.getString(R.string.permission_denied_message))
                                        .positiveText(ACTIVITY.getString(R.string.ok_button))
                                        .onPositive(new BouncingDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(BouncingDialog bouncingDialog1) {

                                                bouncingDialog1.dismissWithAnimation();

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        CheckPermissions(action);

                                                    }
                                                }, 300);

                                            }
                                        })
                                        .negativeText(ACTIVITY.getString(R.string.close_button))
                                        .onNegative(new BouncingDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(BouncingDialog bouncingDialog1) {

                                                bouncingDialog1.dismiss();

                                                ACTIVITY.finishAffinity();

                                            }
                                        });
                                bouncingDialog.setCancelable(false);
                                bouncingDialog.show();

                            }
                        }, 300);

                    }

                }
            }, 69, Assent.WRITE_EXTERNAL_STORAGE);

        } else {

            ActionChooser(action);

        }

    }

    private static void ActionChooser(Action action) {

        switch (action) {

            case FILE_CHOOSER:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        FileChooser();

                    }
                }, 150);

                break;

            case INSTALL_DIALOG:

                ACTIVITY.getFragmentManager().beginTransaction()
                        .add(new InstallDialog(), "install_dialog")
                        .commitAllowingStateLoss();

                break;

        }

    }

    public static void ExportPreferences() {

        try {

            FileWriter mFileWriter = new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/" + ROM_FOLDER + "/" + "preferences.prop");

            BufferedWriter mBufferedWriter = new BufferedWriter(mFileWriter);

            Map<String,?> mPreferences = SP.getAll();

            mPreferences.remove("file_path");

            mPreferences.remove("default_values");

            mPreferences.remove("theme");

            mPreferences.remove("activity_type");

            mPreferences.remove("activity_alias");

            for (Map.Entry<String,?> entry : mPreferences.entrySet()) {

                mBufferedWriter.write(

                        entry.getKey() + "=" + entry.getValue().toString() + "\n"

                );

            }

            mBufferedWriter.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void SetDefaultValues() {

        try {

            Map<String,?> mPreferences = SP.getAll();

            mPreferences.remove("file_path");

            mPreferences.remove("default_values");

            mPreferences.remove("theme");

            mPreferences.remove("activity_type");

            mPreferences.remove("activity_alias");

            for (Map.Entry<String,?> entry : mPreferences.entrySet()) {

                SP_EDITOR.remove(entry.getKey()).apply();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void StartSingleDownload(String downloadLink, String downloadDirectory, String downloadedFileFinalName, String downloadedFileMD5) {

        Uri mDownloadLink = Uri.parse(downloadLink);

        File mDownloadDirectory = new File(downloadDirectory);

        DownloadManager.Request mRequest = new DownloadManager.Request(mDownloadLink);

        mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), downloadedFileFinalName);

        mRequest.setTitle(ACTIVITY.getString(R.string.app_name));

        mRequest.setDescription(downloadedFileFinalName);

        new Download(
                mRequest,
                mDownloadDirectory,
                downloadedFileFinalName,
                downloadedFileMD5).execute();

    }

    public static void StartDownloadROM(String downloadLink, String downloadDirectory, String downloadedFileFinalName) {

        FILE_NAME = downloadedFileFinalName;

        Uri mDownloadLink = Uri.parse(downloadLink);

        File mDownloadDirectory = new File(downloadDirectory);

        DownloadManager.Request mRequest = new DownloadManager.Request(mDownloadLink);

        mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), downloadedFileFinalName);

        mRequest.setTitle(ACTIVITY.getString(R.string.app_name));

        mRequest.setDescription(downloadedFileFinalName);

        new Download(
                mRequest,
                mDownloadDirectory,
                FILE_NAME, true).execute();

    }

    public static void StartFlashRecovery(String downloadLink, String downloadDirectory, String downloadedFileFinalName, String recoveryPartition) {

        Uri mDownloadLink = Uri.parse(downloadLink);

        File mDownloadDirectory = new File(downloadDirectory);

        DownloadManager.Request mRequest = new DownloadManager.Request(mDownloadLink);

        mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), downloadedFileFinalName);

        mRequest.setTitle(ACTIVITY.getString(R.string.app_name));

        mRequest.setDescription(downloadedFileFinalName);

        new FlashRecovery(
                mRequest,
                mDownloadDirectory,
                downloadedFileFinalName,
                recoveryPartition, false).execute();

    }

    public static void StartFlashRecoveryWithAddons(String downloadLink, String downloadDirectory, String downloadedFileFinalName, String recoveryPartition) {

        Uri mDownloadLink = Uri.parse(downloadLink);

        File mDownloadDirectory = new File(downloadDirectory);

        DownloadManager.Request mRequest = new DownloadManager.Request(mDownloadLink);

        mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), downloadedFileFinalName);

        mRequest.setTitle(ACTIVITY.getString(R.string.app_name));

        mRequest.setDescription(downloadedFileFinalName);

        new FlashRecovery(
                mRequest,
                mDownloadDirectory,
                downloadedFileFinalName,
                recoveryPartition, true).execute();

    }

    public static void EnqueueDownload(String downloadLink, String downloadDirectory, String downloadedFileFinalName, String downloadedFileMD5) {

        DOWNLOAD_LINK_LIST.add(Uri.parse(downloadLink));

        DOWNLOAD_DIRECTORY_LIST.add(new File(downloadDirectory));

        DOWNLOADED_FILE_NAME_LIST.add(downloadedFileFinalName);

        DOWNLOADED_FILE_MD5_LIST.add(downloadedFileMD5);

        DownloadManager.Request mRequest = new DownloadManager.Request(DOWNLOAD_LINK_LIST.get(DOWNLOAD_LINK_LIST.size() - 1));

        mRequest.setDestinationInExternalPublicDir(DOWNLOAD_DIRECTORY_LIST.get(DOWNLOAD_DIRECTORY_LIST.size() - 1).getPath(), DOWNLOADED_FILE_NAME_LIST.get(DOWNLOADED_FILE_NAME_LIST.size() - 1));

        mRequest.setTitle(ACTIVITY.getString(R.string.app_name));

        mRequest.setDescription(downloadedFileFinalName);

        DOWNLOAD_REQUEST_LIST.add(mRequest);

    }

    public static void StartMultipleDownloads() {

        new Download(
                DOWNLOAD_REQUEST_LIST.get(0),
                DOWNLOAD_DIRECTORY_LIST.get(0),
                DOWNLOADED_FILE_NAME_LIST.get(0),
                DOWNLOADED_FILE_MD5_LIST.get(0), 1).execute();

    }

    private static NetworkInfo GetNetworkInfo() {

        ConnectivityManager mConnectivityManager = (ConnectivityManager) ACTIVITY.getSystemService(Context.CONNECTIVITY_SERVICE);

        return mConnectivityManager.getActiveNetworkInfo();

    }

    public static boolean isConnected() {

        return (GetNetworkInfo() != null && GetNetworkInfo().isConnected());

    }

    public static boolean isWifiAvailable() {

        return (GetNetworkInfo() != null && GetNetworkInfo().isConnected() && GetNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI);

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
                .items((CharSequence[]) items)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        try {

                            ACTIVITY.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(links[which])));

                        } catch (android.content.ActivityNotFoundException anfe) {

                            ToastLong(ACTIVITY, ACTIVITY.getString(R.string.no_application_found));

                        }

                    }

                })
                .show();

    }

    private static void GenerateROMFolder() {

        String mPath = ACTIVITY.getString(R.string.app_name).replace(" ", "");

        if (!mPath.equals("ROMInstaller")) {

            if (mPath.contains("Installer")) {

                mPath = mPath.replace("Installer", "");

            }

            if (mPath.contains("installer")) {

                mPath = mPath.replace("installer", "");

            }

        }

        ROM_FOLDER = mPath;

        ROM_DOWNLOAD_FOLDER = mPath + "/Download";

    }

    public static void DeleteFolderRecursively(String path) {

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

    @Nullable
    public static Integer FetchPrimaryColor() {

        Integer mTheme = null;

        try {

            Class<?> mClass = ContextThemeWrapper.class;

            Method mMethod = mClass.getMethod("getThemeResId");

            mMethod.setAccessible(true);

            mTheme = (Integer) mMethod.invoke(ACTIVITY);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {

            e.printStackTrace();

        }

        switch (mTheme) {

            case R.style.AppTheme_Light:

                return R.color.colorPrimary_Theme_Light;

            case R.style.AppTheme_Dark:

                return R.color.colorPrimary_Theme_Dark;

        }

        return null;

    }

    @Nullable
    public static Integer FetchAccentColor() {

        Integer mTheme = null;

        try {

            Class<?> mClass = ContextThemeWrapper.class;

            Method mMethod = mClass.getMethod("getThemeResId");

            mMethod.setAccessible(true);

            mTheme = (Integer) mMethod.invoke(ACTIVITY);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {

            e.printStackTrace();

        }

        switch (mTheme) {

            case R.style.AppTheme_Light:

                return R.color.colorAccent_Theme_Light;

            case R.style.AppTheme_Dark:

                return R.color.colorAccent_Theme_Dark;

        }

        return null;

    }

    @Nullable
    public static Integer FetchButtonBorderColor() {

        Integer mTheme = null;

        try {

            Class<?> mClass = ContextThemeWrapper.class;

            Method mMethod = mClass.getMethod("getThemeResId");

            mMethod.setAccessible(true);

            mTheme = (Integer) mMethod.invoke(ACTIVITY);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {

            e.printStackTrace();

        }

        switch (mTheme) {

            case R.style.AppTheme_Light:

                return R.color.downloadCenterButtonBorderColor_Theme_Light;

            case R.style.AppTheme_Dark:

                return R.color.downloadCenterButtonBorderColor_Theme_Dark;

        }

        return null;

    }

    @Nullable
    public static Integer FetchBackgroundColor() {

        Integer mTheme = null;

        try {

            Class<?> mClass = ContextThemeWrapper.class;

            Method mMethod = mClass.getMethod("getThemeResId");

            mMethod.setAccessible(true);

            mTheme = (Integer) mMethod.invoke(ACTIVITY);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {

            e.printStackTrace();

        }

        switch (mTheme) {

            case R.style.AppTheme_Light:

                return R.color.colorBackground_Theme_Light;

            case R.style.AppTheme_Dark:

                return R.color.colorBackground_Theme_Dark;

        }

        return null;

    }

    @Nullable
    public static Integer FetchIconColor() {

        Integer mTheme = null;

        try {

            Class<?> mClass = ContextThemeWrapper.class;

            Method mMethod = mClass.getMethod("getThemeResId");

            mMethod.setAccessible(true);

            mTheme = (Integer) mMethod.invoke(ACTIVITY);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {

            e.printStackTrace();

        }

        switch (mTheme) {

            case R.style.AppTheme_Light:

                return R.color.settingsPreferenceIconColor_Theme_Light;

            case R.style.AppTheme_Dark:

                return R.color.settingsPreferenceIconColor_Theme_Dark;

        }

        return null;

    }

    @Nullable
    public static Integer FetchTheme(Context context) {

        Integer mManifestTheme = null;

        Integer mUserTheme = SP.getInt("theme", 0);

        try {

            mManifestTheme = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.theme;

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

        }

        switch (mManifestTheme) {

            case R.style.AppTheme_Light:

                mManifestTheme = 0;

                break;

            case R.style.AppTheme_Dark:

                mManifestTheme = 1;

                break;

        }

        if (SP.contains("theme")) {

            return mUserTheme;

        }

        return mManifestTheme;

    }

    public static void ThemeChooser() {

        String[] mString = new String[] {ACTIVITY.getString(R.string.light_theme), ACTIVITY.getString(R.string.dark_theme)};

        new MaterialDialog.Builder(ACTIVITY)
                .items((CharSequence[]) mString)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {

                        final Integer mTheme = which;

                        final String mActivitySplashScreen = ACTIVITY.getPackageName() + ".activities.SplashScreenActivity";

                        final String mActivityAlias = ACTIVITY.getPackageName() + ".SplashScreenActivityDark";

                        SP_EDITOR.putInt("theme", which).apply();

                        SP_EDITOR.putBoolean("default_values", false).apply();

                        new MaterialDialog.Builder(ACTIVITY)
                                .title(ACTIVITY.getString(R.string.change_app_icon_dialog_title))
                                .content(ACTIVITY.getString(R.string.change_app_icon_dialog_message))
                                .positiveText(ACTIVITY.getString(R.string.positive_button))
                                .negativeText(ACTIVITY.getString(R.string.negative_button))
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        dialog.dismiss();

                                        Intent mIntent;

                                        Utils.SP_EDITOR.putBoolean("activity_alias", true).apply();

                                        switch (mTheme) {

                                            case 0:

                                                if (isComponentEnabled(ACTIVITY.getPackageManager(), ACTIVITY.getPackageName(), mActivitySplashScreen)) {

                                                    mIntent = new Intent(ACTIVITY, ControlCenter.SHOULD_SHOW_SPLASH_SCREEN ? SplashScreenActivity.class : MainActivity.class);

                                                    if (ControlCenter.SHOULD_SHOW_SPLASH_SCREEN) {

                                                        mIntent.putExtra("THEME", true);

                                                    }

                                                    ACTIVITY.finishAffinity();

                                                    ACTIVITY.startActivity(mIntent);

                                                    ACTIVITY.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                                } else {

                                                    SP_EDITOR.putInt("activity_type", 0).apply();

                                                    ACTIVITY.getPackageManager()
                                                            .setComponentEnabledSetting(
                                                                    new ComponentName(ACTIVITY, mActivitySplashScreen),
                                                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                                                    PackageManager.DONT_KILL_APP);

                                                    ACTIVITY.getPackageManager()
                                                            .setComponentEnabledSetting(
                                                                    new ComponentName(ACTIVITY.getPackageName(), mActivityAlias),
                                                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                                                    PackageManager.DONT_KILL_APP);

                                                }

                                                break;

                                            case 1:

                                                if (isComponentEnabled(ACTIVITY.getPackageManager(), ACTIVITY.getPackageName(), mActivityAlias)) {

                                                    mIntent = new Intent(Intent.ACTION_MAIN);

                                                    ComponentName componentName = new ComponentName(ACTIVITY.getPackageName(), mActivityAlias);

                                                    mIntent.setComponent(componentName);

                                                    if (ControlCenter.SHOULD_SHOW_SPLASH_SCREEN) {

                                                        mIntent.putExtra("THEME", true);

                                                    }

                                                    ACTIVITY.finishAffinity();

                                                    ACTIVITY.startActivity(mIntent);

                                                    ACTIVITY.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                                } else {

                                                    SP_EDITOR.putInt("activity_type", 1).apply();

                                                    ACTIVITY.getPackageManager()
                                                            .setComponentEnabledSetting(
                                                                    new ComponentName(ACTIVITY, mActivitySplashScreen),
                                                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                                                    PackageManager.DONT_KILL_APP);

                                                    ACTIVITY.getPackageManager()
                                                            .setComponentEnabledSetting(
                                                                    new ComponentName(ACTIVITY.getPackageName(), mActivityAlias),
                                                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                                                    PackageManager.DONT_KILL_APP);

                                                }

                                        }

                                        ACTIVITY.finishAffinity();

                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        dialog.dismiss();

                                        Boolean mIsActivityEnabled = (SP.contains("activity_type") && SP.getInt("activity_type", 0) == 0);

                                        Boolean mIsActivityAliasEnabled = (SP.contains("activity_type") && SP.getInt("activity_type", 0) == 1);

                                        Intent mIntent;

                                        if (mIsActivityEnabled && isComponentEnabled(ACTIVITY.getPackageManager(), ACTIVITY.getPackageName(), mActivitySplashScreen)) {

                                            mIntent = new Intent(ACTIVITY, ControlCenter.SHOULD_SHOW_SPLASH_SCREEN ? SplashScreenActivity.class : MainActivity.class);

                                        } else if (mIsActivityAliasEnabled && isComponentEnabled(ACTIVITY.getPackageManager(), ACTIVITY.getPackageName(), mActivityAlias)) {

                                            if (ControlCenter.SHOULD_SHOW_SPLASH_SCREEN) {

                                                mIntent = new Intent(Intent.ACTION_MAIN);

                                                ComponentName componentName = new ComponentName(ACTIVITY.getPackageName(), mActivityAlias);

                                                mIntent.setComponent(componentName);

                                            } else {

                                                mIntent = new Intent(ACTIVITY, MainActivity.class);

                                            }

                                        } else {

                                            SP_EDITOR.putInt("activity_type", 0).apply();

                                            mIntent = new Intent(ACTIVITY, ControlCenter.SHOULD_SHOW_SPLASH_SCREEN ? SplashScreenActivity.class : MainActivity.class);

                                        }

                                        if (ControlCenter.SHOULD_SHOW_SPLASH_SCREEN) {

                                            mIntent.putExtra("THEME", true);

                                        }

                                        ACTIVITY.finishAffinity();

                                        ACTIVITY.startActivity(mIntent);

                                        ACTIVITY.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                    }
                                })
                                .cancelable(false)
                                .show();

                    }
                })
                .show();

    }

    private static boolean isComponentEnabled(PackageManager packageManager, String packageName, String className) {

        ComponentName mComponentName = new ComponentName(packageName, className);

        Integer mComponentEnabledSetting = packageManager.getComponentEnabledSetting(mComponentName);

        switch (mComponentEnabledSetting) {

            case PackageManager.COMPONENT_ENABLED_STATE_ENABLED:

                return true;

            case PackageManager.COMPONENT_ENABLED_STATE_DISABLED:

                return false;

        }

        return false;

    }

    public static boolean CopyAssetFolder(AssetManager manager, String fromPath, String toPath) {

        try {

            String[] mFiles = manager.list(fromPath);

            new File(toPath).mkdirs();

            boolean mRes = true;

            for (String mFile : mFiles)

                if (mFile.contains(".")) {

                    mRes &= CopyAsset(manager, fromPath + "/" + mFile, toPath + "/" + mFile);

                } else {

                    mRes &= CopyAssetFolder(manager, fromPath + "/" + mFile, toPath + "/" + mFile);

                }

            return mRes;

        } catch (Exception e) {

            e.printStackTrace();

            return false;

        }

    }

    private static boolean CopyAsset(AssetManager manager, String fromPath, String toPath) {

        InputStream mIn;

        OutputStream mOut;

        try {

            mIn = manager.open(fromPath);

            new File(toPath).createNewFile();

            mOut = new FileOutputStream(toPath);

            CopyFile(mIn, mOut);

            mIn.close();

            mOut.flush();

            mOut.close();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;

        }

    }

    private static void CopyFile(InputStream in, OutputStream out) throws IOException {

        byte[] mBuffer = new byte[1024];

        int mRead;

        while ((mRead = in.read(mBuffer)) != -1) {

            out.write(mBuffer, 0, mRead);

        }

    }

    @NonNull
    public static Boolean SetZipFile(final File file) {

        if (CheckZipPath(file)) {

            ZIP_FILE = new File(file.getAbsolutePath());

            FILE_NAME = file.getName();

            return true;

        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    BouncingDialog mDialog = new BouncingDialog(ACTIVITY, BouncingDialog.ERROR_TYPE)
                            .title(ACTIVITY.getString(R.string.crash_zip_file_dialog_title))
                            .content(ACTIVITY.getString(R.string.crash_dialog_message))
                            .positiveText(ACTIVITY.getString(R.string.ok_button))
                            .onPositive(new BouncingDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(BouncingDialog bouncingDialog) {

                                    bouncingDialog.dismiss();

                                    throw new RuntimeException("Crash at \"SetZipFile()\" method.\nFile path: " + file.getAbsolutePath());

                                }
                            });
                    mDialog.setCancelable(false);
                    mDialog.show();

                }
            }, 300);

        }

        return false;

    }

    @NonNull
    private static Boolean CheckZipPath(File file) {

        @SuppressLint("SdCardPath")
        String[] mPaths = new String[] {"/storage/", "/sdcard/", "/mnt/"};

        for (String mPath : mPaths) {

            if (file.getAbsolutePath().contains(mPath)) {

                return true;

            }

        }

        return false;

    }

    @Contract(pure = true)
    public static File GetZipFile() {

        return ZIP_FILE;

    }

    @Contract(pure = true)
    public static String GetFileName() {

        return FILE_NAME;

    }

    @NonNull
    public static Boolean SetDeviceModel(final String model) {

        if (ControlCenter.TRIAL_MODE || Arrays.asList(ControlCenter.DEVICE_COMPATIBILITY_LIST).contains(model)) {

            MODEL = model;

            return true;

        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    BouncingDialog mDialog = new BouncingDialog(ACTIVITY, BouncingDialog.ERROR_TYPE)
                            .title(ACTIVITY.getString(R.string.crash_device_model_dialog_title))
                            .content(ACTIVITY.getString(R.string.crash_dialog_message))
                            .positiveText(ACTIVITY.getString(R.string.ok_button))
                            .onPositive(new BouncingDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(BouncingDialog bouncingDialog) {

                                    bouncingDialog.dismiss();

                                    throw new RuntimeException("Crash at \"SetDeviceModel()\" method.\nCurrent model: " + model + "\nDevice compatibility list: " + Arrays.asList(ControlCenter.DEVICE_COMPATIBILITY_LIST));

                                }
                            });
                    mDialog.setCancelable(false);
                    mDialog.show();

                }
            }, 300);

        }

        return false;

    }

    @Contract(pure = true)
    public static String GetDeviceModel() {

        return MODEL;

    }

    public static class InfoProjectPreferencesFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.project_info_section);

            Preference PROJECT_BUILD_NUMBER = findPreference("project_build_number");

            Preference PROJECT_DEVELOPER = findPreference("project_developer");

            Preference PROJECT_THEMER = findPreference("project_themer");

            Preference PROJECT_THREAD = findPreference("project_thread");

            Preference PROJECT_GITHUB = findPreference("project_github");

            Preference DONATE = findPreference("donate_project");

            Preference MY_APPS = findPreference("my_apps");

            PROJECT_BUILD_NUMBER.setIcon(InfoActivity.mInfoIcon);
            PROJECT_BUILD_NUMBER.setSummary(BuildConfig.PROJECT_VERSION);

            PROJECT_DEVELOPER.setIcon(InfoActivity.mDeveloperIcon);
            PROJECT_DEVELOPER.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    String[] mSocial = new String[] {"Google+", "Twitter"};

                    String[] mLinks = new String[] {"http://google.com/+PeppeMontuoro", "https://twitter.com/PeppeMontuoro"};

                    FollowMeDialog(mSocial, mLinks);

                    return false;

                }
            });

            PROJECT_THEMER.setIcon(InfoActivity.mThemerIcon);
            PROJECT_THEMER.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    String[] mSocial = new String[] {"Google+", "Twitter"};

                    String[] mLinks = new String[] {"http://google.com/+MRLOUDT_ONE", "https://twitter.com/MR_LOUD_T_ONE"};

                    FollowMeDialog(mSocial, mLinks);

                    return false;

                }
            });

            PROJECT_THREAD.setIcon(InfoActivity.mThreadIcon);
            PROJECT_THREAD.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Uri mUri = Uri.parse("http://forum.xda-developers.com/android/apps-games/app-rom-installer-to-flash-custom-rom-t3430099");

                    try {

                        startActivity(new Intent(Intent.ACTION_VIEW, mUri));

                    } catch (android.content.ActivityNotFoundException anfe) {

                        ToastLong(ACTIVITY, ACTIVITY.getString(R.string.no_application_found));

                    }

                    return false;

                }
            });

            PROJECT_GITHUB.setIcon(InfoActivity.mGitHubIcon);
            PROJECT_GITHUB.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Uri mUri = Uri.parse("https://github.com/peppe130/ROMInstaller");

                    try {

                        startActivity(new Intent(Intent.ACTION_VIEW, mUri));

                    } catch (android.content.ActivityNotFoundException anfe) {

                        ToastLong(ACTIVITY, ACTIVITY.getString(R.string.no_application_found));

                    }

                    return false;

                }
            });

            DONATE.setIcon(InfoActivity.mDonateIcon);
            DONATE.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    try {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.peppe130.rominstallerdonation")));

                    } catch (android.content.ActivityNotFoundException anfe) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.peppe130.rominstallerdonation")));

                    }

                    return false;
                }
            });

            MY_APPS.setIcon(InfoActivity.mPlayStoreIcon);
            MY_APPS.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    try {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Giuseppe Montuoro")));

                    } catch (android.content.ActivityNotFoundException anfe) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:Giuseppe Montuoro")));

                    }

                    return false;

                }
            });

        }

    }

}
