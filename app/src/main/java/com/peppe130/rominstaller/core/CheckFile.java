package com.peppe130.rominstaller.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import java.io.IOException;
import java.util.Arrays;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.Utils;
import com.peppe130.rominstaller.activities.MainActivity;
import com.stericson.RootTools.RootTools;
import cn.pedant.SweetAlert.SweetAlertDialog;


@SuppressWarnings("ResultOfMethodCallIgnored")
@SuppressLint("CommitPrefEdits")
public class CheckFile extends AsyncTask<String, String, Boolean> {

    Boolean isDeviceCompatible;
    Vibrator mVibrator;
    String mMD5;

    SweetAlertDialog mProgress = new SweetAlertDialog(Utils.ACTIVITY, SweetAlertDialog.PROGRESS_TYPE);

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Utils.SHOULD_LOCK_UI = true;
        Utils.ACTIVITY.invalidateOptionsMenu();

        Utils.ACTIVITY.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Utils.ACTIVITY.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mVibrator = (Vibrator) Utils.ACTIVITY.getSystemService(Context.VIBRATOR_SERVICE);
        isDeviceCompatible = Arrays.asList(Utils.DEVICE_COMPATIBILITY_LIST).contains(Utils.MODEL);

        mProgress.setTitleText(Utils.ACTIVITY.getString(R.string.progress_dialog_title));
        mProgress.setContentText(Utils.ACTIVITY.getString(R.string.check_configuration));
        mProgress.getProgressHelper().setBarColor(ContextCompat.getColor(Utils.ACTIVITY, Utils.PROGRESS_BAR_COLOR));
        mProgress.setCancelable(false);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancel(true);
                Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgress.show();
            }
        }, 500);

    }

    @Override
    protected Boolean doInBackground(String... mRom) {

        String mModel = (String.format(Utils.ACTIVITY.getString(R.string.device_model), Utils.MODEL));
        StringBuilder sbUpdate = new StringBuilder();
        updateResult((long) 1300, sbUpdate.append(Utils.ACTIVITY.getString(R.string.check_model)).toString());
        updateResult((long) 1200, sbUpdate.append(mModel).toString());

        if (Utils.TRIAL_MODE) {
            isDeviceCompatible = true;
        }

        if (isDeviceCompatible) {

            publishProgress(sbUpdate.append(Utils.ACTIVITY.getString(R.string.calculating_md5)).toString());

            try {
                mMD5 = Files.hash(Utils.ZIP_FILE, Hashing.md5()).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if ((Utils.ZIP_FILE.exists()) && Arrays.asList(Utils.ROM_MD5_LIST).contains(mMD5)) {
                updateResult((long) 5000, sbUpdate.append(Utils.ACTIVITY.getString(R.string.initializing_start)).toString());
                return true;
            }

        }

        return false;
    }

    private void updateResult(Long sleep, String nextLine) {

        publishProgress(nextLine);

        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onProgressUpdate(String... update) {
        super.onProgressUpdate(update);

        mProgress.setContentText(update[0]);

    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        mProgress.dismiss();

        Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (!isDeviceCompatible) {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Utils.ACTIVITY, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(Utils.ACTIVITY.getString(R.string.incompatible_device_dialog_title))
                    .setContentText(Utils.ACTIVITY.getString(R.string.incompatible_device_dialog_message))
                    .setConfirmText(Utils.ACTIVITY.getString(R.string.close_button))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Utils.ACTIVITY.finishAffinity();
                        }
                    });
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
        } else if ((Utils.ZIP_FILE.exists()) && result) {
            mVibrator.vibrate(1500);
            if (!RootTools.isAccessGiven()) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Utils.ACTIVITY, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(Utils.ACTIVITY.getString(R.string.no_root_access_dialog_title))
                        .setContentText(Utils.ACTIVITY.getString(R.string.no_root_access_dialog_message))
                        .setConfirmText(Utils.ACTIVITY.getString(R.string.close_button))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Utils.ACTIVITY.finishAffinity();
                            }
                        });
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utils.SHOULD_LOCK_UI = false;
                        Utils.ACTIVITY.invalidateOptionsMenu();
                        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(Utils.ACTIVITY);
                        SharedPreferences.Editor mEditor = SP.edit();
                        mEditor.putString("file_path", Utils.ZIP_FILE.getAbsolutePath()).commit();
                        MainActivity.mViewPager.setAdapter(MainActivity.mFragmentPagerAdapter);
                        MainActivity.mSmartTabLayout.setViewPager(MainActivity.mViewPager);
                        if (MainActivity.mFragmentPagerAdapter.getCount() == 1) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (Utils.BUTTON_UI) {
                                        MainActivity.DONE.setVisibility(View.VISIBLE);
                                    } else {
                                        Utils.ToastShort(Utils.ACTIVITY, Utils.ACTIVITY.getString(R.string.swipe_left_or_right_to_install));
                                    }
                                }
                            }, 300);
                        } else {
                            if (Utils.BUTTON_UI) {
                                MainActivity.BACK.setVisibility(View.GONE);
                                MainActivity.DONE.setVisibility(View.GONE);
                                MainActivity.NEXT.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }, 200);
            }
        } else if ((Utils.ZIP_FILE.exists()) && !result) {
            String mContent = (String.format(Utils.ACTIVITY.getString(R.string.zip_file_md5_mismatch_dialog_message), Utils.FILE_NAME));
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Utils.ACTIVITY, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(Utils.ACTIVITY.getString(R.string.zip_file_md5_mismatch_dialog_title))
                    .setContentText(mContent)
                    .setConfirmText(Utils.ACTIVITY.getString(R.string.download_button))
                    .setCancelText(Utils.ACTIVITY.getString(R.string.file_chooser_button))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            cancel(true);
                            sweetAlertDialog.dismiss();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.ZIP_FILE.delete();
                                    Utils.ROMDownloadLinkGenerator();
                                }
                            }, 300);
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.FileChooser();
                                }
                            }, 300);
                            sDialog.dismissWithAnimation();
                        }
                    });
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
        }
    }
}