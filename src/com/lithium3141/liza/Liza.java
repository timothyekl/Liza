package com.lithium3141.liza;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

import junit.framework.Assert;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;

//import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;

import net.minecraft.server.MinecraftServer;

public class Liza {
	private static CBThread craftBukkitThread;
	private static MinecraftServer minecraftServer;
	
	/**
	 * Original System.out stream
	 */
	public static PrintStream stdout;
	
	/**
	 * Original System.err stream
	 */
	public static PrintStream stderr;
	
	/**
	 * Original System.in stream
	 */
	public static InputStream stdin;
	
	/**
	 * Get the currently running Minecraft server instance. The returned
	 * object is a pure Minecraft server and has minimal CraftBukkit
	 * code associated with it. For the CraftBukkit server equivalent,
	 * see Liza#getCraftServer().
	 * 
	 * The instance is cached across runs; locating the instance is a 
	 * potentially time-consuming operation, especially if the plugin 
	 * under test is heavily multithreaded. One side effect of this is
	 * that calls to Liza#getMinecraftServer() will generally return the
	 * same object across multiple test methods.
	 * 
	 * @return the currently running MinecraftServer instance
	 * 
	 * @see MinecraftServer
	 * @see Liza#getCraftServer()
	 */
	public static MinecraftServer getMinecraftServer() {
		if(minecraftServer == null) {
			minecraftServer = BukkitFinder.getActiveMinecraftServer();
		}
		return minecraftServer;
	}
	
	/**
	 * Get the currently running CraftBukkit server instance. The returned
	 * instance is a CraftBukkit server and has mostly CraftBukkit data
	 * associated with it. For the purer Minecraft server equivalent, see
	 * Liza#getMinecraftServer().
	 * 
	 * The returned instance depends on a call to Liza#getMinecraftServer();
	 * as such, the first call to Liza#getCraftServer() can potentially be
	 * rather time-consuming. However, once fetched the CraftServer is cached
	 * across multiple tests.
	 * 
	 * @return the currently running CraftServer instance
	 * 
	 * @see CraftServer
	 * @see Liza#getMinecraftServer()
	 */
	public static CraftServer getCraftServer() {
		return getMinecraftServer().server;
	}
	
	/**
	 * Perform setup to begin Liza tests. Should be called once
	 * before beginning any testing. At present:
	 *  - Saves standard I/O streams for use later
	 *  - Spawns a new CraftBukkit server instance
	 */
	public static void setUpForTests() {
		stdout = System.out;
		stderr = System.err;
		stdin = System.in;
		
		craftBukkitThread = new CBThread();
		craftBukkitThread.start();
	}
	
	/**
	 * Finalize any testing. Should be called once after completing
	 * all unit tests. At present:
	 *  - Interrupts running CraftBukkit server instance (but does not
	 *    guarantee the instance is killed)
	 *  - Restores standard I/O streams
	 */
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
	public static void loadPluginJar(String jarPath) {
		File jarFile = new File(jarPath);
		if(!jarFile.exists()) {
			Assert.fail("Cannot find file: " + jarPath);
		}
		
		Plugin result = null;
		try {
			result = getCraftServer().getPluginManager().loadPlugin(jarFile);
		} catch(Exception e) {
			Assert.fail("Caught exception loading plugin: " + e.getMessage());
		}
		
		if(result == null) {
			Assert.fail("Plugin loading failed");
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
	
	/**
	 * Enable a loaded plugin by name. Calling this method implies
	 * that the name given maps to a loaded plugin; attempting to
	 * disable a plugin that is not loaded will result in a test
	 * failure.
	 * 
	 * @param name the name of the plugin to enable
	 */
	public static void enablePlugin(String name) {
		Plugin plugin = getCraftServer().getPluginManager().getPlugin(name);
		if(plugin == null) {
			Assert.fail("Could not find plugin to enable: " + name);
		}
		getCraftServer().getPluginManager().enablePlugin(plugin);
	}
	
	/**
	 * Disable a loaded plugin by name. Calling this method implies
	 * that the name given maps to a loaded plugin; attempting to
	 * disable a plugin that is not loaded will result in a test
	 * failure.
	 * 
	 * @param name the name of the plugin to disable
	 */
	public static void disablePlugin(String name) {
		Plugin plugin = getCraftServer().getPluginManager().getPlugin(name);
		if(plugin == null) {
			Assert.fail("Could not find plugin to disable: " + name);
		}
		getCraftServer().getPluginManager().disablePlugin(plugin);
	}
}
