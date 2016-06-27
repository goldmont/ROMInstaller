package com.peppe130.rominstaller.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.entypo_typeface_library.Entypo;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.peppe130.rominstaller.BuildConfig;
import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.Utils;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_layout);

        getFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new SettingsPreferencesFragment())
                .commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.ACTIVITY = this;
    }

    public static class SettingsPreferencesFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings_preference);

            Preference DOWNLOAD_CENTER = findPreference("download_center");
            Preference APP_BUILD_NUMBER = findPreference("app_build_number");
            Preference APP_DEVELOPER = findPreference("app_developer");
            Preference APP_THEMER = findPreference("app_themer");
            Preference APP_GITHUB = findPreference("app_github");
            Preference ROM_BUILD_NUMBER = findPreference("rom_build_number");
            Preference ROM_DEVELOPER = findPreference("rom_developer");
            Preference ROM_THEMER = findPreference("rom_themer");
            Preference ROM_XDA_THREAD = findPreference("rom_xda_thread");
            Preference REVIEW_APP = findPreference("review_app");
            Preference ALL_MY_APPS = findPreference("all_my_apps");

            IconicsDrawable mDownloadCenterIcon = new IconicsDrawable(getActivity())
                    .icon(Entypo.Icon.ent_download)
                    .color(Color.WHITE)
                    .sizeDp(40);

            IconicsDrawable mXDAIcon = new IconicsDrawable(getActivity())
                    .icon(CommunityMaterial.Icon.cmd_book_open_page_variant)
                    .color(Color.WHITE)
                    .sizeDp(40);

            IconicsDrawable mInfoIcon = new IconicsDrawable(getActivity())
                    .icon(GoogleMaterial.Icon.gmd_info_outline)
                    .color(Color.WHITE)
                    .sizeDp(40);

            IconicsDrawable mDeveloperIcon = new IconicsDrawable(getActivity())
                    .icon(GoogleMaterial.Icon.gmd_developer_board)
                    .color(Color.WHITE)
                    .sizeDp(40);

            IconicsDrawable mThemerIcon = new IconicsDrawable(getActivity())
                    .icon(GoogleMaterial.Icon.gmd_color_lens)
                    .color(Color.WHITE)
                    .sizeDp(40);

            IconicsDrawable mGitHubIcon = new IconicsDrawable(getActivity())
                    .icon(CommunityMaterial.Icon.cmd_github_circle)
                    .color(Color.WHITE)
                    .sizeDp(40);

            IconicsDrawable mPlayStoreIcon = new IconicsDrawable(getActivity())
                    .icon(CommunityMaterial.Icon.cmd_google_play)
                    .color(Color.WHITE)
                    .sizeDp(40);

            DOWNLOAD_CENTER.setIcon(mDownloadCenterIcon);
            DOWNLOAD_CENTER.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), DownloadActivity.class));
                    return false;
                }
            });

            APP_BUILD_NUMBER.setIcon(mInfoIcon);
            APP_BUILD_NUMBER.setSummary(BuildConfig.VERSION_NAME);

            APP_DEVELOPER.setIcon(mDeveloperIcon);
            APP_DEVELOPER.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String[] mSocial = {"Google+", "Twitter"};
                    String[] mLinks = {"http://google.com/+PeppeMontuoro", "https://twitter.com/PeppeMontuoro"};
                    Utils.FollowMeDialog(mSocial, mLinks);
                    return false;
                }
            });

            APP_THEMER.setIcon(mThemerIcon);
            APP_THEMER.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String[] mSocial = {"Google+", "Twitter"};
                    String[] mLinks = {"http://google.com/+MRLOUDT_ONE", "https://twitter.com/MR_LOUD_T_ONE"};
                    Utils.FollowMeDialog(mSocial, mLinks);
                    return false;
                }
            });

            APP_GITHUB.setIcon(mGitHubIcon);
            APP_GITHUB.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Uri mUri = Uri.parse("https://github.com/peppe130/ROMInstaller");
                    startActivity(new Intent(Intent.ACTION_VIEW, mUri));
                    return false;
                }
            });

            ROM_BUILD_NUMBER.setIcon(mInfoIcon);

            ROM_DEVELOPER.setIcon(mDeveloperIcon);
            ROM_DEVELOPER.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // Do something
                    return false;
                }
            });

            ROM_THEMER.setIcon(mThemerIcon);
            ROM_THEMER.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // Do something
                    return false;
                }
            });

            ROM_XDA_THREAD.setIcon(mXDAIcon);
            ROM_XDA_THREAD.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Uri mUri = Uri.parse("http://forum.xda-developers.com/galaxy-s4/i9505-develop/rom-osiris-rom-v1-0-t3147053");
                    startActivity(new Intent(Intent.ACTION_VIEW, mUri));
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

            ALL_MY_APPS.setIcon(mPlayStoreIcon);
            ALL_MY_APPS.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Giuseppe Montuoro")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:Giuseppe Montuoro")));
                    }
                    return false;
                }
            });

        }

    }

}