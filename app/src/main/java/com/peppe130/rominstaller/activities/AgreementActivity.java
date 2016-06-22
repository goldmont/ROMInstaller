package com.peppe130.rominstaller.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.Utils;
import org.sufficientlysecure.htmltextview.HtmlTextView;
import cn.pedant.SweetAlert.SweetAlertDialog;


@SuppressLint("CommitPrefEdits")
public class AgreementActivity extends AppCompatActivity {

    ImageButton CLOSE, AGREE;
    SharedPreferences SP;
    SharedPreferences.Editor mEditor;
    public static AppCompatActivity mActivity;
    Boolean mFirstTime, mDoubleBackToExit = false;

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_layout);

        mActivity = this;

        if (Utils.SHOULD_CLOSE_ACTIVITY) {
            Utils.SHOULD_CLOSE_ACTIVITY = false;
            SplashScreenActivity.mActivity.finish();
        }

        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mEditor = SP.edit();
        AGREE = (ImageButton) findViewById(R.id.agree);
        CLOSE = (ImageButton) findViewById(R.id.not_agree);

        mFirstTime = SP.getBoolean("first_time", true);

        if (!mFirstTime) {
            // Start desired UI using sintax:
            // startActivity(new Intent(AgreementActivity.this, Activity.class));
        }

        AGREE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.putBoolean("first_time", false).apply();
                finish();
                // Start desired UI using sintax:
                // startActivity(new Intent(AgreementActivity.this, Activity.class));
            }
        });

        CLOSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AgreementActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.agreement_dialog_title))
                        .setContentText(getString(R.string.agreement_dialog_message))
                        .showCancelButton(true)
                        .setCancelText(getString(R.string.close_button))
                        .setConfirmText(getString(R.string.ok_button))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                finishAffinity();
                            }
                        });
                sweetAlertDialog.setCancelable(true);
                sweetAlertDialog.setCanceledOnTouchOutside(true);
                sweetAlertDialog.show();
            }
        });

        HtmlTextView mHtmlTextView = (HtmlTextView) findViewById(R.id.agreement_html_text);
        assert mHtmlTextView != null;
        mHtmlTextView.setHtmlFromRawResource(AgreementActivity.this, R.raw.agreement, new HtmlTextView.RemoteImageGetter());
    }*/

    // Custom onCreate with Activity Chooser. Replace it with the above one.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_layout);

        mActivity = this;

        if (Utils.SHOULD_CLOSE_ACTIVITY) {
            Utils.SHOULD_CLOSE_ACTIVITY = false;
            SplashScreenActivity.mActivity.finish();
        }

        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mEditor = SP.edit();
        AGREE = (ImageButton) findViewById(R.id.agree);
        CLOSE = (ImageButton) findViewById(R.id.not_agree);

        final String[] mString = {"Buttons UI", "Swipe UI"};

        AGREE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(AgreementActivity.this)
                        .items(mString)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        Utils.BUTTON_UI = true;
                                        break;
                                    case 1:
                                        Utils.BUTTON_UI = false;
                                        break;
                                    default:
                                        break;
                                }
                                finish();
                                startActivity(new Intent(AgreementActivity.this, MainActivity.class));
                            }
                        })
                        .theme(Theme.LIGHT)
                        .show();
            }
        });

        CLOSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AgreementActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.agreement_dialog_title))
                        .setContentText(getString(R.string.agreement_dialog_message))
                        .showCancelButton(true)
                        .setCancelText(getString(R.string.close_button))
                        .setConfirmText(getString(R.string.ok_button))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                finishAffinity();
                            }
                        });
                sweetAlertDialog.setCancelable(true);
                sweetAlertDialog.setCanceledOnTouchOutside(true);
                sweetAlertDialog.show();
            }
        });

        HtmlTextView mHtmlTextView = (HtmlTextView) findViewById(R.id.agreement_html_text);
        assert mHtmlTextView != null;
        mHtmlTextView.setHtmlFromRawResource(AgreementActivity.this, R.raw.agreement, new HtmlTextView.RemoteImageGetter());
    }

    @Override
    public void onBackPressed() {
        if (mDoubleBackToExit) {
            super.onBackPressed();
            return;
        }

        this.mDoubleBackToExit = true;
        Utils.ToastShort(AgreementActivity.this, getString(R.string.double_back_to_exit));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDoubleBackToExit = false;
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.ACTIVITY = this;
        Utils.SHOULD_CLOSE_ACTIVITY = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.SHOULD_CLOSE_ACTIVITY = true;
    }
}