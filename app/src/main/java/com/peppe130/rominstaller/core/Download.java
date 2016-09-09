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
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.view.WindowManager;
import java.io.File;
import java.io.IOException;

import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.ControlCenter;
import com.peppe130.bouncingdialogs.BouncingDialog;
import com.google.common.io.Files;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashCode;


public class Download extends AsyncTask<String, String, String> {

    private Long mDownloadID;
    private HashCode mHashCode;
    private Boolean mStartCheckFile;
    private BouncingDialog mProgress;
    private Integer mNextDownloadIndex;
    private DownloadManager mDownloadManager;
    private DownloadManager.Request mRequest;
    private File mDownloadDirectory, mDownloadedFile;
    private BroadcastReceiver onDownloadFinishReceiver;
    private String mDownloadedFileFinalName, mDownloadedFileMD5;

    public Download(DownloadManager.Request request, File downloadDirectory, String downloadedFileFinalName, Boolean isROM) {

        this.mRequest = request;

        this.mDownloadDirectory = downloadDirectory;

        this.mDownloadedFileFinalName = downloadedFileFinalName;

        this.mStartCheckFile = isROM;

    }

    public Download(DownloadManager.Request request, File downloadDirectory, String downloadedFileFinalName, String downloadedFileMD5) {

        this.mRequest = request;

        this.mDownloadDirectory = downloadDirectory;

        this.mDownloadedFileFinalName = downloadedFileFinalName;

        this.mDownloadedFileMD5 = downloadedFileMD5;

    }

    public Download(DownloadManager.Request request, File downloadDirectory, String downloadedFileFinalName, String downloadedFileMD5, Integer nextDownloadIndex) {

        this.mRequest = request;

        this.mDownloadDirectory = downloadDirectory;

        this.mDownloadedFileFinalName = downloadedFileFinalName;

        this.mDownloadedFileMD5 = downloadedFileMD5;

        this.mNextDownloadIndex = nextDownloadIndex;

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

        mDownloadManager = (DownloadManager) Utils.ACTIVITY.getSystemService(Context.DOWNLOAD_SERVICE);

        String mContent = (String.format(Utils.ACTIVITY.getString(R.string.download_progress_dialog_message), mDownloadedFileFinalName));

        Utils.ACTIVITY.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Utils.ACTIVITY.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mProgress = new BouncingDialog(Utils.ACTIVITY, BouncingDialog.DOWNLOAD_TYPE);
        mProgress.title(Utils.ACTIVITY.getString(R.string.download_progress_dialog_title));
        mProgress.content(mContent);
        mProgress.positiveText(Utils.ACTIVITY.getString(R.string.cancel_button));
        mProgress.onPositive(new BouncingDialog.SingleButtonCallback() {
            @Override
            public void onClick(BouncingDialog bouncingDialog) {

                cancel(true);

                mDownloadManager.remove(mDownloadID);

                Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                if (mStartCheckFile != null && mStartCheckFile) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Utils.FileChooser();

                        }
                    }, 300);

                } else {

                    Utils.SHOULD_LOCK_UI = false;

                    Utils.ACTIVITY.invalidateOptionsMenu();

                    ClearArrays();

                }

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
//
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
    protected String doInBackground(String... mUrl) {

        try {

            mDownloadID = mDownloadManager.enqueue(mRequest);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onPostExecute(String result) {

        super.onPostExecute(result);

        Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Utils.ACTIVITY.registerReceiver(onDownloadFinishReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i) {

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

                            if (mDownloadedFileMD5 != null && mDownloadedFileMD5.trim().length() != 0) {

                                try {

                                    String mContent = (String.format(Utils.ACTIVITY.getString(R.string.downloaded_file_md5_check), mDownloadedFileFinalName));

                                    Utils.ToastShort(Utils.ACTIVITY, mContent);

                                    mHashCode = Files.hash(mDownloadedFile, Hashing.md5());

                                    if (mHashCode.toString().equalsIgnoreCase(mDownloadedFileMD5)) {

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                Utils.TOAST.cancel();

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        mProgress.dismiss();

                                                    }
                                                }, 300);

                                            }
                                        }, 500);

                                        if (mNextDownloadIndex != null && mNextDownloadIndex < Utils.DOWNLOAD_REQUEST_LIST.size()) {

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                @SuppressWarnings("LoopStatementThatDoesntLoop")
                                                public void run() {

                                                    cancel(true);

                                                    for (Integer mInt = mNextDownloadIndex; mInt < Utils.DOWNLOAD_REQUEST_LIST.size(); mInt++) {

                                                        new Download(
                                                                Utils.DOWNLOAD_REQUEST_LIST.get(mInt),
                                                                Utils.DOWNLOAD_DIRECTORY_LIST.get(mInt),
                                                                Utils.DOWNLOADED_FILE_NAME_LIST.get(mInt),
                                                                Utils.DOWNLOADED_FILE_MD5_LIST.get(mInt), ++mInt).execute();

                                                        break;

                                                    }

                                                }
                                            }, 500);

                                        } else {

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    cancel(true);

                                                    Utils.SHOULD_LOCK_UI = false;

                                                    Utils.ACTIVITY.invalidateOptionsMenu();

                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            Utils.ToastLong(Utils.ACTIVITY, Utils.ACTIVITY.getString(R.string.download_completed));

                                                            Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                        }
                                                    }, 300);

                                                    ClearArrays();

                                                }
                                            }, 1000);

                                        }

                                    } else {

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                Utils.TOAST.cancel();

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        mProgress.dismiss();

                                                    }
                                                }, 300);

                                            }
                                        }, 500);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            @SuppressWarnings("ResultOfMethodCallIgnored")
                                            public void run() {

                                                mDownloadedFile.delete();

                                                Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                String mContent = (String.format(Utils.ACTIVITY.getString(R.string.downloaded_file_md5_mismatch_dialog_message), mDownloadedFileFinalName));

                                                BouncingDialog bouncingDialog = new BouncingDialog(Utils.ACTIVITY, BouncingDialog.ERROR_TYPE)
                                                        .title(Utils.ACTIVITY.getString(R.string.downloaded_file_md5_mismatch_dialog_title))
                                                        .content(mContent)
                                                        .positiveText(Utils.ACTIVITY.getString(R.string.download_button))
                                                        .negativeText(Utils.ACTIVITY.getString(R.string.negative_button))
                                                        .onPositive(new BouncingDialog.SingleButtonCallback() {
                                                            @Override
                                                            public void onClick(BouncingDialog bouncingDialog1) {

                                                                cancel(true);

                                                                bouncingDialog1.dismiss();

                                                                new Download(mRequest, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5, mNextDownloadIndex).execute();

                                                            }
                                                        })
                                                        .onNegative(new BouncingDialog.SingleButtonCallback() {
                                                            @Override
                                                            @SuppressWarnings("LoopStatementThatDoesntLoop")
                                                            public void onClick(final BouncingDialog bouncingDialog1) {

                                                                if (mNextDownloadIndex != null && mNextDownloadIndex < Utils.DOWNLOAD_REQUEST_LIST.size()) {

                                                                    new Handler().postDelayed(new Runnable() {
                                                                        @Override
                                                                        public void run() {

                                                                            cancel(true);

                                                                            bouncingDialog1.dismiss();

                                                                            for (Integer mInt = mNextDownloadIndex; mInt < Utils.DOWNLOAD_REQUEST_LIST.size(); mInt++) {

                                                                                new Download(
                                                                                        Utils.DOWNLOAD_REQUEST_LIST.get(mInt),
                                                                                        Utils.DOWNLOAD_DIRECTORY_LIST.get(mInt),
                                                                                        Utils.DOWNLOADED_FILE_NAME_LIST.get(mInt),
                                                                                        Utils.DOWNLOADED_FILE_MD5_LIST.get(mInt), ++mInt).execute();

                                                                                break;

                                                                            }

                                                                        }
                                                                    }, 500);

                                                                } else {

                                                                    cancel(true);

                                                                    bouncingDialog1.dismiss();

                                                                    Utils.SHOULD_LOCK_UI = false;

                                                                    Utils.ACTIVITY.invalidateOptionsMenu();

                                                                }

                                                            }
                                                        });
                                                bouncingDialog.setCancelable(false);
                                                bouncingDialog.show();
                                            }
                                        }, 1000);

                                    }

                                } catch (IOException e) {

                                    e.printStackTrace();

                                }

                            } else {

                                mProgress.dismiss();

                                Boolean mBoolean = mNextDownloadIndex != null && mNextDownloadIndex < Utils.DOWNLOAD_REQUEST_LIST.size();

                                Boolean mBoolean2 = mNextDownloadIndex != null && mNextDownloadIndex < Utils.DOWNLOADED_FILE_MD5_LIST.size() && Utils.DOWNLOADED_FILE_MD5_LIST.get(mNextDownloadIndex) != null && Utils.DOWNLOADED_FILE_MD5_LIST.get(mNextDownloadIndex).trim().length() != 0;

                                if (mBoolean && mBoolean2) {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        @SuppressWarnings("LoopStatementThatDoesntLoop")
                                        public void run() {

                                            cancel(true);

                                            for (Integer mInt = mNextDownloadIndex; mInt < Utils.DOWNLOAD_REQUEST_LIST.size(); mInt++) {

                                                new Download(
                                                        Utils.DOWNLOAD_REQUEST_LIST.get(mInt),
                                                        Utils.DOWNLOAD_DIRECTORY_LIST.get(mInt),
                                                        Utils.DOWNLOADED_FILE_NAME_LIST.get(mInt),
                                                        Utils.DOWNLOADED_FILE_MD5_LIST.get(mInt), ++mInt).execute();

                                                break;

                                            }
                                        }
                                    }, 500);

                                } else if (!mBoolean && mBoolean2) {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        @SuppressWarnings("LoopStatementThatDoesntLoop")
                                        public void run() {

                                            cancel(true);

                                            for (Integer mInt = mNextDownloadIndex; mInt < Utils.DOWNLOAD_REQUEST_LIST.size(); mInt++) {

                                                new Download(
                                                        Utils.DOWNLOAD_REQUEST_LIST.get(mInt),
                                                        Utils.DOWNLOAD_DIRECTORY_LIST.get(mInt),
                                                        Utils.DOWNLOADED_FILE_NAME_LIST.get(mInt),
                                                        Utils.DOWNLOADED_FILE_MD5_LIST.get(mInt)).execute();

                                                break;

                                            }

                                        }
                                    }, 500);

                                } else if (mBoolean && !mBoolean2) {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        @SuppressWarnings("LoopStatementThatDoesntLoop")
                                        public void run() {

                                            cancel(true);

                                            for (Integer mInt = mNextDownloadIndex; mInt < Utils.DOWNLOAD_REQUEST_LIST.size(); mInt++) {

                                                new Download(
                                                        Utils.DOWNLOAD_REQUEST_LIST.get(mInt),
                                                        Utils.DOWNLOAD_DIRECTORY_LIST.get(mInt),
                                                        Utils.DOWNLOADED_FILE_NAME_LIST.get(mInt), null, ++mInt).execute();

                                                break;

                                            }

                                        }
                                    }, 500);

                                } else {

                                    if (mStartCheckFile != null && mStartCheckFile) {

                                        cancel(true);

                                        if (!ControlCenter.TEST_MODE) {

                                            Utils.SetZipFile(mDownloadedFile);

                                            new CheckFile().execute();

                                        } else {

                                            Utils.SHOULD_LOCK_UI = false;

                                            Utils.ACTIVITY.invalidateOptionsMenu();

                                            Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                            Utils.ToastLong(Utils.ACTIVITY, Utils.ACTIVITY.getString(R.string.disable_test_mode_checkfile));

                                        }

                                        ClearArrays();

                                    } else {

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                cancel(true);

                                                Utils.SHOULD_LOCK_UI = false;

                                                Utils.ACTIVITY.invalidateOptionsMenu();

                                                Utils.ToastLong(Utils.ACTIVITY, Utils.ACTIVITY.getString(R.string.download_completed));

                                                Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                ClearArrays();

                                            }
                                        }, 500);

                                    }

                                }

                            }
                            break;

                    }

                }

            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    private void ClearArrays() {

        Utils.DOWNLOAD_REQUEST_LIST.clear();

        Utils.DOWNLOAD_LINK_LIST.clear();

        Utils.DOWNLOAD_DIRECTORY_LIST.clear();

        Utils.DOWNLOADED_FILE_NAME_LIST.clear();

        Utils.DOWNLOADED_FILE_MD5_LIST.clear();

    }

}
