package com.yly.trainsystem.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.yly.trainsystem.R;
import com.yly.trainsystem.SharedUtils;
import com.yly.trainsystem.ui.login.LoginActivity;

import java.util.Objects;

public class MyFragment extends PreferenceFragmentCompat {

    private int CODE_LOGIN = 1;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.my_preferences, rootKey);
       Preference preference = getPreferenceManager().findPreference(getString(R.string.key_login));
       setUserName();

        if (preference != null) {
            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, CODE_LOGIN);
                    return true;
                }
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_LOGIN ) {
            setUserName();
        }
    }

    private void setUserName() {
        String userName = SharedUtils.getInstance(Objects.requireNonNull(getActivity())).getString("userName");
        if (userName != null && !TextUtils.isEmpty(userName)) {
            Preference preference = getPreferenceManager().findPreference(getString(R.string.key_login));
            Objects.requireNonNull(preference).setTitle(userName);

        }
    }
}
