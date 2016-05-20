package net.swc.aMAP.niftyReg;

public class CrashOnException implements ExceptionHandler {

	public void dealWith(Exception e) {
		throw new Error(e);
	}

}
