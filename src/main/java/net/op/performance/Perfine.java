package net.op.performance;

import java.util.logging.Logger;

public class Perfine {

	public static final Logger logger = Logger.getLogger(Perfine.class.getName());

	private Perfine() {
	}
	
	public static void optimizeGame() {
		logger.info("Optimizing game...");
		long start = System.nanoTime();
		
		logger.info("[JGC] Running Java Garbage Collector...");
		System.gc();
		
		logger.info("Optimization process completed!");
		
		int ms = (int) Math.round((System.nanoTime() - start) / 1e6);
		logger.info("Took %dms".formatted(ms));
	}

}
