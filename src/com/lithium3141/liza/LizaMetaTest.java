package com.lithium3141.liza;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class LizaMetaTest extends LizaTest {
	
	@Before
	public void _adjustStartupWait() {
		LizaTest.STARTUP_WAIT = 100L;
	}
	
	@Test
	public void testFramework() {
		Assert.assertTrue(true);
	}
	
	@Test
	public void testServer() {
		Assert.assertNotNull("No running Minecraft server found!", Liza.getMinecraftServer());
	}
	
}
