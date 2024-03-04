package net.opencraft.client;

public final class Main {

	public static void main(String[] args) {
		Thread.currentThread().setName("Main Thread");
		new Thread(new Game()).start();
	}

}