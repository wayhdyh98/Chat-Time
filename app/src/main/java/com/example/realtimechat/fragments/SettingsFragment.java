package com.example.realtimechat.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.realtimechat.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference, rootKey);
    }
}
