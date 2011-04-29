package com.lithium3141.liza;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

import net.minecraft.server.MinecraftServer;

public class CBThread extends Thread {
	public InputStream in;
	public PrintStream out;
	public PrintStream err;
	public String[] options = {"-h", "localhost", "-p", "31415"};
	
	public CBThread() {
		this.in = in;
		try {
			this.out = new PrintStream(new File("liza-output.txt"));
			this.err = new PrintStream(new File("liza-error.txt"));
		} catch (FileNotFoundException e) {
			Main.stderr.println("Could not assign new output streams for Liza!");
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
		if(mcServer != null) {
			mcServer.stop();
		}
	}
}