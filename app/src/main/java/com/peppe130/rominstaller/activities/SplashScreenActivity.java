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

import org.jetbrains.annotations.Contract;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.core.Utils;
import com.peppe130.rominstaller.ControlCenter;
import org.michaelevans.colorart.library.ColorArt;


public class SplashScreenActivity extends Activity {

    Bitmap mBitmap;
    Bundle mBundle;
    ColorArt mColorArt;
    ImageView mImageView;
    RelativeLayout mRelativeLayout;
    BitmapFactory.Options mOptions;
    Boolean isActivityVisible = true;
    Integer mSplashScreenImage, mHeaderColor, mHeaderIcon;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen_layout);

        Utils.SP = PreferenceManager.getDefaultSharedPreferences(this);

        mBundle = getIntent().getExtras();

        if (ControlCenter.SHOULD_SHOW_SPLASH_SCREEN) {

            mRelativeLayout = (RelativeLayout) findViewById(R.id.splash_screen_layout);

            mImageView = (ImageView) findViewById(R.id.imageView);

            if (Utils.FetchTheme(this) == 0) {

                mHeaderColor = ContextCompat.getColor(this, R.color.colorPrimary_Theme_Light);

                mHeaderIcon = R.mipmap.ic_launcher_light;

                mSplashScreenImage = ControlCenter.SPLASH_SCREEN_IMAGE_LIGHT;

            } else {

                mHeaderColor = ContextCompat.getColor(this, R.color.colorPrimary_Theme_Dark);

                mHeaderIcon = R.mipmap.ic_launcher_dark;

                mSplashScreenImage = ControlCenter.SPLASH_SCREEN_IMAGE_DARK;

            }

            mOptions = new BitmapFactory.Options();

            mOptions.inJustDecodeBounds = true;

            BitmapFactory.decodeResource(getResources(), mSplashScreenImage, mOptions);

            mBitmap = DecodeBitmapFromResource(getResources(), mSplashScreenImage, 500, 500);

            mColorArt = new ColorArt(mBitmap);

            mRelativeLayout.setBackgroundColor(mColorArt.getBackgroundColor());

            mImageView.setImageBitmap(mBitmap);

            Bitmap mHeaderIconBitmap = BitmapFactory.decodeResource(getResources(), mHeaderIcon);

            setTaskDescription(new ActivityManager.TaskDescription(getString(R.string.app_name), mHeaderIconBitmap, mHeaderColor));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (isActivityVisible) {

                        if ((mBundle != null && mBundle.getBoolean("THEME")) || Utils.SP.getBoolean("activity_alias", false)) {

                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));

                            Utils.SP_EDITOR.putBoolean("activity_alias", false).apply();

                        } else {

                            if (ControlCenter.SHOULD_SHOW_DISCLAIMER_SCREEN) {

                                startActivity(new Intent(SplashScreenActivity.this, AgreementActivity.class));

                            } else {

                                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));

                            }

                        }

                    }

                    finish();

                    overridePendingTransition(android.R.anim.fade_in, R.anim.zoom_in);

                }
            }, ControlCenter.SPLASH_SCREEN_DELAY);

        } else {

            if ((mBundle != null && mBundle.getBoolean("THEME")) || Utils.SP.getBoolean("activity_alias", false)) {

                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));

                Utils.SP_EDITOR.putBoolean("activity_alias", false).apply();

            } else {

                if (ControlCenter.SHOULD_SHOW_DISCLAIMER_SCREEN) {

                    startActivity(new Intent(SplashScreenActivity.this, AgreementActivity.class));

                } else {

                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));

                }

            }

            finish();

        }

    }

    @Contract(pure = true)
    private int CalculateInSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int mHeight = options.outHeight;

        final int mWidth = options.outWidth;

        int inSampleSize = 1;

        if (mHeight > reqHeight || mWidth > reqWidth) {

            final int mHalfHeight = mHeight / 2;

            final int mHalfWidth = mWidth / 2;

            while ((mHalfHeight / inSampleSize) > reqHeight && (mHalfWidth / inSampleSize) > reqWidth) {

                inSampleSize *= 2;

            }

        }

        return inSampleSize;

    }

    private Bitmap DecodeBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        final BitmapFactory.Options mOptions = new BitmapFactory.Options();

        mOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(res, resId, mOptions);

        mOptions.inSampleSize = CalculateInSize(mOptions, reqWidth, reqHeight);

        mOptions.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, mOptions);

    }

    @Override
    public void onBackPressed() {
        // Do nothing to lock back softkey
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (!isActivityVisible) {

            isActivityVisible = true;

            finish();

            startActivity(getIntent());

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }

    }

    @Override
    protected void onPause() {

        super.onPause();

        isActivityVisible = false;

    }

}