package com.lithium3141.liza;

import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ThreadServerApplication;

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
		
		Main.stdout.println("I mean to be stopped...");
		
		ThreadGroup rootGroup = Thread.currentThread( ).getThreadGroup( );
		ThreadGroup parentGroup;
		while ( ( parentGroup = rootGroup.getParent() ) != null ) {
		    rootGroup = parentGroup;
		}
		Thread[] threads = new Thread[ rootGroup.activeCount() ];
		while ( rootGroup.enumerate( threads, true ) == threads.length ) {
		    threads = new Thread[ threads.length * 2 ];
		}
		for(Thread t : threads) {
			if(t != null && t.getName() != null && t.getName().equals("Server thread")) {
				Main.stdout.println("Found server thread: " + t.toString() + " " + t.getClass());
				ThreadServerApplication tsa = (ThreadServerApplication)t;
				
				// Do reflection black magic
				Class<? extends ThreadServerApplication> c = tsa.getClass();
				Field f;
				try {
					f = c.getDeclaredField("a");
					f.setAccessible(true);
					MinecraftServer mcserver = (MinecraftServer) f.get(tsa);
					mcserver.stop();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
}