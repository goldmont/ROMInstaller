package com.peppe130.rominstaller.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.core.Utils;
import org.sufficientlysecure.htmltextview.HtmlTextView;


public class ChangelogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelog_layout);

        Utils.ACTIVITY = this;

        HtmlTextView mHtmlTextView = (HtmlTextView) findViewById(R.id.changelog_html_text);
        assert mHtmlTextView != null;
        mHtmlTextView.setHtmlFromRawResource(ChangelogActivity.this, R.raw.changelog, new HtmlTextView.RemoteImageGetter());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.ACTIVITY = this;
    }

}