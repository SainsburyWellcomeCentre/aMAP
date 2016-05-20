package net.swc.aMAP.niftyReg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CommandRunner implements Runnable {

	private final File logOut;
	private final File errorOut;
	private final Command command;
	private final ExceptionHandler err;
	private BufferedReader logReader;

	public CommandRunner(Command command, File errorOut, File logOut, ExceptionHandler errorHandler){
		this.logOut = logOut;
		this.errorOut = errorOut;
		this.command = command;
		this.err = errorHandler;
	}

	public void run(){
		List<String> commandList = command.getCommand();
		new File(commandList.get(0)).setExecutable(true);
		ProcessBuilder pb = new ProcessBuilder(commandList);
		pb.redirectError(errorOut);
		pb.redirectOutput(logOut);
		Process p = null;
		try {
			p = pb.start();
			int returnVal = p.waitFor();
			if (returnVal!=0){
				err.dealWith(new Exception("Background process returned a non-zero value, please check error logs for details"));
			}
		} catch (IOException e) {
			err.dealWith(e);
		} catch (InterruptedException e) {
		} finally{
			p.destroy();
		}
	}

	public synchronized String getOutputUpdate() throws IOException{
		if (logReader==null){
			try {logReader = new BufferedReader(new FileReader(logOut));}
			catch (FileNotFoundException e) {
				throw new IOException(e);
			}
		}

		StringBuilder sb = new StringBuilder();
		while (true){
			String newLine = logReader.readLine();
			if (newLine==null)
				break;
			sb.append(newLine);
		}
		return sb.toString();	
	}

}
