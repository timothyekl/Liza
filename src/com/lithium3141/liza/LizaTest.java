package com.lithium3141.liza;

import org.junit.After;
import org.junit.Before;

public class LizaTest {
	/**
	 * Amount of time to wait for the Minecraft server to start, in milliseconds
	 */
	public static long STARTUP_WAIT = 1000L;
	
	@Before
	public void _setUp() {
		Liza.setUpForTests();

		try {
			Thread.sleep(STARTUP_WAIT);
		} catch(InterruptedException e) {
			Main.stderr.println("Error letting Minecraft server start up!");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	@After
	public void _tearDown() {
		Liza.tearDownFromTests();
	}
}
