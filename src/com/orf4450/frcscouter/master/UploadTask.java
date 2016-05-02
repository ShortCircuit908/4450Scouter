package com.orf4450.frcscouter.master;

/**
 * @author Caleb Milligan
 *         Created on 2/22/2016
 */
public abstract class UploadTask implements Runnable {
	protected final Callback callback;
	protected Throwable error = null;

	public UploadTask(Callback callback) {
		this.callback = callback;
	}

	public abstract static class Callback {
		public abstract void onUploadFinished(Throwable e);
	}
}
