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
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.core.Utils;
import com.peppe130.rominstaller.BuildConfig;
import com.peppe130.rominstaller.ControlCenter;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;//


public class InfoActivity extends AppCompatActivity {

    public static IconicsDrawable mInfoIcon, mDeveloperIcon, mThemerIcon, mThreadIcon, mGitHubIcon, mDonateIcon, mPlayStoreIcon;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Utils.InitializeActivity(this);

        setTheme(Utils.FetchTheme(this) == 0 ? R.style.AppTheme_Light : R.style.AppTheme_Dark);

        setContentView(R.layout.activity_settings_layout);

        Bundle mBundle = getIntent().getExtras();

        Integer mIconColor = ContextCompat.getColor(this, Utils.FetchIconColor());

        mInfoIcon = new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_info_outline)
                .color(mIconColor)
                .sizeDp(40);

        mDeveloperIcon = new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_developer_board)
                .color(mIconColor)
                .sizeDp(40);

        mThemerIcon = new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_color_lens)
                .color(mIconColor)
                .sizeDp(40);

        mThreadIcon = new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_book_open_page_variant)
                .color(mIconColor)
                .sizeDp(40);

        mGitHubIcon = new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_github_circle)
                .color(mIconColor)
                .sizeDp(40);

        mDonateIcon = new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_attach_money)
                .color(mIconColor)
                .sizeDp(40);

        mPlayStoreIcon = new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_google_play)
                .color(mIconColor)
                .sizeDp(40);

        if (mBundle != null) {

            switch (mBundle.getInt("PREFERENCES")) {

                case 0:

                    setTitle(getString(R.string.about_project_title));

                    getFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, new Utils.InfoProjectPreferencesFragment())
                            .commit();

                    break;

                case 1:

                    setTitle(getString(R.string.about_rom_title));

                    getFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, new InfoROMPreferencesFragment())
                            .commit();

                    break;

            }

        }

    }

    @Override
    protected void onResume() {

        super.onResume();

        Utils.EnvironmentChecker(this);

    }

    public static class InfoROMPreferencesFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.rom_info_section);

            Preference APP_BUILD_NUMBER = findPreference("app_build_number");

            Preference ROM_BUILD_NUMBER = findPreference("rom_build_number");

            Preference ROM_DEVELOPER = findPreference("rom_developer");

            Preference ROM_THEMER = findPreference("rom_themer");

            Preference ROM_THREAD = findPreference("rom_thread");

            Preference ROM_GITHUB = findPreference("rom_github");

            Preference REVIEW_APP = findPreference("review_app");

            APP_BUILD_NUMBER.setIcon(mInfoIcon);
            APP_BUILD_NUMBER.setSummary(BuildConfig.VERSION_NAME);

            ROM_BUILD_NUMBER.setIcon(mInfoIcon);

            ROM_DEVELOPER.setIcon(mDeveloperIcon);
            ROM_DEVELOPER.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    try {

                        ControlCenter.ROMDeveloperInfoAction();

                    } catch (android.content.ActivityNotFoundException anfe) {

                        Utils.ToastLong(Utils.ACTIVITY, getString(R.string.no_application_found));

                    }

                    return false;

                }
            });

            ROM_THEMER.setIcon(mThemerIcon);
            ROM_THEMER.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    try {

                        ControlCenter.ROMThemerInfoAction();

                    } catch (android.content.ActivityNotFoundException anfe) {

                        Utils.ToastLong(Utils.ACTIVITY, getString(R.string.no_application_found));

                    }

                    return false;

                }
            });

            ROM_THREAD.setIcon(mThreadIcon);
            ROM_THREAD.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    try {

                        ControlCenter.ROMThreadInfoAction();

                    } catch (android.content.ActivityNotFoundException anfe) {

                        Utils.ToastLong(Utils.ACTIVITY, getString(R.string.no_application_found));

                    }

                    return false;

                }
            });

            ROM_GITHUB.setIcon(mGitHubIcon);
            ROM_GITHUB.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    try {

                        ControlCenter.ROMGitHubInfoAction();

                    } catch (android.content.ActivityNotFoundException anfe) {

                        Utils.ToastLong(Utils.ACTIVITY, getString(R.string.no_application_found));

                    }

                    return false;

                }
            });

            REVIEW_APP.setIcon(mPlayStoreIcon);
            REVIEW_APP.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    try {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));

                    } catch (android.content.ActivityNotFoundException anfe) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));

                    }

                    return false;

                }
            });

        }

    }

}