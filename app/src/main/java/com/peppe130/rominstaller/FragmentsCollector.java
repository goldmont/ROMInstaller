package com.peppe130.rominstaller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.XpPreferenceFragment;
import java.util.ArrayList;


public class FragmentsCollector {

    public static ArrayList<Fragment> mListFragment = new ArrayList<>();

    public static void Setup() {

        mListFragment.add(new FirstFragment());
        mListFragment.add(new SecondFragment());
        mListFragment.add(new ThirdFragment());
        mListFragment.add(new FourthFragment());
        mListFragment.add(new FifthFragment());
        mListFragment.add(new SixthFragment());

    }

    public static class FirstFragment extends XpPreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.first_fragment);
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
            addPreferencesFromResource(R.xml.second_fragment);
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
            addPreferencesFromResource(R.xml.third_fragment);
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
            addPreferencesFromResource(R.xml.fourth_fragment);
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
            addPreferencesFromResource(R.xml.fifth_fragment);
        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

    public static class SixthFragment extends XpPreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.sixth_fragment);
        }

        @Override
        public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
            // Do nothing
        }

    }

}