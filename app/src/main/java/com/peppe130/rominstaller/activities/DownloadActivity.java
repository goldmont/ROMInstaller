package com.peppe130.rominstaller.activities;

import android.app.DownloadManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;
import com.mikepenz.iconics.IconicsDrawable;
import com.peppe130.rominstaller.ControlCenter;
import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.core.Utils;
import com.peppe130.rominstaller.core.CustomFileChooser;
import com.peppe130.rominstaller.core.Download;
import com.peppe130.rominstaller.core.FlashRecovery;
import com.peppe130.rominstaller.core.SystemProperties;


@SuppressWarnings("ResultOfMethodCallIgnored, ConstantConditions")
public class DownloadActivity extends AppCompatActivity implements CustomFileChooser.FileCallback {

    FancyButton BUTTON, BUTTON2, BUTTON3, BUTTON4, BUTTON5, BUTTON6, BUTTON7, BUTTON8, BUTTON9;
    Uri mDownloadLink;
    File mDownloadDirectory;
    String mDownloadedFileFinalName, mDownloadedFileMD5, mRecoveryPartition;
    DownloadManager.Request mRequest;
    Integer mBorderColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_layout);

        BUTTON = (FancyButton) findViewById(R.id.download_button);
        BUTTON2 = (FancyButton) findViewById(R.id.download_button2);
        BUTTON3 = (FancyButton) findViewById(R.id.download_button3);
        BUTTON4 = (FancyButton) findViewById(R.id.download_button4);
        BUTTON5 = (FancyButton) findViewById(R.id.download_button5);
        BUTTON6 = (FancyButton) findViewById(R.id.download_button6);
        BUTTON7 = (FancyButton) findViewById(R.id.download_button7);
        BUTTON8 = (FancyButton) findViewById(R.id.download_button8);
        BUTTON9 = (FancyButton) findViewById(R.id.download_button9);

        RelativeLayout mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        for (int mInt = 0; mInt != mRelativeLayout.getChildCount(); mInt++) {
            mRelativeLayout.getChildAt(mInt).setBackgroundColor(Utils.FetchPrimaryColor());
            mBorderColor = ContextCompat.getColor(DownloadActivity.this, ControlCenter.ButtonBorderColorChooser());
        }

        // Simple download without MD5 check
        BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDownloadLink = Uri.parse("http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip");
                mDownloadDirectory = new File(getString(R.string.rom_folder));
                mDownloadedFileFinalName = "Test.zip";

                mRequest = new DownloadManager.Request(mDownloadLink);
                mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), mDownloadedFileFinalName);

                new Download(
                        mRequest,
                        mDownloadDirectory,
                        mDownloadedFileFinalName).execute();
            }
        });

        BUTTON.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        BUTTON.setBorderColor(mBorderColor);
                        return false;
                    case MotionEvent.ACTION_UP:
                        BUTTON.setBorderColor(0);
                        return false;
                    case MotionEvent.ACTION_CANCEL:
                        BUTTON.setBorderColor(0);
                        return false;
                }
                return false;
            }
        });

        // Simple download with MD5 check - MD5 matches
        BUTTON2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDownloadLink = Uri.parse("http://www.mediafire.com/download/a0jswcs84smbi8i/Test2.zip");
                mDownloadDirectory = new File(getString(R.string.rom_folder));
                mDownloadedFileFinalName = "Test2.zip";
                mDownloadedFileMD5 = "3a416cafb312cb15ce6b3b09249fe6e6";

                mRequest = new DownloadManager.Request(mDownloadLink);
                mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), mDownloadedFileFinalName);

                new Download(
                        mRequest,
                        mDownloadDirectory,
                        mDownloadedFileFinalName,
                        mDownloadedFileMD5).execute();
            }
        });

        BUTTON2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        BUTTON2.setBorderColor(mBorderColor);
                        return false;
                    case MotionEvent.ACTION_UP:
                        BUTTON2.setBorderColor(0);
                        return false;
                    case MotionEvent.ACTION_CANCEL:
                        BUTTON2.setBorderColor(0);
                        return false;
                }
                return false;
            }
        });

        // Simple download with MD5 check - MD5 does not match
        BUTTON3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDownloadLink = Uri.parse("http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip");
                mDownloadDirectory = new File(getString(R.string.rom_folder));
                mDownloadedFileFinalName = "Test3.zip";
                mDownloadedFileMD5 = "3a416cafb312cb15ce6b3b09249fe6e6";

                mRequest = new DownloadManager.Request(mDownloadLink);
                mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), mDownloadedFileFinalName);

                new Download(
                        mRequest,
                        mDownloadDirectory,
                        mDownloadedFileFinalName,
                        mDownloadedFileMD5).execute();
            }
        });

        BUTTON3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        BUTTON3.setBorderColor(mBorderColor);
                        return false;
                    case MotionEvent.ACTION_UP:
                        BUTTON3.setBorderColor(0);
                        return false;
                    case MotionEvent.ACTION_CANCEL:
                        BUTTON3.setBorderColor(0);
                        return false;
                }
                return false;
            }
        });

        // Download ROM
        BUTTON4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDownloadLink = Uri.parse("http://www.mediafire.com/download/3a7gefzmxj44lv1/SampleROM.zip");
                mDownloadDirectory = new File(getString(R.string.rom_folder));
                mDownloadedFileFinalName = "SampleROM.zip";
                if (ControlCenter.TEST_MODE) {
                    Utils.MODEL = SystemProperties.get("ro.product.model");
                }

                mRequest = new DownloadManager.Request(mDownloadLink);
                mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), mDownloadedFileFinalName);

                new Download(
                        mRequest,
                        mDownloadDirectory,
                        mDownloadedFileFinalName, true).execute();
            }
        });

        BUTTON4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        BUTTON4.setBorderColor(mBorderColor);
                        return false;
                    case MotionEvent.ACTION_UP:
                        BUTTON4.setBorderColor(0);
                        return false;
                    case MotionEvent.ACTION_CANCEL:
                        BUTTON4.setBorderColor(0);
                        return false;
                }
                return false;
            }
        });

        // Download Recovery
        BUTTON5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDownloadLink = Uri.parse("http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip");
                mDownloadDirectory = new File(getString(R.string.rom_folder));
                mDownloadedFileFinalName = "Recovery.zip";
                mRecoveryPartition = "YourDeviceRecoveryPartition";

                mRequest = new DownloadManager.Request(mDownloadLink);
                mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), mDownloadedFileFinalName);

                new FlashRecovery(
                        mRequest,
                        mDownloadDirectory,
                        mDownloadedFileFinalName,
                        mRecoveryPartition, false).execute();
            }
        });

        BUTTON5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        BUTTON5.setBorderColor(mBorderColor);
                        return false;
                    case MotionEvent.ACTION_UP:
                        BUTTON5.setBorderColor(0);
                        return false;
                    case MotionEvent.ACTION_CANCEL:
                        BUTTON5.setBorderColor(0);
                        return false;
                }
                return false;
            }
        });

        // Download Recovery with Add-Ons
        BUTTON6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDownloadLink = Uri.parse("http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip");
                mDownloadDirectory = new File(getString(R.string.rom_folder));
                mDownloadedFileFinalName = "Recovery.zip";
                mRecoveryPartition = "YourDeviceRecoveryPartition";
                mRequest = new DownloadManager.Request(mDownloadLink);
                mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), mDownloadedFileFinalName);

                // Download Add-On N°1
                Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip"));
                Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(getString(R.string.rom_folder)));
                Utils.DOWNLOADED_FILE_NAME_LIST.add("Add-On.zip");
                Utils.DOWNLOADED_FILE_MD5_LIST.add("5fb732eea3d3e2b407fa7685c27a5354");
                DownloadManager.Request request = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(0));
                request.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(0).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(0));
                Utils.DOWNLOAD_REQUEST_LIST.add(request);

                // Download Add-On N°2
                Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.mediafire.com/download/a0jswcs84smbi8i/Test2.zip"));
                Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(getString(R.string.rom_folder)));
                Utils.DOWNLOADED_FILE_NAME_LIST.add("Add-On2.zip");
                Utils.DOWNLOADED_FILE_MD5_LIST.add("3a416cafb312cb15ce6b3b09249fe6e6");
                DownloadManager.Request request2 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(1));
                request2.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(1).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(1));
                Utils.DOWNLOAD_REQUEST_LIST.add(request2);

                // Download Add-On N°3
                Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip"));
                Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(getString(R.string.rom_folder)));
                Utils.DOWNLOADED_FILE_NAME_LIST.add("Add-On3.zip");
                Utils.DOWNLOADED_FILE_MD5_LIST.add("f946055c11a6a25d202f81171944fa1e");
                DownloadManager.Request request3 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(2));
                request3.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(2).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(2));
                Utils.DOWNLOAD_REQUEST_LIST.add(request3);

                new FlashRecovery(
                        mRequest,
                        mDownloadDirectory,
                        mDownloadedFileFinalName,
                        mRecoveryPartition, true).execute();
            }
        });

        BUTTON6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        BUTTON6.setBorderColor(mBorderColor);
                        return false;
                    case MotionEvent.ACTION_UP:
                        BUTTON6.setBorderColor(0);
                        return false;
                    case MotionEvent.ACTION_CANCEL:
                        BUTTON6.setBorderColor(0);
                        return false;
                }
                return false;
            }
        });

        // Multiple downloads without MD5 check
        BUTTON7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Download N°1
                Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip"));
                Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(getString(R.string.rom_folder)));
                Utils.DOWNLOADED_FILE_NAME_LIST.add("Test.zip");
                DownloadManager.Request request = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(0));
                request.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(0).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(0));
                Utils.DOWNLOAD_REQUEST_LIST.add(request);

                // Download N°2
                Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.mediafire.com/download/a0jswcs84smbi8i/Test2.zip"));
                Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(getString(R.string.rom_folder)));
                Utils.DOWNLOADED_FILE_NAME_LIST.add("Test2.zip");
                DownloadManager.Request request2 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(1));
                request2.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(1).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(1));
                Utils.DOWNLOAD_REQUEST_LIST.add(request2);

                // Download N°3
                Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip"));
                Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(getString(R.string.rom_folder)));
                Utils.DOWNLOADED_FILE_NAME_LIST.add("Test3.zip");
                DownloadManager.Request request3 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(2));
                request3.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(2).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(2));
                Utils.DOWNLOAD_REQUEST_LIST.add(request3);

                new Download(
                        Utils.DOWNLOAD_REQUEST_LIST.get(0),
                        Utils.DOWNLOAD_DIRECTORY_LIST.get(0),
                        Utils.DOWNLOADED_FILE_NAME_LIST.get(0), 1).execute();
            }
        });

        BUTTON7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        BUTTON7.setBorderColor(mBorderColor);
                        return false;
                    case MotionEvent.ACTION_UP:
                        BUTTON7.setBorderColor(0);
                        return false;
                    case MotionEvent.ACTION_CANCEL:
                        BUTTON7.setBorderColor(0);
                        return false;
                }
                return false;
            }
        });

        // Multiple downloads with MD5 check
        BUTTON8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Download N°1
                Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip"));
                Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(getString(R.string.rom_folder)));
                Utils.DOWNLOADED_FILE_NAME_LIST.add("Test.zip");
                Utils.DOWNLOADED_FILE_MD5_LIST.add("5fb732eea3d3e2b407fa7685c27a5354");
                DownloadManager.Request request = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(0));
                request.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(0).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(0));
                Utils.DOWNLOAD_REQUEST_LIST.add(request);

                // Download N°2
                Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.mediafire.com/download/a0jswcs84smbi8i/Test2.zip"));
                Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(getString(R.string.rom_folder)));
                Utils.DOWNLOADED_FILE_NAME_LIST.add("Test2.zip");
                Utils.DOWNLOADED_FILE_MD5_LIST.add("3a416cafb312cb15ce6b3b09249fe6e6");
                DownloadManager.Request request2 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(1));
                request2.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(1).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(1));
                Utils.DOWNLOAD_REQUEST_LIST.add(request2);

                // Download N°3
                Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip"));
                Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(getString(R.string.rom_folder)));
                Utils.DOWNLOADED_FILE_NAME_LIST.add("Test3.zip");
                Utils.DOWNLOADED_FILE_MD5_LIST.add("f946055c11a6a25d202f81171944fa1e");
                DownloadManager.Request request3 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(2));
                request3.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(2).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(2));
                Utils.DOWNLOAD_REQUEST_LIST.add(request3);

                new Download(
                        Utils.DOWNLOAD_REQUEST_LIST.get(0),
                        Utils.DOWNLOAD_DIRECTORY_LIST.get(0),
                        Utils.DOWNLOADED_FILE_NAME_LIST.get(0),
                        Utils.DOWNLOADED_FILE_MD5_LIST.get(0), 1).execute();
            }
        });

        BUTTON8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        BUTTON8.setBorderColor(mBorderColor);
                        return false;
                    case MotionEvent.ACTION_UP:
                        BUTTON8.setBorderColor(0);
                        return false;
                    case MotionEvent.ACTION_CANCEL:
                        BUTTON8.setBorderColor(0);
                        return false;
                }
                return false;
            }
        });

        // Multiple downloads mixed
        BUTTON9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Download N°1 - MD5 matches
                Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.mediafire.com/download/z7fn3nw1vn5oo8a/Test.zip"));
                Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(getString(R.string.rom_folder)));
                Utils.DOWNLOADED_FILE_NAME_LIST.add("Test.zip");
                Utils.DOWNLOADED_FILE_MD5_LIST.add("5fb732eea3d3e2b407fa7685c27a5354");
                DownloadManager.Request request = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(0));
                request.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(0).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(0));
                Utils.DOWNLOAD_REQUEST_LIST.add(request);

                // Download N°2 - MD5 does not match
                Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.mediafire.com/download/a0jswcs84smbi8i/Test2.zip"));
                Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(getString(R.string.rom_folder)));
                Utils.DOWNLOADED_FILE_NAME_LIST.add("Test2.zip");
                Utils.DOWNLOADED_FILE_MD5_LIST.add("3a416cafb312cb15ce6b3b09249fe6e6sd");
                DownloadManager.Request request2 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(1));
                request2.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(1).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(1));
                Utils.DOWNLOAD_REQUEST_LIST.add(request2);

                // Download N°3 - No MD5 check
                Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip"));
                Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(getString(R.string.rom_folder)));
                Utils.DOWNLOADED_FILE_NAME_LIST.add("Test3.zip");
                Utils.DOWNLOADED_FILE_MD5_LIST.add(null);
                DownloadManager.Request request3 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(2));
                request3.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(2).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(2));
                Utils.DOWNLOAD_REQUEST_LIST.add(request3);

                // Download N°4 - MD5 matches
                Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.mediafire.com/download/7waxhxzanc31y1z/Test3.zip"));
                Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(getString(R.string.rom_folder)));
                Utils.DOWNLOADED_FILE_NAME_LIST.add("Test4.zip");
                Utils.DOWNLOADED_FILE_MD5_LIST.add("f946055c11a6a25d202f81171944fa1e");
                DownloadManager.Request request4 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(3));
                request4.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(3).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(3));
                Utils.DOWNLOAD_REQUEST_LIST.add(request4);

                new Download(
                        Utils.DOWNLOAD_REQUEST_LIST.get(0),
                        Utils.DOWNLOAD_DIRECTORY_LIST.get(0),
                        Utils.DOWNLOADED_FILE_NAME_LIST.get(0),
                        Utils.DOWNLOADED_FILE_MD5_LIST.get(0), 1).execute();
            }
        });

        BUTTON9.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        BUTTON9.setBorderColor(mBorderColor);
                        return false;
                    case MotionEvent.ACTION_UP:
                        BUTTON9.setBorderColor(0);
                        return false;
                    case MotionEvent.ACTION_CANCEL:
                        BUTTON9.setBorderColor(0);
                        return false;
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Utils.ACTIVITY = this;

        File mROMFolder = new File(Environment.getExternalStorageDirectory().getPath() + "/" + getString(R.string.rom_folder));

        if(!mROMFolder.exists()) {
            mROMFolder.mkdirs();
        }

    }

    @Override
    public void onBackPressed() {
        if (!Utils.SHOULD_LOCK_UI) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        menu.findItem(R.id.clear_downloads).setEnabled(!Utils.SHOULD_LOCK_UI);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_download_menu, menu);
        IconicsDrawable mClearDownloadsIcon = new IconicsDrawable(DownloadActivity.this)
                .icon(ControlCenter.CLEAR_DOWNLOADS_ICON)
                .actionBar()
                .color(Color.WHITE)
                .sizeDp(30);
        MenuItem mClearDownloads = menu.findItem(R.id.clear_downloads);
        mClearDownloads.setIcon(mClearDownloadsIcon);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_downloads:
                final File mFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + getString(R.string.rom_folder));
                if (mFile.exists() && mFile.isDirectory() && mFile.listFiles().length != 0) {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(DownloadActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getString(R.string.clear_downloads_dialog_title))
                            .setContentText(getString(R.string.clear_downloads_dialog_message))
                            .setConfirmText(getString(R.string.positive_button))
                            .setCancelText(getString(R.string.what_is_inside_button))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    Utils.deleteFolderRecursively(mFile.toString());
                                    Utils.ToastShort(DownloadActivity.this, getString(R.string.cleared));
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            new CustomFileChooser.Builder(DownloadActivity.this)
                                                    .initialPath(Environment.getExternalStorageDirectory().getPath() + "/" + getString(R.string.rom_folder))
                                                    .lockFolder(null, true, true)
                                                    .canceledOnTouchOutside(true)
                                                    .show();
                                        }
                                    }, 300);
                                }
                            });
                    sweetAlertDialog.setCanceledOnTouchOutside(true);
                    sweetAlertDialog.show();
                } else {
                    Utils.ToastLong(DownloadActivity.this, getString(R.string.clear_downloads_folder_is_empty));
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onFileSelection(@NonNull CustomFileChooser dialog, @NonNull File file) {
        // Do nothing
    }

}
