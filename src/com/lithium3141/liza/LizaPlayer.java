package com.lithium3141.liza;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import junit.framework.Assert;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.NetLoginHandler;
import net.minecraft.server.NetServerHandler;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.World;

public class LizaPlayer {
	private EntityPlayer entityPlayer;
	private String name;
	
	public LizaPlayer(String name) {
		this.name = name;
		
		World world = Liza.getMinecraftServer().worlds.get(0);
		this.entityPlayer = new EntityPlayer(Liza.getMinecraftServer(), world, this.name, new ItemInWorldManager(world));
		Socket sock = null;
		try {
			sock = new Socket("localhost", 31415);
		} catch (UnknownHostException e1) {
			Assert.fail("Could not connect to localhost (what?)");
			e1.printStackTrace(Liza.stderr);
		} catch (IOException e1) {
			Assert.fail("Could not handle I/O to localhost");
			e1.printStackTrace(Liza.stderr);
		}
		
		// Just create the server handler - don't need to assign it
		NetLoginHandler nlh = new NetLoginHandler(Liza.getMinecraftServer(), sock, "Liza login handler");
		new NetServerHandler(Liza.getMinecraftServer(), new NetworkManager(sock, "Liza network manager", nlh), this.entityPlayer);
		Liza.getMinecraftServer().serverConfigurationManager.a(entityPlayer);
	}
	
	public EntityPlayer getEntityPlayer() {
		return this.entityPlayer;
	}
	
	public String getName() {
		return this.name;
	}
}
