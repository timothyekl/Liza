package com.lithium3141.liza;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class InputWrapper extends InputStream {
	
	public InputStream defaultStream = System.in;

	@Override
	public int read() throws IOException {
		return this.defaultStream.read();
	}
	
	@Override
	public int available() throws IOException {
		return this.defaultStream.available();
	}
	
	@Override
	public void close() throws IOException {
		this.defaultStream.close();
	}
	
	public void terminate() {
		Main.stdout.println("Called terminate() on input wrapper");
		try {
			this.defaultStream = new ByteArrayInputStream("stop".getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(Main.stderr);
		}
	}

}
