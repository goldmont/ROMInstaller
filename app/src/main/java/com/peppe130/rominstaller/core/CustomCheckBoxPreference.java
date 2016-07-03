package com.peppe130.rominstaller.core;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.preference.PreferenceViewHolder;
import android.support.v7.preference.TwoStatePreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.TextView;

import com.peppe130.rominstaller.R;


@SuppressWarnings("unused")
@SuppressLint("PrivateResource")
public class CustomCheckBoxPreference extends TwoStatePreference {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomCheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomCheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.Preference_Material_CheckBoxPreference);
    }

    public CustomCheckBoxPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.checkBoxPreferenceStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckBoxPreference, defStyleAttr, defStyleRes);
        setSummaryOn(a.getString(R.styleable.CheckBoxPreference_android_summaryOn));
        setSummaryOff(a.getString(R.styleable.CheckBoxPreference_android_summaryOff));
        setDisableDependentsState(a.getBoolean(R.styleable.CheckBoxPreference_android_disableDependentsState, false));
        a.recycle();
    }

    @Override
    public void onBindViewHolder(final PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        TextView mTitle = (TextView) holder.findViewById(android.R.id.title);
        mTitle.setSingleLine(false);

        View checkboxView = holder.findViewById(android.R.id.checkbox);
        if (checkboxView == null) {
            checkboxView = holder.findViewById(R.id.checkbox);
        }
        if (checkboxView instanceof Checkable) {
            ((Checkable) checkboxView).setChecked(mChecked);
        }

        syncSummaryView(holder);
    }

    private void syncCheckboxView(View view) {
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(mChecked);
        }
    }
}