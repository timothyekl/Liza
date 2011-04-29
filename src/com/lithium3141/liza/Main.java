package com.lithium3141.liza;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;


public class Main {

	public static CBThread serverThread;
	
	final static String[] serverOptions = {"-h", "localhost", "-p", "31415"};
	
	public static PrintStream stdout;
	public static PrintStream stderr;
	public static InputStream stdin;
	
	public static PrintStream serverOut;
	public static PrintStream serverErr;
	public static InputStream serverIn;
	
	public static boolean shouldTerminate;
	
	/**
	 * Launch the test runner.
	 * @param args Command-line arguments
	 * @throws IOException if System.in is broken
	 */
	public static void main(String[] args) throws IOException {
		// Save system streams
		stdout = System.out;
		stderr = System.err;
		stdin = System.in;
		
		// Set up streams
		try {
			serverOut = new PrintStream(new File("test-output.txt"));
			serverErr = new PrintStream(new File("test-error.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		serverIn = System.in;
		
		// Run CB
		serverThread = new CBThread(serverIn, serverOut, serverErr, serverOptions);
		serverThread.start();
		
		// Check streams
		System.out.println("test");
		
		// Sleep for thread
		while(true) {
			String input = (new BufferedReader(new InputStreamReader(stdin))).readLine();
			stdout.println(input);
			if(input.startsWith("stop")) {
				stdout.println("Killing...");
				serverThread.interrupt();
				System.exit(0);
			}
		}
	}

}
