package com.demo.example.widget;

import android.content.Context;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.example.R;


public class NumberPicker extends LinearLayout implements View.OnClickListener, View.OnFocusChangeListener, View.OnLongClickListener {
    private static final int DEFAULT_MAX = 31;
    private static final int DEFAULT_MIN = 1;
    protected int mCurrent;
    private boolean mDecrement;
    private NumberPickerButton mDecrementButton;
    private String[] mDisplayedValues;
    protected int mEnd;
    private Formatter mFormatter;
    private final Handler mHandler;
    private boolean mIncrement;
    private NumberPickerButton mIncrementButton;
    private OnChangedListener mListener;
    private final InputFilter mNumberInputFilter;
    protected int mPrevious;
    private final Runnable mRunnable;
    private long mSpeed;
    protected int mStart;
    private final EditText mText;
    public static final Formatter TWO_DIGIT_FORMATTER = new Formatter() {
        final StringBuilder mBuilder = new StringBuilder();
        final java.util.Formatter mFmt = new java.util.Formatter(this.mBuilder);
        final Object[] mArgs = new Object[1];

        @Override
        public String toString(int i) {
            this.mArgs[0] = Integer.valueOf(i);
            this.mBuilder.delete(0, this.mBuilder.length());
            this.mFmt.format("%02d", this.mArgs);
            return this.mFmt.toString();
        }
    };
    private static final char[] DIGIT_CHARACTERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    
    public interface Formatter {
        String toString(int i);
    }

    
    public interface OnChangedListener {
        void onChanged(NumberPicker numberPicker, int i, int i2);
    }

    public NumberPicker(Context context) {
        this(context, null);
    }

    public NumberPicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NumberPicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet);
        this.mRunnable = new Runnable() {
            @Override
            public void run() {
                if (NumberPicker.this.mIncrement) {
                    NumberPicker.this.changeCurrent(NumberPicker.this.mCurrent + 1);
                    NumberPicker.this.mHandler.postDelayed(this, NumberPicker.this.mSpeed);
                } else if (NumberPicker.this.mDecrement) {
                    NumberPicker.this.changeCurrent(NumberPicker.this.mCurrent - 1);
                    NumberPicker.this.mHandler.postDelayed(this, NumberPicker.this.mSpeed);
                }
            }
        };
        this.mSpeed = 300;
        setOrientation(VERTICAL);
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.dialog_number_picker, (ViewGroup) this, true);
        this.mHandler = new Handler();
        NumberPickerInputFilter numberPickerInputFilter = new NumberPickerInputFilter();
        this.mNumberInputFilter = new NumberRangeKeyListener();
        this.mIncrementButton = (NumberPickerButton) findViewById(R.id.increment);
        this.mIncrementButton.setOnClickListener(this);
        this.mIncrementButton.setOnLongClickListener(this);
        this.mIncrementButton.setNumberPicker(this);
        this.mDecrementButton = (NumberPickerButton) findViewById(R.id.decrement);
        this.mDecrementButton.setOnClickListener(this);
        this.mDecrementButton.setOnLongClickListener(this);
        this.mDecrementButton.setNumberPicker(this);
        this.mText = (EditText) findViewById(R.id.timepicker_input);
        this.mText.setOnFocusChangeListener(this);
        this.mText.setFilters(new InputFilter[]{numberPickerInputFilter});
        this.mText.setRawInputType(2);
        if (!isEnabled()) {
            setEnabled(false);
        }
        this.mStart = 1;
        this.mEnd = 31;
    }

    @Override
    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.mIncrementButton.setEnabled(z);
        this.mDecrementButton.setEnabled(z);
        this.mText.setEnabled(z);
    }

    public void setOnChangeListener(OnChangedListener onChangedListener) {
        this.mListener = onChangedListener;
    }

    public void setFormatter(Formatter formatter) {
        this.mFormatter = formatter;
    }

    public void setRange(int i, int i2) {
        this.mStart = i;
        this.mEnd = i2;
        this.mCurrent = i;
        updateView();
    }

    public void setRange(int i, int i2, String[] strArr) {
        this.mDisplayedValues = strArr;
        this.mStart = i;
        this.mEnd = i2;
        this.mCurrent = i;
        updateView();
    }

    public void setCurrent(int i) {
        this.mCurrent = i;
        updateView();
    }

    public void setSpeed(long j) {
        this.mSpeed = j;
    }

    @Override
    public void onClick(View view) {
        validateInput(this.mText);
        if (!this.mText.hasFocus()) {
            this.mText.requestFocus();
        }
        if (R.id.increment == view.getId()) {
            changeCurrent(this.mCurrent + 1);
        } else if (R.id.decrement == view.getId()) {
            changeCurrent(this.mCurrent - 1);
        }
    }

    private String formatNumber(int i) {
        if (this.mFormatter != null) {
            return this.mFormatter.toString(i);
        }
        return String.valueOf(i);
    }

    protected void changeCurrent(int i) {
        if (i > this.mEnd) {
            i = this.mStart;
        } else if (i < this.mStart) {
            i = this.mEnd;
        }
        this.mPrevious = this.mCurrent;
        this.mCurrent = i;
        notifyChange();
        updateView();
    }

    protected void notifyChange() {
        if (this.mListener != null) {
            this.mListener.onChanged(this, this.mPrevious, this.mCurrent);
        }
    }

    protected void updateView() {
        if (this.mDisplayedValues == null) {
            this.mText.setText(formatNumber(this.mCurrent));
        } else {
            this.mText.setText(this.mDisplayedValues[this.mCurrent - this.mStart]);
        }
        this.mText.setSelection(this.mText.getText().length());
    }

    private void validateCurrentView(CharSequence charSequence) {
        int selectedPos = getSelectedPos(charSequence.toString());
        if (selectedPos >= this.mStart && selectedPos <= this.mEnd && this.mCurrent != selectedPos) {
            this.mPrevious = this.mCurrent;
            this.mCurrent = selectedPos;
            notifyChange();
        }
        updateView();
    }

    @Override
    public void onFocusChange(View view, boolean z) {
        if (!z) {
            validateInput(view);
        }
    }

    private void validateInput(View view) {
        String valueOf = String.valueOf(((TextView) view).getText());
        if ("".equals(valueOf)) {
            updateView();
        } else {
            validateCurrentView(valueOf);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        this.mText.clearFocus();
        if (R.id.increment == view.getId()) {
            this.mIncrement = true;
            this.mHandler.post(this.mRunnable);
        } else if (R.id.decrement == view.getId()) {
            this.mDecrement = true;
            this.mHandler.post(this.mRunnable);
        }
        return true;
    }

    public void cancelIncrement() {
        this.mIncrement = false;
    }

    public void cancelDecrement() {
        this.mDecrement = false;
    }

    
    private class NumberPickerInputFilter implements InputFilter {
        private NumberPickerInputFilter() {
        }

        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            if (NumberPicker.this.mDisplayedValues == null) {
                return NumberPicker.this.mNumberInputFilter.filter(charSequence, i, i2, spanned, i3, i4);
            }
            String valueOf = String.valueOf(charSequence.subSequence(i, i2));
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(spanned.subSequence(0, i3)));
            sb.append((Object) valueOf);
            sb.append((Object) spanned.subSequence(i4, spanned.length()));
            String lowerCase = String.valueOf(sb.toString()).toLowerCase();
            for (String str : NumberPicker.this.mDisplayedValues) {
                if (str.toLowerCase().startsWith(lowerCase)) {
                    return valueOf;
                }
            }
            return "";
        }
    }

    
    private class NumberRangeKeyListener extends NumberKeyListener {
        @Override
        public int getInputType() {
            return 2;
        }

        private NumberRangeKeyListener() {
        }

        @Override
        protected char[] getAcceptedChars() {
            return NumberPicker.DIGIT_CHARACTERS;
        }

        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            CharSequence filter = super.filter(charSequence, i, i2, spanned, i3, i4);
            if (filter == null) {
                filter = charSequence.subSequence(i, i2);
            }
            String str = String.valueOf(spanned.subSequence(0, i3)) + ((Object) filter) + ((Object) spanned.subSequence(i4, spanned.length()));
            if ("".equals(str)) {
                return str;
            }
            return NumberPicker.this.getSelectedPos(str) > NumberPicker.this.mEnd ? "" : filter;
        }
    }

    
    public int getSelectedPos(String str) {
        if (this.mDisplayedValues == null) {
            return Integer.parseInt(str);
        }
        for (int i = 0; i < this.mDisplayedValues.length; i++) {
            str = str.toLowerCase();
            if (this.mDisplayedValues[i].toLowerCase().startsWith(str)) {
                return this.mStart + i;
            }
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return this.mStart;
        }
    }

    public int getCurrent() {
        return this.mCurrent;
    }
}
