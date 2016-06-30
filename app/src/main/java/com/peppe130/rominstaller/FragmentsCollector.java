package com.peppe130.rominstaller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.XpPreferenceFragment;
import java.util.ArrayList;


public class FragmentsCollector {

    public static ArrayList<Fragment> mListFragment = new ArrayList<>();

    public static void Setup() {

        if (mListFragment != null) {
            mListFragment.clear();
        }

        mListFragment.add(new FirstFragment());
        mListFragment.add(new SecondFragment());
        mListFragment.add(new ThirdFragment());
        mListFragment.add(new FourthFragment());
        mListFragment.add(new FifthFragment());
        mListFragment.add(new CscFragment());
        mListFragment.add(new BloatFragment());

    }

    public static class FirstFragment extends XpPreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.tab_ui_first_preference);
        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

    public static class SecondFragment extends XpPreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.tab_ui_second_preference);
        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

    public static class ThirdFragment extends XpPreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.tab_ui_third_preference);
        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

    public static class FourthFragment extends XpPreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.tab_ui_fourth_preference);
        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

    public static class FifthFragment extends XpPreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.tab_ui_fifth_preference);
        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

    public static class CscFragment extends XpPreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.tab_ui_csc_preference);
        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

    public static class BloatFragment extends XpPreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.tab_ui_bloat_preference);
        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

}