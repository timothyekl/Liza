package com.lithium3141.liza;


public class Main {

	public static Thread serverThread;
	
	final static String[] serverOptions = {"-h", "localhost", "-p", "31415"};
	
	/**
	 * Launch the test runner.
	 * @param args Command-line arguments
	 */
	public static void main(String[] args) {
		serverThread = new Thread() {
			public void run() {
				org.bukkit.craftbukkit.Main.main(serverOptions);
			}
		};
		serverThread.start();
		
		while(serverThread.isAlive()) {
			try {
				Thread.sleep(1000);
			} catch(InterruptedException ie) { }
		}
	}

}
