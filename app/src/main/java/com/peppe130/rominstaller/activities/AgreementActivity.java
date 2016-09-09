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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.core.Utils;
import com.peppe130.rominstaller.ControlCenter;
import com.peppe130.bouncingdialogs.BouncingDialog;
import com.afollestad.materialdialogs.MaterialDialog;
import org.sufficientlysecure.htmltextview.HtmlTextView;

//
public class AgreementActivity extends AppCompatActivity {

    Button AGREE, CLOSE;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Utils.InitializeActivity(this);

        setTheme(Utils.FetchTheme(this) == 0 ? R.style.AppTheme_Light : R.style.AppTheme_Dark);

        setContentView(R.layout.activity_agreement_layout);

        AGREE = (Button) findViewById(R.id.agree);

        CLOSE = (Button) findViewById(R.id.close);

        AGREE.setTextColor(ContextCompat.getColor(this, Utils.FetchAccentColor()));

        CLOSE.setTextColor(ContextCompat.getColor(this, Utils.FetchAccentColor()));

        AGREE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ControlCenter.TRIAL_MODE) {

                    String[] mString = {"Buttons UI", "Swipe UI"};

                    new MaterialDialog.Builder(AgreementActivity.this)
                            .items(mString)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                    dialog.dismiss();

                                    switch (which) {

                                        case 0:

                                            ControlCenter.BUTTON_UI = true;

                                            break;

                                        case 1:

                                            ControlCenter.BUTTON_UI = false;

                                            break;

                                    }

                                    startActivity(new Intent(AgreementActivity.this, MainActivity.class));

                                    finish();

                                }

                            })
                            .show();

                } else {

                    finish();

                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    startActivity(new Intent(AgreementActivity.this, MainActivity.class));

                }

            }

        });

        CLOSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BouncingDialog bouncingDialog = new BouncingDialog(AgreementActivity.this, BouncingDialog.WARNING_TYPE)
                        .title(getString(R.string.agreement_dialog_title))
                        .content(getString(R.string.agreement_dialog_message))
                        .positiveText(getString(R.string.got_it_button))
                        .negativeText(getString(R.string.close_button))
                        .onNegative(new BouncingDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(BouncingDialog bouncingDialog1) {

                                bouncingDialog1.dismiss();

                                finishAffinity();

                            }
                        });
                bouncingDialog.setCancelable(true);
                bouncingDialog.show();

            }

        });

        HtmlTextView mHtmlTextView = (HtmlTextView) findViewById(R.id.agreement_html_text);

        assert mHtmlTextView != null;

        mHtmlTextView.setHtml(R.raw.agreement, null);

    }

    @Override
    protected void onResume() {

        super.onResume();

        Utils.EnvironmentChecker(this);

    }

    @Override
    public void onBackPressed() {

        BouncingDialog bouncingDialog = new BouncingDialog(AgreementActivity.this, BouncingDialog.WARNING_TYPE)
                .title(getString(R.string.agreement_dialog_title))
                .content(getString(R.string.agreement_dialog_message))
                .positiveText(getString(R.string.got_it_button))
                .negativeText(getString(R.string.close_button))
                .onNegative(new BouncingDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(BouncingDialog bouncingDialog1) {

                        bouncingDialog1.dismiss();

                        finishAffinity();

                    }
                });
        bouncingDialog.setCancelable(true);
        bouncingDialog.show();

    }

}
