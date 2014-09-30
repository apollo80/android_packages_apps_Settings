package com.android.settings.deviceinfo;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import java.util.List;

public class SdExtSettingEnabler implements OnCheckedChangeListener {

        protected final Context mContext;
        private Switch mSwitch;
        List<CheckBoxPreference> mCheckBoxes;

        public SdExtSettingEnabler(Context context, Switch swtch, List<CheckBoxPreference> checkBoxes) {
                mContext = context;
                setSwitch(swtch);
                mCheckBoxes = checkBoxes;
        }

        public void setSwitch(Switch swtch) {
                if (mSwitch == swtch)
                        return;

                if (mSwitch != null)
                        mSwitch.setOnCheckedChangeListener(null);
                mSwitch = swtch;
                mSwitch.setOnCheckedChangeListener(this);

                mSwitch.setChecked(false);
        }

        public void onCheckedChanged(CompoundButton view, boolean isChecked) {
            for(CheckBoxPreference check: mCheckBoxes) {
                check.setSelectable(isChecked);
                check.setEnabled(isChecked);
            }
        }

        public void resume() {
                mSwitch.setOnCheckedChangeListener(this);
                mSwitch.setChecked(false);
        }

        public void pause() {
                mSwitch.setOnCheckedChangeListener(null);
        }
}

