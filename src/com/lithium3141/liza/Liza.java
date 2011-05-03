package com.lithium3141.liza;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;

//import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;


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

	/**
	 * Load a plugin contained in the given File object into the currently
	 * running Minecraft server. The File should match the type of plugin
	 * the running server expects; in almost all cases, this will be a
	 * compiled JAR file.
	 * 
	 * @param file the JAR file to load
	 * @throws FileNotFoundException if the given file path cannot be located
	 * @throws UnknownDependencyException if the underlying call to the PluginManager fails
	 * @throws InvalidDescriptionException if the underlying call to the PluginManager fails
	 * @throws InvalidPluginException if the underlying call to the PluginManager fails
	 */
	public static void loadPluginJar(String jarPath) throws FileNotFoundException, InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
		File jarFile = new File(jarPath);
		if(!jarFile.exists()) {
			throw new FileNotFoundException("Cannot find file: " + jarPath);
		}
		
		Plugin result = getMinecraftServer().server.getPluginManager().loadPlugin(jarFile);
		if(result == null) {
			Liza.stderr.println("Choked");
		} else {
			Liza.stdout.println("Got plugin result");
		}
	}
	
	/**
	 * Load a plugin from an instantiated Plugin class. Unsupported and unimplemented.
	 * @param pluginInstance
	 * @throws UnsupportedOperationException
	 */
	public static void loadPlugin(Plugin pluginInstance) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Loading plugin instances from runtime Java objects not supported");
		
		/*
		CraftServer craftServer = getMinecraftServer().server;
		
		Class<? extends CraftServer> craftServerClass = craftServer.getClass();
		try {
			Method loadPluginMethod = craftServerClass.getDeclaredMethod("loadPlugin", Plugin.class);
			loadPluginMethod.setAccessible(true);
			loadPluginMethod.invoke(craftServer, pluginInstance);
		} catch(InvocationTargetException e) {
			e.getTargetException().printStackTrace(stderr);
			e.printStackTrace(stderr);
		} catch(Exception e) {
			e.printStackTrace(stderr);
		}
		*/
	}
}
