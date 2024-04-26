package net.op.spectoland;

import java.net.Socket;
import java.util.List;

import net.op.crash.CrashReport;

public class SpectoBugs {
	
	private SpectoBugs() {
	}

	public static void report(CrashReport crashinfo, Socket server) {
		throw new UnsupportedOperationException("In development!");
	}
	
	public static void bulkReport(CrashReport crashinfo, List<Socket> servers) {
		throw new UnsupportedOperationException("In development!");
	}
	
}
