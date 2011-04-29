package com.lithium3141.liza;

import java.io.InputStream;
import java.io.PrintStream;

public class CBThread extends Thread {
	public InputStream in;
	public PrintStream out;
	public PrintStream err;
	public String[] options;
	
	public CBThread(InputStream in, PrintStream out, PrintStream err, String[] options) {
		this.in = in;
		this.out = out;
		this.err = err;
		this.options = options;
	}
	
	public void run() {
		System.setOut(out);
		System.setIn(in);
		System.setErr(err);
		org.bukkit.craftbukkit.Main.main(options);
	}
	
	@Override
	public void interrupt() {
		super.interrupt();
		BukkitFinder.getActiveMinecraftServer().stop();
	}
}