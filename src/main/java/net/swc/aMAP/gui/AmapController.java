package net.swc.aMAP.gui;

import java.io.File;

import javax.swing.JOptionPane;

import net.swc.aMAP.Utilities;
import net.swc.aMAP.niftyReg.CommandRunner;
import net.swc.aMAP.niftyReg.ExceptionHandler;

public class AmapController {
	
	public static final File defaultAvgFile = Utilities.getResource("atlas/Kim_OstenRef_ARA_v2_Average_Brain.nii");
	public static final File defaultAtlasFile = Utilities.getResource("atlas/Kim_ORL_ARA_v2.3_Segmentation_Smoothed.nii");
	public static final File testBrain = Utilities.getResource("testBrain/Test1_Ch02_12.5-Zsmoothed.nii");
	private final AmapMainFrame amapMainFrame;
	private Thread currentRunner;
	
	public AmapController(AmapMainFrame amapMainFrame) {
		this.amapMainFrame = amapMainFrame;
	}

	public void runAmap(AmapRunConfig rc) {
		currentRunner = new Thread(new AmapRunner(rc, amapMainFrame));
		currentRunner.start();
	}
	
	public void requestAbort() {
		if (currentRunner!=null && currentRunner.isAlive())
			currentRunner.interrupt();
	}
	
	private class AmapRunner implements Runnable, ExceptionHandler{
		private final AmapRunConfig rc;
		private AmapMainFrame mainFrame;
		private boolean hasCrashed = false;
		private final Object lock = new Object();
		public AmapRunner(AmapRunConfig rc, AmapMainFrame runFrame){
			this.rc = rc;
			this.mainFrame = runFrame;
		}
		public void run() {
			Thread t = null;
			try {
				t = new Thread(new CommandRunner(rc.getAladinCommand(), rc.getErrOut("aladin"), rc.getLogOut("aladin"), this));
				t.start();
				mainFrame.setStatus("Step 1/3 : Running affine registration (reg_aladin)");
				t.join();
				synchronized(lock){if (hasCrashed) return;}
				t = new Thread(new CommandRunner(rc.getf3dCommand(), rc.getErrOut("f3d"), rc.getLogOut("f3d"), this));
				t.start();
				mainFrame.setStatus("Step 2/3 : Running free-form registration (reg_f3d)");
				t.join();
				synchronized(lock){if (hasCrashed) return;}
				t = new Thread(new CommandRunner(rc.getResampleCommand(), rc.getErrOut("resample"), rc.getLogOut("resample"), this));
				t.start();
				mainFrame.setStatus("Step 3/3 : Resampling the atlas segmentation (reg_resample)");
				t.join();
				synchronized(lock){if (hasCrashed) return;}
				mainFrame.done();
				JOptionPane.showMessageDialog(mainFrame, "Segmentation finished!");
			} catch (InterruptedException e) {
				if (t!=null)
					t.interrupt();
				mainFrame.done();
				mainFrame.setStatus("Run Cancelled.");
			}
			
		}
		public void dealWith(Exception e) {
			synchronized(lock){hasCrashed = true;}
			mainFrame.done();
			mainFrame.setStatus("Run Crashed!");
			Utilities.exceptionPopup(mainFrame, e);
		}
		
	}

	public boolean isRunning() {
		return currentRunner!=null && currentRunner.isAlive();
	}

}
