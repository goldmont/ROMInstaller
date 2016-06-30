package com.peppe130.rominstaller.core;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.peppe130.rominstaller.ControlCenter;
import com.peppe130.rominstaller.R;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;
import cn.pedant.SweetAlert.SweetAlertDialog;


@SuppressWarnings("unused, ResultOfMethodCallIgnored, ConstantConditions")
public class FlashRecovery extends AsyncTask<String, String, Boolean> {

    SweetAlertDialog mProgress;
    File mDownloadDirectory, mDownloadedFile;
    DownloadManager.Request mRequest;
    String mDownloadedFileFinalName, mMD5, mRecoveryPartition;
    Boolean mHasAddOns;
    BroadcastReceiver onDownloadFinishReceiver;

    public FlashRecovery(DownloadManager.Request request, File downloadDirectory, String downloadedFileFinalName, String recoveryPartition, Boolean hasAddOns) {
        this.mRequest = request;
        this.mDownloadDirectory = downloadDirectory;
        this.mDownloadedFileFinalName = downloadedFileFinalName;
        this.mRecoveryPartition = recoveryPartition;
        this.mHasAddOns = hasAddOns;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Utils.SHOULD_LOCK_UI = true;
        Utils.ACTIVITY.invalidateOptionsMenu();

        mDownloadedFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + mDownloadDirectory + "/" + mDownloadedFileFinalName);

        if (mDownloadedFile.exists()) {
            mDownloadedFile.delete();
        }

        Utils.ACTIVITY.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Utils.ACTIVITY.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        String mContent = (String.format(Utils.ACTIVITY.getString(R.string.download_recovery_progress_dialog_message), mDownloadedFileFinalName));
        mProgress = new SweetAlertDialog(Utils.ACTIVITY, SweetAlertDialog.PROGRESS_TYPE);
        mProgress.setTitleText(Utils.ACTIVITY.getString(R.string.download_recovery_progress_dialog_title));
        mProgress.setContentText(mContent);
        mProgress.getProgressHelper().setBarColor(ContextCompat.getColor(Utils.ACTIVITY, ControlCenter.AccentColorChooser()));
        mProgress.setCancelable(false);
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
                DownloadManager mDownloadManager = (DownloadManager) Utils.ACTIVITY.getSystemService(Context.DOWNLOAD_SERVICE);
                mDownloadManager.enqueue(mRequest);
            }
        }, 500);
    }

    @Override
    protected Boolean doInBackground(String... mRoot) {
        return RootTools.isAccessGiven();
    }

    @Override
    protected void onPostExecute(final Boolean result) {
        super.onPostExecute(result);
        Utils.ACTIVITY.registerReceiver(onDownloadFinishReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, android.content.Intent i) {
                mProgress.dismiss();
                Utils.ACTIVITY.unregisterReceiver(onDownloadFinishReceiver);
                Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                if (result) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgress.setTitleText(Utils.ACTIVITY.getString(R.string.check_md5_recovery_progress_dialog_title));
                            mProgress.setContentText(Utils.ACTIVITY.getString(R.string.check_md5_recovery_progress_dialog_message));
                            mProgress.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        mMD5 = Files.hash(mDownloadedFile, Hashing.md5()).toString();
                                        if (Arrays.asList(ControlCenter.RECOVERY_MD5_LIST).contains(mMD5)) {
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mProgress.dismiss();
                                                    Utils.Sleep(500);
                                                    mProgress.setTitleText(Utils.ACTIVITY.getString(R.string.flash_recovery_progress_dialog_title));
                                                    mProgress.setContentText(Utils.ACTIVITY.getString(R.string.flash_recovery_progress_dialog_message));
                                                    mProgress.show();
                                                    if (!ControlCenter.TRIAL_MODE) {
                                                        try {
                                                            Command flashRecovery = new Command(0, "dd " + "if=" + mDownloadedFile + " of=" + mRecoveryPartition);
                                                            RootTools.getShell(true).add(flashRecovery);
                                                            new Handler().postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    mProgress.dismiss();
                                                                    Utils.ToastLong(Utils.ACTIVITY, Utils.ACTIVITY.getString(R.string.flash_completed));
                                                                    if (mHasAddOns) {
                                                                        cancel(true);
                                                                        Utils.TOAST.cancel();
                                                                        new Download(
                                                                                Utils.DOWNLOAD_REQUEST_LIST.get(0),
                                                                                Utils.DOWNLOAD_DIRECTORY_LIST.get(0),
                                                                                Utils.DOWNLOADED_FILE_NAME_LIST.get(0),
                                                                                Utils.DOWNLOADED_FILE_MD5_LIST.get(0), 1).execute();
                                                                    } else {
                                                                        Utils.SHOULD_LOCK_UI = false;
                                                                        Utils.ACTIVITY.invalidateOptionsMenu();
                                                                        Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                                    }
                                                                }
                                                            }, 2000);
                                                        } catch (TimeoutException | RootDeniedException | IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                mProgress.dismiss();
                                                                Utils.ToastLong(Utils.ACTIVITY, Utils.ACTIVITY.getString(R.string.flash_completed));
                                                                if (mHasAddOns) {
                                                                    cancel(true);
                                                                    Utils.TOAST.cancel();
                                                                    new Download(
                                                                            Utils.DOWNLOAD_REQUEST_LIST.get(0),
                                                                            Utils.DOWNLOAD_DIRECTORY_LIST.get(0),
                                                                            Utils.DOWNLOADED_FILE_NAME_LIST.get(0),
                                                                            Utils.DOWNLOADED_FILE_MD5_LIST.get(0), 1).execute();
                                                                } else {
                                                                    Utils.SHOULD_LOCK_UI = false;
                                                                    Utils.ACTIVITY.invalidateOptionsMenu();
                                                                    Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                                }
                                                            }
                                                        }, 2000);
                                                    }
                                                }
                                            }, 500);
                                        } else {
                                            mProgress.dismiss();
                                            mDownloadedFile.delete();
                                            Utils.Sleep(500);
                                            String mContent = (String.format(Utils.ACTIVITY.getString(R.string.recovery_md5_mismatch_dialog_message), mDownloadedFileFinalName));
                                            final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Utils.ACTIVITY, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText(Utils.ACTIVITY.getString(R.string.recovery_md5_mismatch_dialog_title))
                                                    .setContentText(mContent)
                                                    .setConfirmText(Utils.ACTIVITY.getString(R.string.download_button))
                                                    .setCancelText(Utils.ACTIVITY.getString(R.string.negative_button))
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            cancel(true);
                                                            sDialog.dismiss();
                                                            new FlashRecovery(
                                                                    mRequest,
                                                                    mDownloadDirectory,
                                                                    mDownloadedFileFinalName,
                                                                    mRecoveryPartition, mHasAddOns).execute();
                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            sweetAlertDialog.dismiss();
                                                            new Handler().postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Utils.SHOULD_LOCK_UI = false;
                                                                    Utils.ACTIVITY.invalidateOptionsMenu();
                                                                    Utils.ToastLong(Utils.ACTIVITY, Utils.ACTIVITY.getString(R.string.recovery_needed));
                                                                }
                                                            }, 300);
                                                        }
                                                    });
                                            sweetAlertDialog.setCancelable(false);
                                            sweetAlertDialog.show();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 500);
                        }
                    }, 500);
                } else {
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
                }
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}