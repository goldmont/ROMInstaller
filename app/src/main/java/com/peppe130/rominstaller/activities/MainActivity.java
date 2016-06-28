package com.peppe130.rominstaller.activities;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.XpPreferenceFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.iconics.IconicsDrawable;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.Utils;
import com.peppe130.rominstaller.core.CustomViewPager;
import com.peppe130.rominstaller.core.CheckFile;
import com.peppe130.rominstaller.core.CustomFileChooser;
import com.peppe130.rominstaller.core.InstallPopupDialog;
import com.peppe130.rominstaller.core.SystemProperties;
import cn.pedant.SweetAlert.SweetAlertDialog;


@SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
@SuppressLint("CommitPrefEdits")
public class MainActivity extends AppCompatActivity implements CustomFileChooser.FileCallback {

    SharedPreferences SP;
    SharedPreferences.Editor mEditor;
    public static ImageButton NEXT, BACK, DONE;
    public static CustomViewPager mViewPager;
    public static FragmentPagerAdapter mFragmentPagerAdapter;
    public static SmartTabLayout mSmartTabLayout;
    public static ArrayList<Fragment> mListFragment = new ArrayList<>();
    Boolean mDefaultOptions, mLastPageScrolled = false, mShouldShowToast = false, mNextOrInstallHint = false;
    Vibrator mVibrator;
    Integer mLatestPage, mCurrentItem, mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        if (Utils.SHOULD_CLOSE_ACTIVITY) {
            Utils.SHOULD_CLOSE_ACTIVITY = false;
            if (Utils.SHOULD_SHOW_DISCLAIMER_SCREEN) {
                AgreementActivity.mActivity.finish();
            }
        }

        if (mListFragment != null) {
            mListFragment.clear();
        }

        mListFragment.add(new FirstFragment());
        mListFragment.add(new SecondFragment());
        mListFragment.add(new ThirdFragment());
        mListFragment.add(new FourthFragment());
        mListFragment.add(new FifthFragment());
        mListFragment.add(new CscFragment());
        mListFragment.add(new BloatFragment());

        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mEditor = SP.edit();
        NEXT = (ImageButton) findViewById(R.id.next);
        BACK = (ImageButton) findViewById(R.id.back);
        DONE = (ImageButton) findViewById(R.id.done);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mDefaultOptions = SP.getBoolean("default", true);

        mViewPager = (CustomViewPager) findViewById(R.id.viewpager);
        mFragmentPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mSmartTabLayout = (SmartTabLayout) findViewById(R.id.viewpager_indicator);
        assert mSmartTabLayout != null;

        if (Utils.TEST_MODE) {
            Utils.SHOULD_LOCK_UI = false;
            invalidateOptionsMenu();
            if (Utils.BUTTON_UI) {
                if (mFragmentPagerAdapter.getCount() == 1) {
                    DONE.setVisibility(View.VISIBLE);
                } else {
                    NEXT.setVisibility(View.VISIBLE);
                }
            }
            mViewPager.setAdapter(mFragmentPagerAdapter);
            mSmartTabLayout.setViewPager(mViewPager);
            mEditor.putBoolean("default", true).apply();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mViewPager.setAdapter(mFragmentPagerAdapter);
                    mSmartTabLayout.setViewPager(mViewPager);
                }
            }, 200);
        } else {
            if (mDefaultOptions) {
                mViewPager.removeAllViews();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String mConfirmDevice = (String.format(getString(R.string.detect_device_dialog_message), SystemProperties.get("ro.product.model")));
                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getString(R.string.detect_device_dialog_title))
                                .setContentText(mConfirmDevice)
                                .setConfirmText(getString(R.string.positive_button))
                                .setCancelText(getString(R.string.list_button))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismiss();
                                        Utils.MODEL = SystemProperties.get("ro.product.model");
                                        Setup();
                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                new MaterialDialog.Builder(MainActivity.this)
                                                        .positiveText(getString(R.string.not_in_list_button))
                                                        .items(Utils.DEVICE_COMPATIBILITY_LIST)
                                                        .itemsCallback(new MaterialDialog.ListCallback() {
                                                            @Override
                                                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                                                Utils.MODEL = String.valueOf(text);
                                                                Setup();
                                                            }
                                                        })
                                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                            @Override
                                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Utils.ACTIVITY, SweetAlertDialog.ERROR_TYPE)
                                                                                .setTitleText(Utils.ACTIVITY.getString(R.string.incompatible_device_dialog_title))
                                                                                .setContentText(Utils.ACTIVITY.getString(R.string.device_not_in_list_dialog_message))
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
                                                                }, 300);
                                                            }
                                                        })
                                                        .cancelable(false)
                                                        .show();
                                            }
                                        }, 300);
                                    }
                                });
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.show();
                    }
                }, 1000);
            } else {
                Utils.SHOULD_LOCK_UI = false;
                invalidateOptionsMenu();
                if (Utils.BUTTON_UI) {
                    if (mFragmentPagerAdapter.getCount() == 1) {
                        DONE.setVisibility(View.VISIBLE);
                    } else {
                        NEXT.setVisibility(View.VISIBLE);
                    }
                }
                mViewPager.setAdapter(mFragmentPagerAdapter);
                mSmartTabLayout.setViewPager(mViewPager);
                mEditor.putBoolean("default", true).apply();
                Utils.ZIP_FILE = new File(SP.getString("file_path", ""));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mViewPager.setAdapter(mFragmentPagerAdapter);
                        mSmartTabLayout.setViewPager(mViewPager);
                    }
                }, 200);
            }
        }

        NEXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BACK.setVisibility(View.VISIBLE);

                if (mViewPager.getCurrentItem() != mFragmentPagerAdapter.getCount()) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }
            }
        });

        NEXT.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Utils.ToastShort(MainActivity.this, getString(R.string.next_button));
                return true;
            }
        });

        BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mNextOrInstallHint = false;
                DONE.setVisibility(View.GONE);
                NEXT.setVisibility(View.VISIBLE);

                if (mViewPager.getCurrentItem() != 0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                }
            }
        });

        BACK.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Utils.ToastShort(MainActivity.this, getString(R.string.back_button));
                return true;
            }
        });

        DONE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.TEST_MODE) {
                    ExportPreferences();
                    getFragmentManager().beginTransaction()
                            .add(new InstallPopupDialog(Utils.ZIP_FILE.toString()), "install_fragment")
                            .commitAllowingStateLoss();
                } else {
                    Utils.ToastShort(MainActivity.this, getString(R.string.can_not_install));
                }
            }
        });

        DONE.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Utils.ToastShort(MainActivity.this, getString(R.string.done_button));
                return true;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (mLatestPage.equals(position)) {
                    if (Utils.BUTTON_UI) {
                        mLastPageScrolled = false;
                        mNextOrInstallHint = true;
                        NEXT.setVisibility(View.GONE);
                        DONE.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPosition = position;
                mLatestPage = mFragmentPagerAdapter.getCount() - 1;
                if (mViewPager.getCurrentItem() > 0) {
                    setTitle(R.string.toolbar_title2);
                } else {
                    setTitle(R.string.app_name);
                    BACK.setVisibility(View.GONE);
                }
                if (mShouldShowToast && mLatestPage.equals(position)) {
                    if (!Utils.BUTTON_UI) {
                        mShouldShowToast = false;
                        if (!Utils.TEST_MODE) {
                            Utils.ToastShort(MainActivity.this, getString(R.string.swipe_right_to_install));
                        }
                    }
                }
                if (!Utils.BUTTON_UI) {
                    if (mLastPageScrolled && mLatestPage.equals(position)) {
                        mLastPageScrolled = false;
                        if (!Utils.TEST_MODE) {
                            ExportPreferences();
                            getFragmentManager().beginTransaction()
                                    .add(new InstallPopupDialog(Utils.ZIP_FILE.toString()), "install_fragment")
                                    .commitAllowingStateLoss();
                        } else {
                            Utils.ToastShort(MainActivity.this, getString(R.string.can_not_install));
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mLatestPage = mFragmentPagerAdapter.getCount() - 1;
                mCurrentItem = mViewPager.getCurrentItem();
                mLastPageScrolled = mCurrentItem.equals(mLatestPage) && state == 1;
                if (mViewPager.getCurrentItem() == mFragmentPagerAdapter.getCount() -2) {
                    mShouldShowToast = true;
                }
            }
        });

    }

    public void Setup() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.set_path_dialog_title))
                        .setContentText(getString(R.string.set_path_dialog_message))
                        .setConfirmText(getString(R.string.ok_button))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
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
        }, 500);
    }

    public void ExportPreferences() {
        try {
            FileWriter mFileWriter = new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/" + getString(R.string.rom_name) + "/" + "preferences.prop");
            BufferedWriter mBufferedWriter = new BufferedWriter(mFileWriter);
            mBufferedWriter.write(
                            "wipe=" + (String.valueOf(SP.getBoolean("wipe", false))) +
                            "\nkernel=" + (SP.getString("listKernel", "1")) +
                            "\nstatusbar=" + (SP.getString("listStatusBar", "2")) +
                            "\nsf_qc=" + String.valueOf(SP.getBoolean("SF-QC", false)) +
                            "\nmy_files=" + SP.getString("listMyFile", "2") +
                            "\nkeyboard=" + (SP.getString("listKey", "2")) +
                            "\nmms=" + SP.getString("listMMS", "2") +
                            "\nfonts=" + SP.getString("listFonts", "3") +
                            "\naccuweather=" + SP.getString("listAccuweather", "4") +
                            "\nsounds=" + SP.getString("listSound", "3") +
                            "\nboot_animation=" + SP.getString("listBootanimation", "3") +
                            "\nscrolling_wallpaper=" + String.valueOf(SP.getBoolean("scrolling_wallpaper", false)) +
                            "\nincreasing_ringtone=" + String.valueOf(SP.getBoolean("increasing_ringtone", true)) +
                            "\nsec_symbols=" + String.valueOf(SP.getBoolean("sec_symbol", false)) +
                            "\nvoice_call=" + String.valueOf(SP.getBoolean("voice_call", true)) +
                            "\ncall_button=" + String.valueOf(SP.getBoolean("call_button", true)) +
                            "\nscheduled_messages=" + String.valueOf(SP.getBoolean("scheduled_messages", true)) +
                            "\nshutter_sound=" + String.valueOf(SP.getBoolean("shutter_sound", false)) +
                            "\nboot_sound=" + String.valueOf(SP.getBoolean("boot_sound", false)) +
                            "\nxposed=" + String.valueOf(SP.getBoolean("xposed", false)) +
                            "\ncsc1=" + String.valueOf(SP.getBoolean("csc1", true)) +
                            "\ncsc2=" + String.valueOf(SP.getBoolean("csc2", true)) +
                            "\ncsc3=" + String.valueOf(SP.getBoolean("csc3", true)) +
                            "\ncsc4=" + String.valueOf(SP.getBoolean("csc4", true)) +
                            "\ncsc5=" + String.valueOf(SP.getBoolean("csc5", true)) +
                            "\ncsc6=" + String.valueOf(SP.getBoolean("csc6", true)) +
                            "\ncsc7=" + String.valueOf(SP.getBoolean("csc7", true)) +
                            "\ncsc8=" + String.valueOf(SP.getBoolean("csc8", true)) +
                            "\nbloat1=" + String.valueOf(SP.getBoolean("bloat1", true)) +
                            "\nbloat2=" + String.valueOf(SP.getBoolean("bloat2", true)) +
                            "\nbloat3=" + String.valueOf(SP.getBoolean("bloat3", false)) +
                            "\nbloat4=" + String.valueOf(SP.getBoolean("bloat4", false)) +
                            "\nbloat5=" + String.valueOf(SP.getBoolean("bloat5", true)) +
                            "\nbloat6=" + String.valueOf(SP.getBoolean("bloat6", false)) +
                            "\nbloat7=" + String.valueOf(SP.getBoolean("bloat7", false))
            );
            mBufferedWriter.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void DefaultValues() {
        // CheckBoxes
        mEditor.putBoolean("wipe", false).commit();
        mEditor.putBoolean("SF-QC", false).commit();
        mEditor.putBoolean("scrolling_wallpaper", false).commit();
        mEditor.putBoolean("increasing_ringtone", true).commit();
        mEditor.putBoolean("sec_symbols", false).commit();
        mEditor.putBoolean("voice_call", true).commit();
        mEditor.putBoolean("shutter_sound", false).commit();
        mEditor.putBoolean("boot_sound", false).commit();
        mEditor.putBoolean("call_button", true).commit();
        mEditor.putBoolean("xposed", false).commit();
        mEditor.putBoolean("csc1", false).commit();
        mEditor.putBoolean("csc2", false).commit();
        mEditor.putBoolean("csc3", false).commit();
        mEditor.putBoolean("csc4", false).commit();
        mEditor.putBoolean("csc5", false).commit();
        mEditor.putBoolean("csc6", false).commit();
        mEditor.putBoolean("csc7", false).commit();
        mEditor.putBoolean("csc8", false).commit();
        mEditor.putBoolean("csc10", false).commit();
        mEditor.putBoolean("csc11", false).commit();
        mEditor.putBoolean("bloat1", false).commit();
        mEditor.putBoolean("bloat2", false).commit();
        mEditor.putBoolean("bloat3", false).commit();
        mEditor.putBoolean("bloat4", false).commit();
        mEditor.putBoolean("bloat5", false).commit();
        mEditor.putBoolean("bloat6", false).commit();
        mEditor.putBoolean("bloat7", false).commit();
        // Lists
        mEditor.putString("listKernel", "1").commit();
        mEditor.putString("listStatusBar", "2").commit();
        mEditor.putString("listMyFiles", "2").commit();
        mEditor.putString("listKey", "2").commit();
        mEditor.putString("listMMS", "2").commit();
        mEditor.putString("listFonts", "3").commit();
        mEditor.putString("listAccuweather", "4").commit();
        mEditor.putString("listSound", "3").commit();
        mEditor.putString("listBootanimation", "3").commit();
    }

    public static class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return mListFragment.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mListFragment.get(position);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Utils.ACTIVITY = this;

        File mPath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + getString(R.string.rom_download_folder));
        File mSample = new File(mPath.getPath() + "/" + "Sample.zip");

        if(!mPath.exists()) {
            mPath.mkdirs();
        }

        if(!mSample.exists()) {
            Utils.copyAssetFolder(MainActivity.this.getAssets(), "sample", mPath.toString());
        }

    }

    @Override
    public void onBackPressed() {

        if (Utils.BUTTON_UI && mFragmentPagerAdapter.getCount() != 1) {
            mNextOrInstallHint = false;
            DONE.setVisibility(View.GONE);
            NEXT.setVisibility(View.VISIBLE);
        }

        if (mViewPager.getCurrentItem() == 0 && !Utils.SHOULD_LOCK_UI) {

            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.exit_dialog_title))
                    .setContentText(getString(R.string.exit_dialog_message))
                    .showCancelButton(true)
                    .setCancelText(getString(R.string.negative_button))
                    .setConfirmText(getString(R.string.positive_button))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            finishAffinity();
                        }
                    });
            sweetAlertDialog.setCanceledOnTouchOutside(true);
            sweetAlertDialog.show();

        } else if (mViewPager.getCurrentItem() > 0) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }

    }

    @Override
    public void onFileSelection(@NonNull CustomFileChooser dialog, @NonNull final File file) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String mContent = (String.format(getString(R.string.confirm_file_dialog_message), file.getAbsolutePath()));
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.confirm_file_dialog_title))
                        .setContentText(mContent)
                        .setConfirmText(getString(R.string.ok_button))
                        .setCancelText(getString(R.string.negative_button))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Utils.ZIP_FILE = new File(file.getAbsolutePath());
                                Utils.FILE_NAME = file.getName();
                                mEditor.putString("file_path", file.getAbsolutePath()).apply();
                                new CheckFile().execute();
                                sDialog.dismissWithAnimation();
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
        }, 300);

    }

    public static class FirstFragment extends XpPreferenceFragment {

        public static FirstFragment newInstance() {
            return new FirstFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.tab_ui_first_preference);
        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

    public static class SecondFragment extends XpPreferenceFragment {

        public static SecondFragment newInstance() {
            return new SecondFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.tab_ui_second_preference);
        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

    public static class ThirdFragment extends XpPreferenceFragment {

        public static ThirdFragment newInstance() {
            return new ThirdFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.tab_ui_third_preference);
        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

    public static class FourthFragment extends XpPreferenceFragment {

        public static FourthFragment newInstance() {
            return new FourthFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.tab_ui_fourth_preference);
        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

    public static class FifthFragment extends XpPreferenceFragment {

        public static FifthFragment newInstance() {
            return new FifthFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.tab_ui_fifth_preference);
        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

    public static class CscFragment extends XpPreferenceFragment {

        public static CscFragment newInstance() {
            return new CscFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.tab_ui_csc_preference);
        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

    public static class BloatFragment extends XpPreferenceFragment {

        public static BloatFragment newInstance() {
            return new BloatFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.tab_ui_bloat_preference);
        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        menu.findItem(R.id.settings).setEnabled(!Utils.SHOULD_LOCK_UI);
        menu.findItem(R.id.changelog).setEnabled(!Utils.SHOULD_LOCK_UI);
        menu.findItem(R.id.default_options).setEnabled(!Utils.SHOULD_LOCK_UI);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        IconicsDrawable mSettingsIcon = new IconicsDrawable(MainActivity.this)
                .icon(Utils.SETTINGS_ICON)
                .actionBar()
                .color(Color.WHITE)
                .sizeDp(30);
        IconicsDrawable mChangelogIcon = new IconicsDrawable(MainActivity.this)
                .icon(Utils.CHANGELOG_ICON)
                .actionBar()
                .color(Color.WHITE)
                .sizeDp(30);
        IconicsDrawable mDefaultOptionsIcon = new IconicsDrawable(MainActivity.this)
                .icon(Utils.DEFAULT_OPTIONS_ICON)
                .actionBar()
                .color(Color.WHITE)
                .sizeDp(35);
        MenuItem mSettings, mChangelog, mDefaultOptions;
        mSettings = menu.findItem(R.id.settings);
        mChangelog = menu.findItem(R.id.changelog);
        mDefaultOptions = menu.findItem(R.id.default_options);
        mSettings.setIcon(mSettingsIcon);
        mChangelog.setIcon(mChangelogIcon);
        mDefaultOptions.setIcon(mDefaultOptionsIcon);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.default_options:
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.restore_default))
                        .setContentText(getString(R.string.confirm_restore_default))
                        .setConfirmText(getString(R.string.positive_button))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                DefaultValues();

                                sDialog.setTitleText(getString(R.string.restored_default_options_title))
                                        .setContentText(getString(R.string.restored_default_options_message))
                                        .setConfirmText(getString(R.string.ok_button))
                                        .showCancelButton(false)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                // Recreate activity
                                                finishAffinity();
                                                startActivity(getIntent());
                                                mEditor.putBoolean("default", false).commit();
                                            }
                                        });
                                sDialog.setCancelable(false);
                                sDialog.setCanceledOnTouchOutside(false);
                                sDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .setCancelText(getString(R.string.negative_button))
                        .showCancelButton(true);
                sweetAlertDialog.setCanceledOnTouchOutside(true);
                sweetAlertDialog.show();
                break;
            case R.id.changelog:
                startActivity(new Intent(MainActivity.this, ChangelogActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

}