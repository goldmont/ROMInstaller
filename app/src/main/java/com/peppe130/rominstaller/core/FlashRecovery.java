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

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.view.WindowManager;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.ControlCenter;
import com.peppe130.bouncingdialogs.BouncingDialog;
import com.google.common.io.Files;
import com.google.common.hash.Hashing;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;


class FlashRecovery extends AsyncTask<String, String, Boolean> {

    private Long mDownloadID;
    private Boolean mHasAddOns;
    private BouncingDialog mProgress;
    private DownloadManager mDownloadManager;
    private DownloadManager.Request mRequest;
    private File mDownloadDirectory, mDownloadedFile;
    private BroadcastReceiver onDownloadFinishReceiver;
    private String mDownloadedFileFinalName, mMD5, mRecoveryPartition;

    FlashRecovery(DownloadManager.Request request, File downloadDirectory, String downloadedFileFinalName, String recoveryPartition, Boolean hasAddOns) {

        this.mRequest = request;

        this.mDownloadDirectory = downloadDirectory;

        this.mDownloadedFileFinalName = downloadedFileFinalName;

        this.mRecoveryPartition = recoveryPartition;

        this.mHasAddOns = hasAddOns;

    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
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

        mProgress = new BouncingDialog(Utils.ACTIVITY, BouncingDialog.DOWNLOAD_TYPE);
        mProgress.title(Utils.ACTIVITY.getString(R.string.download_recovery_progress_dialog_title));
        mProgress.content(mContent);
        mProgress.positiveText(Utils.ACTIVITY.getString(R.string.cancel_button));
        mProgress.onPositive(new BouncingDialog.SingleButtonCallback() {
            @Override
            public void onClick(BouncingDialog bouncingDialog) {

                cancel(true);

                mDownloadManager.remove(mDownloadID);

                Utils.SHOULD_LOCK_UI = false;

                Utils.ACTIVITY.invalidateOptionsMenu();

                Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                Utils.ToastLong(Utils.ACTIVITY, Utils.ACTIVITY.getString(R.string.download_canceled));

            }
        });
        mProgress.autoDismiss(true);
        mProgress.titleColorAttr(R.attr.bd_progress_title_color);
        mProgress.setCancelable(false);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                cancel(true);

                Utils.SHOULD_LOCK_UI = false;

                Utils.ACTIVITY.invalidateOptionsMenu();

                Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mProgress.show();

                mDownloadManager = (DownloadManager) Utils.ACTIVITY.getSystemService(Context.DOWNLOAD_SERVICE);

                mDownloadID = mDownloadManager.enqueue(mRequest);

            }
        }, 500);

    }

    @Override
    protected Boolean doInBackground(String... mRoot) {

        return RootTools.isAccessGiven();

    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onPostExecute(final Boolean result) {

        super.onPostExecute(result);

        Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Utils.ACTIVITY.registerReceiver(onDownloadFinishReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, android.content.Intent i) {

                mProgress.dismiss();

                Utils.ACTIVITY.unregisterReceiver(onDownloadFinishReceiver);

                Long mDownloadID = i.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                DownloadManager.Query mQuery = new DownloadManager.Query();

                mQuery.setFilterById(mDownloadID);

                Cursor mCursor = mDownloadManager.query(mQuery);

                mCursor.moveToFirst();

                Integer mColumnIndex = mCursor.getColumnIndex(DownloadManager.COLUMN_STATUS);

                Integer mStatus;

                if (mCursor != null && mCursor.moveToFirst() && mColumnIndex != null) {

                    mStatus = mCursor.getInt(mColumnIndex);

                    switch (mStatus) {

                        case DownloadManager.STATUS_SUCCESSFUL:

                            if (result) {

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        mProgress.title(Utils.ACTIVITY.getString(R.string.check_md5_recovery_progress_dialog_title));
                                        mProgress.content(Utils.ACTIVITY.getString(R.string.check_md5_recovery_progress_dialog_message));
                                        mProgress.changeDialogType(BouncingDialog.PROGRESS_TYPE);
                                        mProgress.show();

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            @SuppressWarnings("ResultOfMethodCallIgnored")
                                            public void run() {

                                                try {

                                                    mMD5 = Files.hash(mDownloadedFile, Hashing.md5()).toString();

                                                    if (Arrays.asList(ControlCenter.RECOVERY_MD5_LIST).contains(mMD5)) {

                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                mProgress.dismiss();

                                                                Utils.Sleep(500);

                                                                mProgress.title(Utils.ACTIVITY.getString(R.string.flash_recovery_progress_dialog_title));
                                                                mProgress.content(Utils.ACTIVITY.getString(R.string.flash_recovery_progress_dialog_message));
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

                                                        BouncingDialog bouncingDialog = new BouncingDialog(Utils.ACTIVITY, BouncingDialog.ERROR_TYPE)
                                                                .title(Utils.ACTIVITY.getString(R.string.recovery_md5_mismatch_dialog_title))
                                                                .content(mContent)
                                                                .positiveText(Utils.ACTIVITY.getString(R.string.download_button))
                                                                .negativeText(Utils.ACTIVITY.getString(R.string.negative_button))
                                                                .onPositive(new BouncingDialog.SingleButtonCallback() {
                                                                    @Override
                                                                    public void onClick(BouncingDialog bouncingDialog1) {

                                                                        cancel(true);

                                                                        bouncingDialog1.dismiss();

                                                                        new FlashRecovery(
                                                                                mRequest,
                                                                                mDownloadDirectory,
                                                                                mDownloadedFileFinalName,
                                                                                mRecoveryPartition, mHasAddOns).execute();

                                                                    }
                                                                })
                                                                .onNegative(new BouncingDialog.SingleButtonCallback() {
                                                                    @Override
                                                                    public void onClick(BouncingDialog bouncingDialog1) {

                                                                        bouncingDialog1.dismiss();

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
                                                        bouncingDialog.setCancelable(false);
                                                        bouncingDialog.show();
                                                    }

                                                } catch (IOException e) {

                                                    e.printStackTrace();

                                                }

                                            }
                                        }, 500);

                                    }
                                }, 500);

                            } else {

                                BouncingDialog bouncingDialog = new BouncingDialog(Utils.ACTIVITY, BouncingDialog.ERROR_TYPE)
                                        .title(Utils.ACTIVITY.getString(R.string.no_root_access_dialog_title))
                                        .content(Utils.ACTIVITY.getString(R.string.no_root_access_dialog_message))
                                        .positiveText(Utils.ACTIVITY.getString(R.string.close_button))
                                        .onPositive(new BouncingDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(BouncingDialog bouncingDialog1) {

                                                Utils.ACTIVITY.finishAffinity();

                                            }
                                        });
                                bouncingDialog.setCancelable(false);
                                bouncingDialog.show();

                            }

                            break;

                    }

                }

            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

}