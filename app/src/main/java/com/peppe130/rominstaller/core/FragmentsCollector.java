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

package com.peppe130.rominstaller.core;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.XpPreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.ControlCenter;


public class FragmentsCollector {

    private static ArrayList<Fragment> FRAGMENTS_LIST = new ArrayList<>();

    public static void Setup() {

        if (FragmentsCollector.FRAGMENTS_LIST != null) {

            FragmentsCollector.FRAGMENTS_LIST.clear();

        }

        if (ControlCenter.SHOULD_SHOW_HOME_FRAGMENT) {

            FRAGMENTS_LIST.add(new HomeFragment());

        }

        for (int mInt = 0; mInt < ControlCenter.FRAGMENTS_RESOURCES.length; mInt++) {

            FRAGMENTS_LIST.add(PreferencesFragment.newInstance(ControlCenter.FRAGMENTS_RESOURCES[mInt]));

        }

    }

    @NonNull
    public static Integer GetCount() {

        return FRAGMENTS_LIST.size();

    }

    public static Fragment GetFragment(Integer position) {

        return FRAGMENTS_LIST.get(position);

    }

    public static class HomeFragment extends XpPreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {

            super.onActivityCreated(savedInstanceState);

            ControlCenter.HomeFragment(getActivity(), this, getView());

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            return inflater.inflate(R.layout.home_fragment_layout, container, false);

        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

    public static class PreferencesFragment extends XpPreferenceFragment {

        public static PreferencesFragment newInstance(Integer resource) {

            PreferencesFragment mPreferencesFragment = new PreferencesFragment();

            Bundle mBundle = new Bundle();

            mBundle.putInt("RESOURCE", resource);

            mPreferencesFragment.setArguments(mBundle);

            return mPreferencesFragment;

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            addPreferencesFromResource(getArguments().getInt("RESOURCE"));

        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

            super.onViewCreated(view, savedInstanceState);

            getListView().setBackgroundColor(Color.TRANSPARENT);

        }
    }

}