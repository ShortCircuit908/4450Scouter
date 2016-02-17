package com.orf4450.frcscouter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * @author Caleb Milligan
 *         Created on 2/4/2016
 */
public class ToggleableRadioButton extends RadioButton {
	public ToggleableRadioButton(Context context) {
		super(context);
	}

	public ToggleableRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ToggleableRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ToggleableRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void toggle() {
		if (isChecked()) {
			if (getParent() instanceof RadioGroup) {
				((RadioGroup) getParent()).clearCheck();
			}
		}
		else {
			setChecked(true);
		}
	}
}
