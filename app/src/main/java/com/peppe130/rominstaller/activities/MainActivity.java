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

package com.peppe130.rominstaller.activities;

import android.support.annotation.NonNull;
import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import java.io.File;

import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.core.Utils;
import com.peppe130.rominstaller.ControlCenter;
import com.peppe130.rominstaller.core.CheckFile;
import com.peppe130.bouncingdialogs.BouncingDialog;
import com.peppe130.rominstaller.core.CustomViewPager;
import com.peppe130.rominstaller.core.CustomFileChooser;
import com.peppe130.rominstaller.core.FragmentsCollector;
import com.peppe130.rominstaller.core.InstallDialog;
import com.peppe130.rominstaller.core.SystemProperties;
import com.afollestad.assent.Assent;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.iconics.IconicsDrawable;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.SmartTabIndicationInterpolator;


public class MainActivity extends AppCompatActivity implements CustomFileChooser.FileCallback {

    private static ImageButton NEXT, BACK, DONE;
    private static FrameLayout mFrameLayout;
    private static CustomViewPager mViewPager;
    private static FragmentPagerAdapter mFragmentPagerAdapter;
    private static SmartTabLayout mSmartTabLayout;
    Integer mTheme, mHeaderColor, mHeaderIcon, mLatestPage, mCurrentItem;
    Boolean mLastPageScrolled = false, mShouldShowSwipeHint = false;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Utils.InitializeActivity(this);

        if (Utils.FetchTheme(this) == 0) {

            mTheme = R.style.AppTheme_Light;

            mHeaderIcon = R.mipmap.ic_launcher_light;

            mHeaderColor = ContextCompat.getColor(this, R.color.colorPrimary_Theme_Light);

        } else {

            mTheme = R.style.AppTheme_Dark;

            mHeaderIcon = R.mipmap.ic_launcher_dark;

            mHeaderColor = ContextCompat.getColor(this, R.color.colorPrimary_Theme_Dark);

        }

        setTheme(mTheme);

        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), mHeaderIcon);

        setTaskDescription(new ActivityManager.TaskDescription(getString(R.string.app_name), mBitmap, mHeaderColor));

        setContentView(R.layout.activity_main_layout);

        NEXT = (ImageButton) findViewById(R.id.next);

        BACK = (ImageButton) findViewById(R.id.back);

        DONE = (ImageButton) findViewById(R.id.done);

        mFrameLayout = (FrameLayout) findViewById(R.id.viewPagerContainer);

        mFrameLayout.setBackgroundColor(ContextCompat.getColor(this, Utils.FetchBackgroundColor()));

        mViewPager = (CustomViewPager) findViewById(R.id.viewPager);

        mFragmentPagerAdapter = new Adapter(getSupportFragmentManager());

        mSmartTabLayout = (SmartTabLayout) findViewById(R.id.viewPagerIndicator);

        assert mSmartTabLayout != null;

        mSmartTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, Utils.FetchAccentColor()));

        mSmartTabLayout.setIndicationInterpolator(SmartTabIndicationInterpolator.LINEAR);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Assent.setActivity(this, this);

        }

        FragmentsCollector.Setup();

        if (ControlCenter.TEST_MODE) {

            Utils.SHOULD_LOCK_UI = false;

            invalidateOptionsMenu();

            if (ControlCenter.BUTTON_UI) {

                if (mFragmentPagerAdapter.getCount() == 1) {

                    DONE.setVisibility(View.VISIBLE);

                } else {

                    NEXT.setVisibility(View.VISIBLE);

                }

            }

            mViewPager.setAdapter(mFragmentPagerAdapter);

            mSmartTabLayout.setViewPager(mViewPager);

            Utils.SP_EDITOR.putBoolean("default_values", true).apply();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    mViewPager.setAdapter(mFragmentPagerAdapter);

                    mSmartTabLayout.setViewPager(mViewPager);

                }

            }, 200);

        } else {

            if (Utils.SP.getBoolean("default_values", true)) {

                mViewPager.removeAllViews();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        String mConfirmDevice = (String.format(getString(R.string.detect_device_dialog_message), SystemProperties.get("ro.product.model")));

                        BouncingDialog bouncingDialog = new BouncingDialog(MainActivity.this, BouncingDialog.WARNING_TYPE)
                                .title(getString(R.string.detect_device_dialog_title))
                                .content(mConfirmDevice)
                                .positiveText(getString(R.string.continue_button))
                                .negativeText(getString(R.string.list_button))
                                .onPositive(new BouncingDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(BouncingDialog bouncingDialog1) {

                                        Utils.SetDeviceModel(SystemProperties.get("ro.product.model"));

                                        FileChooser();

                                    }

                                })
                                .onNegative(new BouncingDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(BouncingDialog bouncingDialog1) {

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                new MaterialDialog.Builder(MainActivity.this)
                                                        .positiveText(getString(R.string.not_in_list_button))
                                                        .items(ControlCenter.DEVICE_COMPATIBILITY_LIST)
                                                        .itemsCallback(new MaterialDialog.ListCallback() {
                                                            @Override
                                                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                                                dialog.dismiss();

                                                                Utils.SetDeviceModel(text.toString());

                                                                FileChooser();

                                                            }
                                                        })
                                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                            @Override
                                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                                                dialog.dismiss();

                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {

                                                                        BouncingDialog bouncingDialog2 = new BouncingDialog(Utils.ACTIVITY, BouncingDialog.ERROR_TYPE)
                                                                                .title(Utils.ACTIVITY.getString(R.string.incompatible_device_dialog_title))
                                                                                .content(Utils.ACTIVITY.getString(R.string.device_not_in_list_dialog_message))
                                                                                .positiveText(Utils.ACTIVITY.getString(R.string.close_button))
                                                                                .onPositive(new BouncingDialog.SingleButtonCallback() {
                                                                                    @Override
                                                                                    public void onClick(BouncingDialog bouncingDialog3) {

                                                                                        bouncingDialog3.dismiss();

                                                                                        Utils.ACTIVITY.finishAffinity();

                                                                                    }

                                                                                });
                                                                        bouncingDialog2.setCancelable(false);
                                                                        bouncingDialog2.show();

                                                                    }

                                                                }, 300);

                                                            }

                                                        })
                                                        .cancelable(false)
                                                        .show();

                                            }

                                        }, 400);

                                    }

                                })
                                .autoDismiss(true);
                        bouncingDialog.setCancelable(false);
                        bouncingDialog.show();

                    }
                }, 1000);

            } else {

                Utils.SHOULD_LOCK_UI = false;

                invalidateOptionsMenu();

                if (ControlCenter.BUTTON_UI) {

                    if (mFragmentPagerAdapter.getCount() == 1) {

                        DONE.setVisibility(View.VISIBLE);

                    } else {

                        NEXT.setVisibility(View.VISIBLE);

                    }

                }

                mViewPager.setAdapter(mFragmentPagerAdapter);

                mSmartTabLayout.setViewPager(mViewPager);

                Utils.SP_EDITOR.putBoolean("default_values", true).apply();

                Utils.SetZipFile(new File(Utils.SP.getString("file_path", "")));

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

                Utils.ExportPreferences();

                if (!ControlCenter.TEST_MODE) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        Utils.CheckPermissions(Utils.Action.INSTALL_DIALOG);

                    } else {

                        getFragmentManager().beginTransaction()
                                .add(new InstallDialog(), "install_dialog")
                                .commitAllowingStateLoss();

                    }

                } else {

                    Utils.ToastShort(MainActivity.this, getString(R.string.preferences_exported));

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

                if (mLatestPage != null && mLatestPage.equals(position) && ControlCenter.BUTTON_UI) {

                    mLastPageScrolled = false;

                    NEXT.setVisibility(View.GONE);

                    DONE.setVisibility(View.VISIBLE);

                } else if (mLatestPage != null && !mLatestPage.equals(position) && ControlCenter.BUTTON_UI) {

                    DONE.setVisibility(View.GONE);

                    NEXT.setVisibility(View.VISIBLE);

                } else if (mLatestPage != null && !mLatestPage.equals(position) && !ControlCenter.BUTTON_UI) {

                    if (Utils.SNACKBAR != null) {

                        Utils.SNACKBAR.dismiss();

                    }

                }

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                mLatestPage = mFragmentPagerAdapter.getCount() - 1;

                switch (mViewPager.getCurrentItem()) {

                    case 0:

                        setTitle(R.string.app_name);

                        if (ControlCenter.BUTTON_UI) {

                            BACK.setVisibility(View.GONE);

                        }

                        break;

                    default:

                        setTitle(R.string.toolbar_title);

                        break;

                }

                if (mShouldShowSwipeHint && mLatestPage.equals(position) && !ControlCenter.BUTTON_UI && !ControlCenter.TEST_MODE) {

                    mShouldShowSwipeHint = false;

                    Utils.Snackbar(mFrameLayout, getString(R.string.swipe_right_to_install), 5000);

                }

                if (!ControlCenter.BUTTON_UI) {

                    if (mLastPageScrolled && mLatestPage.equals(position)) {

                        mLastPageScrolled = false;

                        Utils.ExportPreferences();

                        if (!ControlCenter.TEST_MODE) {

                            if (Utils.SNACKBAR != null) {

                                Utils.SNACKBAR.dismiss();

                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                Utils.CheckPermissions(Utils.Action.INSTALL_DIALOG);

                            } else {

                                getFragmentManager().beginTransaction()
                                        .add(new InstallDialog(), "install_dialog")
                                        .commitAllowingStateLoss();

                            }

                        } else {

                            Utils.ToastShort(MainActivity.this, getString(R.string.preferences_exported));

                        }

                    }

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                mLatestPage = mFragmentPagerAdapter.getCount() - 1;

                mCurrentItem = mViewPager.getCurrentItem();

                mLastPageScrolled = mCurrentItem.equals(mLatestPage) && state == 1;

                if (mViewPager.getCurrentItem() == mFragmentPagerAdapter.getCount() - 2) {

                    mShouldShowSwipeHint = true;

                }

            }

        });

    }

    public static void InitialSetup() {

        mViewPager.setAdapter(MainActivity.mFragmentPagerAdapter);

        mSmartTabLayout.setViewPager(mViewPager);

        if (mFragmentPagerAdapter.getCount() == 1) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (ControlCenter.BUTTON_UI) {

                        DONE.setVisibility(View.VISIBLE);

                    } else {

                        Utils.Snackbar(mFrameLayout, Utils.ACTIVITY.getString(R.string.swipe_right_to_install), 5000);

                    }

                }
            }, 300);

        } else {

            if (ControlCenter.BUTTON_UI) {

                BACK.setVisibility(View.GONE);

                DONE.setVisibility(View.GONE);

                NEXT.setVisibility(View.VISIBLE);

            }

        }

    }

    public void FileChooser() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                BouncingDialog bouncingDialog = new BouncingDialog(MainActivity.this, BouncingDialog.WARNING_TYPE)
                        .title(getString(R.string.set_path_dialog_title))
                        .content(getString(R.string.set_path_dialog_message))
                        .positiveText(getString(R.string.ok_button))
                        .onPositive(new BouncingDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(BouncingDialog bouncingDialog1) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            Utils.CheckPermissions(Utils.Action.FILE_CHOOSER);

                                        }
                                    }, 300);

                                } else {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            Utils.FileChooser();

                                        }
                                    }, 300);

                                }

                            }

                        })
                        .autoDismiss(true);
                bouncingDialog.setCancelable(false);
                bouncingDialog.show();

            }

        }, 500);

    }

    public static class Adapter extends FragmentPagerAdapter {

        public Adapter(FragmentManager fragmentManager) {

            super(fragmentManager);

        }

        @Override
        public int getCount() {

            return FragmentsCollector.GetCount();

        }

        @Override
        public Fragment getItem(int position) {

            return FragmentsCollector.GetFragment(position);

        }

    }

    @Override
    protected void onResume() {

        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Assent.setActivity(this, this);

        }

        Utils.EnvironmentChecker(this);

    }

    @Override
    protected void onPause() {

        super.onPause();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (isFinishing()) {

                Assent.setActivity(this, null);

            }

        }

    }

    @Override
    public void onBackPressed() {

        if (mViewPager.getCurrentItem() == 0 && !Utils.SHOULD_LOCK_UI) {

            BouncingDialog bouncingDialog = new BouncingDialog(MainActivity.this, BouncingDialog.WARNING_TYPE)
                    .title(getString(R.string.exit_dialog_title))
                    .content(getString(R.string.exit_dialog_message))
                    .positiveText(getString(R.string.positive_button))
                    .negativeText(getString(R.string.negative_button))
                    .onPositive(new BouncingDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(BouncingDialog bouncingDialog1) {

                            bouncingDialog1.dismiss();

                            finishAffinity();

                        }

                    });
            bouncingDialog.setCanceledOnTouchOutside(true);
            bouncingDialog.show();

        } else if (mViewPager.getCurrentItem() > 0) {

            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Assent.handleResult(permissions, grantResults);

    }

    @Override
    public void onFileSelection(@NonNull CustomFileChooser dialog, @NonNull final File file) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                BouncingDialog bouncingDialog = new BouncingDialog(MainActivity.this, BouncingDialog.FILE_CHOOSER_TYPE)
                        .title(getString(R.string.confirm_file_dialog_title))
                        .title2(getString(R.string.confirm_file_dialog_title2))
                        .content2(file.getName())
                        .content3(file.getParent())
                        .positiveText(getString(R.string.continue_button))
                        .negativeText(getString(R.string.negative_button))
                        .onPositive(new BouncingDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(BouncingDialog bouncingDialog1) {

                                Utils.SetZipFile(file);

                                Utils.SP_EDITOR.putString("file_path", file.getAbsolutePath()).apply();

                                new CheckFile().execute();

                            }

                        })
                        .onNegative(new BouncingDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(BouncingDialog bouncingDialog1) {

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
        }, 300);

    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {

        menu.findItem(R.id.settings).setEnabled(!Utils.SHOULD_LOCK_UI);

        menu.findItem(R.id.changelog).setEnabled(!Utils.SHOULD_LOCK_UI);

        menu.findItem(R.id.default_values).setEnabled(!Utils.SHOULD_LOCK_UI);

        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem mSettings, mChangelog, mDefaultOptions;

        getMenuInflater().inflate(R.menu.activity_main_menu, menu);

        IconicsDrawable mSettingsIcon = new IconicsDrawable(MainActivity.this)
                .icon(ControlCenter.SETTINGS_ICON)
                .actionBar()
                .color(Color.WHITE)
                .sizeDp(30);

        IconicsDrawable mChangelogIcon = new IconicsDrawable(MainActivity.this)
                .icon(ControlCenter.CHANGELOG_ICON)
                .actionBar()
                .color(Color.WHITE)
                .sizeDp(30);

        IconicsDrawable mDefaultValuesIcon = new IconicsDrawable(MainActivity.this)
                .icon(ControlCenter.DEFAULT_VALUES_ICON)
                .actionBar()
                .color(Color.WHITE)
                .sizeDp(35);

        mSettings = menu.findItem(R.id.settings);
        mSettings.setIcon(mSettingsIcon);

        mChangelog = menu.findItem(R.id.changelog);
        mChangelog.setIcon(mChangelogIcon);

        mDefaultOptions = menu.findItem(R.id.default_values);
        mDefaultOptions.setIcon(mDefaultValuesIcon);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.default_values:

                BouncingDialog bouncingDialog = new BouncingDialog(MainActivity.this, BouncingDialog.WARNING_TYPE)
                        .title(getString(R.string.confirm_default_values_title))
                        .content(getString(R.string.confirm_default_values_message))
                        .positiveText(getString(R.string.positive_button))
                        .onPositive(new BouncingDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(BouncingDialog bouncingDialog1) {

                                Utils.SetDefaultValues();

                                bouncingDialog1.dismiss();

                                finishAffinity();

                                startActivity(new Intent(getIntent()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                Utils.SP_EDITOR.putBoolean("default_values", false).commit();

                            }
                        })
                        .negativeText(getString(R.string.negative_button))
                        .showNegativeButton(true);
                bouncingDialog.setCanceledOnTouchOutside(true);
                bouncingDialog.show();

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