package com.lithium3141.liza;

import java.io.InputStream;
import java.io.PrintStream;

import net.minecraft.server.MinecraftServer;

public class Liza {
	private static CBThread craftBukkitThread;
	private static MinecraftServer minecraftServer;
	
	public static PrintStream stdout;
	public static PrintStream stderr;
	public static InputStream stdin;
	
	public static MinecraftServer getMinecraftServer() {
		if(minecraftServer == null) {
			minecraftServer = BukkitFinder.getActiveMinecraftServer();
		}
		return minecraftServer;
	}
	
	public static void setUpForTests() {
		stdout = System.out;
		stderr = System.err;
		stdin = System.in;
		
		craftBukkitThread = new CBThread();
		craftBukkitThread.start();
	}
	
	public static void tearDownFromTests() {
		craftBukkitThread.interrupt();
		
		System.setOut(stdout);
		System.setErr(stderr);
		System.setIn(stdin);
	}
}
