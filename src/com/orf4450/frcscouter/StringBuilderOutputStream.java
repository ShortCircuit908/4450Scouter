package com.orf4450.frcscouter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A dummy OutputStream which appends content to a string
 *
 * @author Caleb Milligan
 *         Created on 2/19/2016
 */
public class StringBuilderOutputStream extends OutputStream {
	StringBuilder builder = new StringBuilder();

	@Override
	public void write(int cur_byte) throws IOException {
		builder.append((char) cur_byte);
	}

	@Override
	public String toString() {
		return builder.toString();
	}
}
