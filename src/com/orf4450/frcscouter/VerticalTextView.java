package com.orf4450.frcscouter;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Display text vertically
 *
 * @author Caleb Milligan
 *         Created on 2/4/2016
 */
public class VerticalTextView extends TextView {
	private boolean top_down;

	public VerticalTextView(Context context) {
		this(context, null);
	}

	public VerticalTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setGravity(int gravity) {
		super.setGravity(gravity);
		if (Gravity.isVertical(gravity) && (gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
			super.setGravity((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.TOP);
			top_down = false;
		}
		else {
			top_down = true;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		TextPaint textPaint = getPaint();
		textPaint.setColor(getCurrentTextColor());
		textPaint.drawableState = getDrawableState();

		canvas.save();

		if (top_down) {
			canvas.translate(getWidth(), 0);
			canvas.rotate(90);
		}
		else {
			canvas.translate(0, getHeight());
			canvas.rotate(-90);
		}


		canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());

		getLayout().draw(canvas);
		canvas.restore();
	}
}