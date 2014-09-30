/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.deviceinfo;

import android.app.ActionBar;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.view.Gravity;
import android.widget.Switch;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

/**
 * USB storage settings.
 */
public class SdExtSettings extends SettingsPreferenceFragment {

    private static final String TAG = "SdExtSettings";
    private static final String enableFlag = "enable";
    private static final String disableFlag = "disable";

    private static final String[] BIND_KEYS_APP = {
        "bind_app"
        , "bind_app_asec"
        , "bind_app_lib"
        , "bind_app_private"
        , "bind_dalvik_cache"
        , "bind_data"
    };

    private static final String[] preferenceKeys = {
        "persist.bind.app"
        , "persist.bind.app-asec"
        , "persist.bind.app-lib"
        , "persist.bind.app-private"
        , "persist.bind.dalvik-cache"
        , "persist.bind.data"
    };

    private static final int preferenceCount = 6;

    private List<CheckBoxPreference> mCheckBoxes;
    private SdExtSettingEnabler mSdExtSettingEnabler;


    private PreferenceScreen createPreferenceHierarchy() {

        PreferenceScreen root = getPreferenceScreen();
        if (root != null) {
            root.removeAll();
        }
        addPreferencesFromResource(R.xml.sdext_settings);
        root = getPreferenceScreen();

        mCheckBoxes = new ArrayList<CheckBoxPreference>();

        for (int idx = 0; idx < preferenceCount; ++idx)
        {
            CheckBoxPreference checkBox = (CheckBoxPreference)root.findPreference( BIND_KEYS_APP[idx] );
            mCheckBoxes.add(checkBox);
            if (checkBox != null) {
                boolean flagIsSet = SystemProperties.get( preferenceKeys[idx], disableFlag).equals(enableFlag);
                String clickValue =  (flagIsSet ? enableFlag : disableFlag);

                Log.d(TAG, "orig - " + preferenceKeys[idx] + " -> " + SystemProperties.get(preferenceKeys[idx], disableFlag) );
                Log.d(TAG, preferenceKeys[idx] + " -> " + clickValue );
                checkBox.setChecked(flagIsSet);
                checkBox.setEnabled(false);
            }
        }

        Activity activity = getActivity();
        ActionBar actionbar = activity.getActionBar();
        Switch actionBarSwitch = new Switch(activity);

        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,  ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(actionBarSwitch, new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT
                        , Gravity.CENTER_VERTICAL | Gravity.RIGHT));

        mSdExtSettingEnabler = new SdExtSettingEnabler(getActivity(), actionBarSwitch, mCheckBoxes);

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();

        createPreferenceHierarchy();
        mSdExtSettingEnabler.resume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mSdExtSettingEnabler.pause();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        String clickValue =  ((CheckBoxPreference)preference).isChecked() ? enableFlag : disableFlag;
        for (int idx = 0; idx < preferenceCount; ++idx)
        {
            if ( preference == mCheckBoxes.get(idx) ) {
                // SystemProperties.set("persist.bind.app", clickValue);
                Log.d(TAG, preferenceKeys[idx] + " -> " + clickValue );
                break;
            }
        }

        return true;
    }
}
