package com.peppe130.rominstaller.core;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.WindowManager;
import java.io.File;
import java.io.IOException;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.peppe130.rominstaller.ControlCenter;
import com.peppe130.rominstaller.R;
import cn.pedant.SweetAlert.SweetAlertDialog;


@SuppressWarnings("ResultOfMethodCallIgnored, unused, LoopStatementThatDoesntLoop, ConstantConditions")
public class Download extends AsyncTask<String, String, String> {

    SweetAlertDialog mProgress;
    HashCode mHashCode;
    Uri mDownloadLink;
    File mDownloadDirectory, mDownloadedFile;
    String mDownloadedFileFinalName, mDownloadedFileMD5;
    Boolean mStartCheckFile;
    Integer mNextDownloadIndex = null;
    DownloadManager.Request mRequest;
    BroadcastReceiver onDownloadFinishReceiver;

    public Download(DownloadManager.Request request, File downloadDirectory, String downloadedFileFinalName) {
        this.mRequest = request;
        this.mDownloadDirectory = downloadDirectory;
        this.mDownloadedFileFinalName = downloadedFileFinalName;
    }

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

    public Download(DownloadManager.Request request, File downloadDirectory, String downloadedFileFinalName, Integer nextDownloadIndex) {
        this.mRequest = request;
        this.mDownloadDirectory = downloadDirectory;
        this.mDownloadedFileFinalName = downloadedFileFinalName;
        this.mNextDownloadIndex = nextDownloadIndex;
    }

    public Download(DownloadManager.Request request, File downloadDirectory, String downloadedFileFinalName, String downloadedFileMD5, Integer nextDownloadIndex) {
        this.mRequest = request;
        this.mDownloadDirectory = downloadDirectory;
        this.mDownloadedFileFinalName = downloadedFileFinalName;
        this.mDownloadedFileMD5 = downloadedFileMD5;
        this.mNextDownloadIndex = nextDownloadIndex;
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

        String mContent = (String.format(Utils.ACTIVITY.getString(R.string.download_progress_dialog_message), mDownloadedFileFinalName));
        Utils.ACTIVITY.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Utils.ACTIVITY.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mProgress = new SweetAlertDialog(Utils.ACTIVITY, SweetAlertDialog.PROGRESS_TYPE);
        mProgress.setTitleText(Utils.ACTIVITY.getString(R.string.download_progress_dialog_title));
        mProgress.setContentText(mContent);
        mProgress.getProgressHelper().setBarColor(Utils.FetchAccentColor());
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
            }
        }, 500);
    }

    @Override
    protected String doInBackground(String... mUrl) {
        try {
            DownloadManager mDownloadManager = (DownloadManager) Utils.ACTIVITY.getSystemService(Context.DOWNLOAD_SERVICE);
            mDownloadManager.enqueue(mRequest);
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Utils.ACTIVITY.registerReceiver(onDownloadFinishReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i) {
                Utils.ACTIVITY.unregisterReceiver(onDownloadFinishReceiver);
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
                                        Utils.DOWNLOAD_REQUEST_LIST.clear();
                                        Utils.DOWNLOAD_LINK_LIST.clear();
                                        Utils.DOWNLOAD_DIRECTORY_LIST.clear();
                                        Utils.DOWNLOADED_FILE_NAME_LIST.clear();
                                        Utils.DOWNLOADED_FILE_MD5_LIST.clear();
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
                                public void run() {
                                    mDownloadedFile.delete();
                                    Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    String mContent = (String.format(Utils.ACTIVITY.getString(R.string.downloaded_file_md5_mismatch_dialog_message), mDownloadedFileFinalName));
                                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Utils.ACTIVITY, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText(Utils.ACTIVITY.getString(R.string.downloaded_file_md5_mismatch_dialog_title))
                                            .setContentText(mContent)
                                            .setConfirmText(Utils.ACTIVITY.getString(R.string.download_button))
                                            .setCancelText(Utils.ACTIVITY.getString(R.string.negative_button))
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    cancel(true);
                                                    sweetAlertDialog.dismiss();
                                                    if (mDownloadedFileMD5 != null && mDownloadedFileMD5.trim().length() != 0) {
                                                        new Download(mRequest, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5, mNextDownloadIndex).execute();
                                                    } else {
                                                        new Download(mRequest, mDownloadDirectory, mDownloadedFileFinalName, mNextDownloadIndex).execute();
                                                    }
                                                }
                                            })
                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                                    if (mNextDownloadIndex != null && mNextDownloadIndex < Utils.DOWNLOAD_REQUEST_LIST.size()) {
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                cancel(true);
                                                                sweetAlertDialog.dismiss();
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
                                                        sweetAlertDialog.dismiss();
                                                        Utils.SHOULD_LOCK_UI = false;
                                                        Utils.ACTIVITY.invalidateOptionsMenu();
                                                    }
                                                }
                                            });
                                    sweetAlertDialog.setCancelable(false);
                                    sweetAlertDialog.show();
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
                            public void run() {
                                cancel(true);
                                for (Integer mInt = mNextDownloadIndex; mInt < Utils.DOWNLOAD_REQUEST_LIST.size(); mInt++) {
                                    new Download(
                                            Utils.DOWNLOAD_REQUEST_LIST.get(mInt),
                                            Utils.DOWNLOAD_DIRECTORY_LIST.get(mInt),
                                            Utils.DOWNLOADED_FILE_NAME_LIST.get(mInt), ++mInt).execute();
                                    break;
                                }
                            }
                        }, 500);
                    } else {
                        if (mStartCheckFile != null && mStartCheckFile) {
                            cancel(true);
                            if (!ControlCenter.TEST_MODE) {
                                Utils.ZIP_FILE = mDownloadedFile;
                                new CheckFile().execute();
                            } else {
                                Utils.SHOULD_LOCK_UI = false;
                                Utils.ACTIVITY.invalidateOptionsMenu();
                                Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Utils.ToastLong(Utils.ACTIVITY, Utils.ACTIVITY.getString(R.string.can_not_check_file));
                            }
                            Utils.DOWNLOAD_REQUEST_LIST.clear();
                            Utils.DOWNLOAD_LINK_LIST.clear();
                            Utils.DOWNLOAD_DIRECTORY_LIST.clear();
                            Utils.DOWNLOADED_FILE_NAME_LIST.clear();
                            Utils.DOWNLOADED_FILE_MD5_LIST.clear();
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    cancel(true);
                                    Utils.SHOULD_LOCK_UI = false;
                                    Utils.ACTIVITY.invalidateOptionsMenu();
                                    Utils.ToastLong(Utils.ACTIVITY, Utils.ACTIVITY.getString(R.string.download_completed));
                                    Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    Utils.DOWNLOAD_REQUEST_LIST.clear();
                                    Utils.DOWNLOAD_LINK_LIST.clear();
                                    Utils.DOWNLOAD_DIRECTORY_LIST.clear();
                                    Utils.DOWNLOADED_FILE_NAME_LIST.clear();
                                    Utils.DOWNLOADED_FILE_MD5_LIST.clear();
                                }
                            }, 500);
                        }
                    }
                }
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}