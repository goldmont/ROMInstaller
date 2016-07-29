package com.peppe130.rominstaller.activities;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import java.io.File;

import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.core.Utils;
import com.peppe130.rominstaller.ControlCenter;
import org.sufficientlysecure.htmltextview.HtmlRemoteImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;


@SuppressWarnings("ResultOfMethodCallIgnored")
public class ChangelogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelog_layout);

        Utils.ACTIVITY = this;

        HtmlTextView mHtmlTextView = (HtmlTextView) findViewById(R.id.changelog_html_text);
        assert mHtmlTextView != null;
        mHtmlTextView.setHtml(R.raw.changelog, new HtmlRemoteImageGetter(mHtmlTextView));
    }

    @Override
    protected void onResume() {
        super.onResume();

        Utils.ACTIVITY = this;

        File mROMFolder = new File(Environment.getExternalStorageDirectory().getPath() + "/" + getString(R.string.rom_folder));
        File mSampleZIP = new File(mROMFolder.getPath() + "/" + "Sample.zip");

        if(!mROMFolder.exists()) {
            mROMFolder.mkdirs();
        }

        if(ControlCenter.TRIAL_MODE && !mSampleZIP.exists()) {
            Utils.CopyAssetFolder(getAssets(), "sample", mROMFolder.toString());
        }

        ControlCenter.ROMUtils();

    }

}