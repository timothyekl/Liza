package com.lithium3141.liza;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import net.minecraft.server.MinecraftServer;

//import net.minecraft.server.MinecraftServer;

/**
 * Thread used within Liza to run the Minecraft/CraftBukkit server.
 */
public class CBThread extends Thread {
	public InputStream in;
	public PrintStream out;
	public PrintStream err;
	public String[] options = {"-h", "127.0.0.1", "-p", "31415"};
	
	public CBThread() {
		//this.in = in;
		try {
			this.out = new PrintStream(new FileOutputStream(new File("liza-output.txt"), false));
			this.err = new PrintStream(new FileOutputStream(new File("liza-error.txt"), false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void run() {
		System.setOut(this.out);
		System.setIn(this.in);
		System.setErr(this.err);
		
		org.bukkit.craftbukkit.Main.main(options);
	}
	
	@Override
	public void interrupt() {
		super.interrupt();
		MinecraftServer mcServer = BukkitFinder.getActiveMinecraftServer();
		mcServer.stop();
	}
}