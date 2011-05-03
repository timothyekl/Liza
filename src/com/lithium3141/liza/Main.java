package com.lithium3141.liza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Small main stub for running Liza directly. Not generally used,
 * except for debugging in core Liza functionality (e.g. CBThread
 * class).
 * 
 * @see Liza
 */
public class Main {

	public static CBThread serverThread;
	
	public static PrintStream stdout;
	public static PrintStream stderr;
	public static InputStream stdin;
	
	/**
	 * Launch the test runner semi-interactively.
	 * @param args Command-line arguments
	 * @throws IOException if System.in is broken
	 */
	public static void main(String[] args) throws IOException {
		// Save system streams
		stdout = System.out;
		stderr = System.err;
		stdin = System.in;
		
		// Run CB
		serverThread = new CBThread();
		serverThread.start();
		
		// Sleep for thread
		while(true) {
			String input = (new BufferedReader(new InputStreamReader(stdin))).readLine();
			stdout.println(input);
			if(input.startsWith("stop")) {
				stdout.println("Stopping Liza main process...");
				serverThread.interrupt();
				System.exit(0);
			}
		}
	}

}
