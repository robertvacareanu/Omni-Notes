package it.feio.android.omninotes;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import it.feio.android.checklistview.utils.DensityUtil;

public class FontPickerListPreference extends ListPreference {
    private int clickedValue;
    private static int x = 0;
    private static float initialValue = 0f;

    public FontPickerListPreference(Context context) {
        super(context);
    }

    public FontPickerListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        Log.v("FPLP", "onPrepareDialogBuilder");
        if (getEntries() == null || getEntryValues() == null) {
            super.onPrepareDialogBuilder(builder);
            return;
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.preference_font_picker, getEntries()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                CheckedTextView view = (CheckedTextView) convertView;
                if (view == null) {
                    view = (CheckedTextView) View.inflate(getContext(), R.layout.preference_font_picker, null);
                }
                float currentSize = DensityUtil.pxToDp(view.getTextSize(), getContext());

                if (initialValue == 0f) {
                    initialValue = currentSize;
                }
                float offset = getContext().getResources().getIntArray(
                        R.array.text_size_offset)[position];
                Log.v("FPLP", x++ + "Get view of position: " + position + " with text: " + getEntries()[position] + " having current size: " + currentSize + " offset: " + offset + " " + getCount() + " " + initialValue);
                view.setText(getEntries()[position]);
                view.setTextSize(initialValue + offset);
                return view;
            }
        };
        clickedValue = findIndexOfValue(getValue());
        builder.setSingleChoiceItems(adapter, clickedValue, (DialogInterface dialogInterface, int i) -> {
            clickedValue = i;
            FontPickerListPreference.this.onClick(dialogInterface, DialogInterface.BUTTON_POSITIVE);
            dialogInterface.dismiss();

        });
        builder.setPositiveButton(null, null);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        Log.v("FPLP", "Dialog closed with: " + positiveResult + " " + clickedValue);
        if (positiveResult && clickedValue >= 0 && getEntryValues() != null) {
            String value = getEntryValues()[clickedValue].toString();
            if (callChangeListener(value)) {
                setValue(value);
            }
        }
    }
}


