package com.lithium3141.liza;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class LizaTest {
	/**
	 * Amount of time to wait for the Minecraft server to start and stop, in milliseconds
	 */
	public static long SERVER_WAIT = 1000L;
	
	private static void waitForServer() {
		try {
			Thread.sleep(SERVER_WAIT);
		} catch(InterruptedException e) {
			Main.stderr.println("Error letting Minecraft server start up!");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	@BeforeClass
	public static void _setUp() {
		Liza.setUpForTests();
		waitForServer();
	}
	
	@Test
	public void _testRAM() {
		Assert.assertTrue("Minecraft server requires at least 1024MB to run; edit your runtime", Runtime.getRuntime().maxMemory() / 1024L / 1024L > 1000L);
	}
	
	@AfterClass
	public static void _tearDown() {
		Liza.tearDownFromTests();
	}
}
