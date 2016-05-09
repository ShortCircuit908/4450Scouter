package com.orf4450.frcscouter;

/**
 * Represents a task which is executed after data is uploaded
 *
 * @author Caleb Milligan
 *         Created on 1/14/2016
 */
public interface UploadCallback {
	/**
	 * This method is called when an upload is finished, whether or not is successful.
	 * If the upload was successful, the passed parameter will be <code>null</code>.
	 * Otherwise, the error which prevented the upload will be passed.
	 *
	 * @param e The {@link Throwable} thrown during uploading, or {@code null} if the upload was successful
	 */
	void onUploadFinished(Throwable e);
}
