package com.doku.android.samplesdkapps.main;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import androidx.fragment.app.DialogFragment;
import com.doku.android.samplesdkapps.R;

/**
 * Created by dedye on 04,June,2020
 */
public class SettingPage extends DialogFragment implements View.OnClickListener {

    private Context parent;
    private EditText edittextSettingpageClientid;
    private EditText edittextSettingpageSharedkey;
    private Boolean isProduction;
    private Boolean activeResultPage;
    private RadioButton radiobuttonSettingpageSunbox;
    private RadioButton radiobuttonSettingpageProduction;
    private RadioButton radiobuttonSettingpageYes;
    private RadioButton radiobuttonSettingpageNo;

    public interface SettingDialogListener {
        void onFinishEditDialog(String clientId, String sharedKey, Boolean isProduction, Boolean activeResultPage);
    }

    public void setParent(Context fragment) {
        this.parent = fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settingpage_fragment, container);
        edittextSettingpageClientid = view.findViewById(R.id.edittext_settingpage_clientid);
        edittextSettingpageSharedkey = view.findViewById(R.id.edittext_settingpage_sharedkey);

        radiobuttonSettingpageSunbox = view.findViewById(R.id.radiobutton_settingpage_sunbox);
        radiobuttonSettingpageProduction = view.findViewById(R.id.radiobutton_settingpage_production);

        radiobuttonSettingpageYes = view.findViewById(R.id.radiobutton_settingpage_yes);
        radiobuttonSettingpageNo = view.findViewById(R.id.radiobutton_settingpage_no);

        Button buttonSettingpageOk = view.findViewById(R.id.button_settingpage_ok);
        buttonSettingpageOk.setOnClickListener(this);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        return view;
    }

    private void checkValidatedSubmit() {
        View focusView = null;
        edittextSettingpageClientid.setError(null);
        edittextSettingpageSharedkey.setError(null);

        if (radiobuttonSettingpageSunbox.isChecked()) {
            isProduction = false;
        } else if (radiobuttonSettingpageProduction.isChecked()) {
            isProduction = true;
        }

        if (radiobuttonSettingpageYes.isChecked()) {
            activeResultPage = false;
        } else if (radiobuttonSettingpageNo.isChecked()) {
            activeResultPage = true;
        }

        String clientId = edittextSettingpageClientid.getText().toString();
        String sharedKey = edittextSettingpageSharedkey.getText().toString();

        boolean cancel = false;

        if (TextUtils.isEmpty(clientId)) {
            edittextSettingpageClientid.setError(getString(R.string.error_field_required));
            focusView = edittextSettingpageClientid;
            cancel = true;
        }

        if (TextUtils.isEmpty(sharedKey)) {
            edittextSettingpageSharedkey.setError(getString(R.string.error_field_required));
            focusView = edittextSettingpageSharedkey;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            SettingDialogListener activity = (SettingDialogListener) parent;
            activity.onFinishEditDialog(clientId, sharedKey, isProduction, activeResultPage);
            getDialog().dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        checkValidatedSubmit();
    }
}
