package com.lithium3141.liza;

import java.lang.reflect.Field;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ThreadServerApplication;

/**
 * Aids in the process of finding a Bukkit. (In reality, does some
 * black magic with threads and reflection to get the running
 * MinecraftServer instance.)
 */
public class BukkitFinder {
	/**
	 * The name given to the thread running the primary server. Used
	 * to distinguish between active threads and locate the one
	 * handling the game, as opposed to e.g. the net listener.
	 */
	public static final String MINECRAFT_SERVER_THREAD_NAME = "Server thread";
	
	/**
	 * The name given to the thread listening on the network port. Used
	 * to distinguish between active threads and locate the one
	 * handling network traffic, as opposed to e.g. the server.
	 */
	public static final String MINECRAFT_NETWORK_THREAD_NAME = "Listen thread";
	
	/**
	 * The obfuscated name used in the ThreadServerApplication class
	 * to refer to the active MinecraftServer instance. Used in
	 * later reflection to locate and fetch that instance.
	 */
	public static final String MINECRAFT_SERVER_FIELD_NAME = "a";
	
	/**
	 * Obtain an array of all active threads in the current Java VM.
	 * Handy for doing quick iteration over threads later to locate
	 * an active MinecraftServer instance.
	 * 
	 * @return an array of all Thread objects currently registered in
	 * the running Java VM.
	 */
	public static Thread[] getAllThreads() {
		ThreadGroup rootGroup = Thread.currentThread( ).getThreadGroup( );
		ThreadGroup parentGroup;
		while ( ( parentGroup = rootGroup.getParent() ) != null ) {
		    rootGroup = parentGroup;
		}
		Thread[] threads = new Thread[ rootGroup.activeCount() ];
		while ( rootGroup.enumerate( threads, true ) == threads.length ) {
		    threads = new Thread[ threads.length * 2 ];
		}
		return threads;
	}
	
	/**
	 * Find the currently running Thread (really an instance of
	 * ThreadServerApplication) responsible for running the Minecraft server.
	 * 
	 * @return the Thread running the currently active Minecraft server
	 */
	public static Thread getActiveServerThread() {
		for(Thread t : getAllThreads()) {
			if(t != null && t.getName() != null && t.getName().equals(MINECRAFT_SERVER_THREAD_NAME)) {
				return t;
			}
		}
		return null;
	}
	
	/**
	 * Find the currently running Thread (really an instance of
	 * NetworkAcceptThread) that is holding the Minecraft server port.
	 */
	public static Thread getActiveNetworkThread() {
		for(Thread t : getAllThreads()) {
			if(t != null & t.getName() != null && t.getName().equals(MINECRAFT_NETWORK_THREAD_NAME)) {
				return t;
			}
		}
		return null;
	}
	
	/**
	 * Locate the MinecraftServer instance currently running in this
	 * Java VM. Relies on both the label given to the server thread
	 * and the obfuscated field name in ThreadServerApplication, as
	 * well as the getAllThreads() method.
	 * 
	 * This method performs some serious black magic with regards to
	 * threading and reflection, and is highly brittle. Use with
	 * caution. Also note that in order to succeed, the server must
	 * have been spawned as a Thread within the same VM from which
	 * getActiveMinecraftServer() is being called.
	 * 
	 * @see ThreadServerApplication
	 * @see BukkitFinder#getAllThreads()
	 * @return the active MinecraftServer instance, or null if such
	 * an instance cannot be found
	 */
	public static MinecraftServer getActiveMinecraftServer() {
		ThreadServerApplication tsa = (ThreadServerApplication)getActiveServerThread();

		// Do reflection black magic
		Class<? extends ThreadServerApplication> c = tsa.getClass();
		Field f;
		try {
			f = c.getDeclaredField(MINECRAFT_SERVER_FIELD_NAME);

			// Make field accessible to this method, then get value
			f.setAccessible(true);
			MinecraftServer mcserver = (MinecraftServer) f.get(tsa);
			return mcserver;
		} catch (Exception e) {
			// Pokemon exception handling, I know, but this whole thing smells anyway
			return null;
		}
	}
}
