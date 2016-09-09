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
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.core.Utils;
import com.peppe130.rominstaller.core.SystemProperties;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.entypo_typeface_library.Entypo;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;


public class SettingsActivity extends AppCompatActivity {

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Utils.InitializeActivity(this);

        setTheme(Utils.FetchTheme(this) == 0 ? R.style.AppTheme_Light : R.style.AppTheme_Dark);

        setContentView(R.layout.activity_settings_layout);

        getFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new SettingsPreferencesFragment())
                .commit();

    }

    @Override
    protected void onResume() {

        super.onResume();

        Utils.EnvironmentChecker(this);

    }

    public static class SettingsPreferencesFragment extends PreferenceFragment {

        @Override
        @SuppressWarnings("ConstantConditions")
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings_fragment);

            Preference DOWNLOAD_CENTER = findPreference("download_center");

            Preference THEME_CHOOSER = findPreference("theme_chooser");

            Preference DEVICE_MODEL = findPreference("device_model");

            Preference BUILD_VERSION = findPreference("build_version");

            Preference ABOUT_PROJECT = findPreference("about_project");

            Preference ABOUT_ROM = findPreference("about_rom");

            Integer mIconColor = ContextCompat.getColor(getActivity(), Utils.FetchIconColor());

            IconicsDrawable mDownloadCenterIcon = new IconicsDrawable(getActivity())
                    .icon(Entypo.Icon.ent_download)
                    .color(mIconColor)
                    .sizeDp(40);

            IconicsDrawable mThemeChooserIcon = new IconicsDrawable(getActivity())
                    .icon(CommunityMaterial.Icon.cmd_theme_light_dark)
                    .color(mIconColor)
                    .sizeDp(40);

            IconicsDrawable mDeviceIcon = new IconicsDrawable(getActivity())
                    .icon(GoogleMaterial.Icon.gmd_phone_android)
                    .color(mIconColor)
                    .sizeDp(40);

            IconicsDrawable mBuildIcon = new IconicsDrawable(getActivity())
                    .icon(CommunityMaterial.Icon.cmd_android)
                    .color(mIconColor)
                    .sizeDp(40);

            IconicsDrawable mProjectInfoIcon = new IconicsDrawable(getActivity())
                    .icon(CommunityMaterial.Icon.cmd_code_braces)
                    .color(mIconColor)
                    .sizeDp(40);

            IconicsDrawable mROMInfoIcon = new IconicsDrawable(getActivity())
                    .icon(CommunityMaterial.Icon.cmd_android_studio)
                    .color(mIconColor)
                    .sizeDp(40);

            DOWNLOAD_CENTER.setIcon(mDownloadCenterIcon);
            DOWNLOAD_CENTER.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    startActivity(new Intent(getActivity(), DownloadActivity.class));

                    return false;

                }
            });

            THEME_CHOOSER.setIcon(mThemeChooserIcon);
            THEME_CHOOSER.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Utils.ThemeChooser();

                    return false;

                }
            });

            DEVICE_MODEL.setIcon(mDeviceIcon);
            DEVICE_MODEL.setSummary(Utils.GetDeviceModel() != null ? Utils.GetDeviceModel() : Build.MODEL);

            BUILD_VERSION.setIcon(mBuildIcon);
            BUILD_VERSION.setSummary(SystemProperties.get("ro.build.display.id"));

            ABOUT_PROJECT.setIcon(mProjectInfoIcon);
            ABOUT_PROJECT.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    getActivity().startActivity(new Intent(getActivity(), InfoActivity.class).putExtra("PREFERENCES", 0));

                    return false;

                }
            });

            ABOUT_ROM.setIcon(mROMInfoIcon);
            ABOUT_ROM.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    getActivity().startActivity(new Intent(getActivity(), InfoActivity.class).putExtra("PREFERENCES", 1));

                    return false;

                }
            });

        }

    }

}
