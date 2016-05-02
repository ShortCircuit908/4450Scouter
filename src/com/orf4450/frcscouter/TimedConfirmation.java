package com.orf4450.frcscouter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

/**
 * @author Caleb Milligan
 *         Created on 1/13/2016
 */
public class TimedConfirmation extends Thread {
	private final ProgressBar progress_bar;
	private final Runnable callback;
	public Handler handler;
	private boolean is_holding = false;

	public TimedConfirmation(int millis, ProgressBar progress_bar, Runnable callback) {
		this.progress_bar = progress_bar;
		this.callback = callback;
		progress_bar.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					is_holding = true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					is_holding = false;
				}
				return true;
			}
		});
		progress_bar.setMax(millis);
		start();
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				return true;
			}
		});
		handler.post(new Runnable() {
			@Override
			public void run() {
				while (progress_bar.getProgress() < progress_bar.getMax()) {
					if (is_holding) {
						progress_bar.setProgress(progress_bar.getProgress() + 1);
					}
					else {
						progress_bar.setProgress(0);
					}
					try {
						Thread.sleep(1);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				callback.run();
			}
		});
		Looper.loop();
	}

	public void exit() {
		handler.getLooper().quit();
	}
}
