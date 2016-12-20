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

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.view.WindowManager;
import java.io.IOException;
import java.util.Arrays;

import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.ControlCenter;
import com.peppe130.bouncingdialogs.BouncingDialog;
import com.peppe130.rominstaller.activities.MainActivity;
import com.google.common.io.Files;
import com.google.common.hash.Hashing;
import com.stericson.RootTools.RootTools;


public class CheckFile extends AsyncTask<String, String, Boolean> {

    private String mMD5;
    private Vibrator mVibrator;
    private BouncingDialog mProgress;

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        Utils.SHOULD_LOCK_UI = true;

        Utils.ACTIVITY.invalidateOptionsMenu();

        Utils.ACTIVITY.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Utils.ACTIVITY.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mVibrator = (Vibrator) Utils.ACTIVITY.getSystemService(Context.VIBRATOR_SERVICE);

        mProgress = new BouncingDialog(Utils.ACTIVITY, BouncingDialog.PROGRESS_TYPE);
        mProgress.title(Utils.ACTIVITY.getString(R.string.progress_dialog_title));
        mProgress.content(Utils.ACTIVITY.getString(R.string.check_configuration));
        mProgress.titleColorAttr(R.attr.bd_progress_title_color);
        mProgress.setCancelable(false);
        mProgress.setCanceledOnTouchOutside(false);
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

            }
        }, 500);

    }

    @Override
    protected Boolean doInBackground(String... mCheck) {

        String mModel = (String.format(Utils.ACTIVITY.getString(R.string.device_model), Utils.GetDeviceModel()));

        StringBuilder sbUpdate = new StringBuilder();

        updateResult((long) 1200, sbUpdate.append(mModel).toString());

        if (ControlCenter.TRIAL_MODE) {

            updateResult((long) 2500, sbUpdate.append(Utils.ACTIVITY.getString(R.string.calculating_md5)).append(" (Fake)").toString());

            updateResult((long) 5000, sbUpdate.append(Utils.ACTIVITY.getString(R.string.initialized)).toString());

            return true;

        } else {

            publishProgress(sbUpdate.append(Utils.ACTIVITY.getString(R.string.calculating_md5)).toString());

            try {

                mMD5 = Files.hash(Utils.GetZipFile(), Hashing.md5()).toString();

            } catch (IOException e) {

                e.printStackTrace();

            }

            try {

                Thread.sleep(2000);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            if ((Utils.GetZipFile().exists()) && (Arrays.asList(ControlCenter.ROM_MD5_LIST).contains(mMD5.toUpperCase()) || Arrays.asList(ControlCenter.ROM_MD5_LIST).contains(mMD5.toLowerCase()))) {

                updateResult((long) 5000, sbUpdate.append(Utils.ACTIVITY.getString(R.string.initialized)).toString());

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

        mProgress.content(update[0]);

    }

    @Override
    protected void onPostExecute(Boolean result) {

        super.onPostExecute(result);

        mProgress.dismiss();

        Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Utils.ACTIVITY.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if ((Utils.GetZipFile().exists()) && result) {

            mVibrator.vibrate(1500);

            if (RootTools.isAccessGiven() || Utils.GRANT_INITIAL_ROOT_ACCESS) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Utils.SHOULD_LOCK_UI = false;

                        Utils.ACTIVITY.invalidateOptionsMenu();

                        MainActivity.InitialSetup();

                    }
                }, 200);

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

        } else if ((Utils.GetZipFile().exists()) && !result) {

            String mContent = (String.format(Utils.ACTIVITY.getString(R.string.zip_file_md5_mismatch_dialog_message), Utils.GetZipFile().getName()));

            BouncingDialog bouncingDialog = new BouncingDialog(Utils.ACTIVITY, BouncingDialog.ERROR_TYPE)
                    .title(Utils.ACTIVITY.getString(R.string.zip_file_md5_mismatch_dialog_title))
                    .content(mContent)
                    .positiveText(Utils.ACTIVITY.getString(R.string.rom_download_button))
                    .negativeText(Utils.ACTIVITY.getString(R.string.retry_button))
                    .onPositive(new BouncingDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(BouncingDialog bouncingDialog1) {

                            cancel(true);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    ControlCenter.DownloadROM();

                                }
                            }, 300);

                        }
                    })
                    .onNegative(new BouncingDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(BouncingDialog bouncingDialog1) {

                            cancel(true);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Utils.FileChooser();

                                }
                            }, 300);

                        }
                    })
                    .autoDismiss(true);
            bouncingDialog.setCancelable(false);
            bouncingDialog.show();

        }

    }

}