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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.core.Utils;
import org.sufficientlysecure.htmltextview.HtmlTextView;


public class ChangelogActivity extends AppCompatActivity {

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Utils.InitializeActivity(this);

        setTheme(Utils.FetchTheme(this) == 0 ? R.style.AppTheme_Light : R.style.AppTheme_Dark);

        setContentView(R.layout.activity_changelog_layout);

        HtmlTextView mHtmlTextView = (HtmlTextView) findViewById(R.id.changelog_html_text);

        assert mHtmlTextView != null;

        mHtmlTextView.setHtml(R.raw.changelog, null);

    }

    @Override
    protected void onResume() {

        super.onResume();

        Utils.EnvironmentChecker(this);

    }

}
