package net.swc.aMAP.niftyReg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.swc.aMAP.parameters.DoubleParameter;
import net.swc.aMAP.parameters.FileParameter;
import net.swc.aMAP.parameters.IntParameter;

import org.testng.annotations.Test;

//import static org.testng.Assert.*;

public class TestCommands {
	public static final File logOut = new File("src/test/resources/logOut.txt");
	public static final File errOut = new File("src/test/resources/errOut.txt");
	public static final File avgBrain = new File("src/main/resources/OstenRef_ARA_v2_rotatedFlipZXYZ12.5.nii");
	public static final File atlas = new File("src/main/resources/ORL_ARA_v2.3_bi_rotated-doubleXYGaussSmooth0.5iter2DownScaled.nii");
	public static final File testBrain = new File("src/test/resources/MV_Ntsr1_165_StitchedImagesPaths_Ch02_12.5-ZsmoothedAffRegNoTrans.nii");
	public static final File affOut = new File("src/test/resources/aff.txt");
	public static final File tmpResOut = new File("src/test/resources/res.nii.gz");
	public static final File cpp = new File("src/test/resources/cpp.nii");
	
	@Test
	public void testEmptyCommands() {
		List<NiftyCommand> commands = new ArrayList<NiftyCommand>();
		commands.add(new RegAladin());
		commands.add(new RegF3d());
		commands.add(new RegResample());
		for (NiftyCommand nc : commands){
			new CommandRunner(nc, errOut, logOut, new CrashOnException()).run();;
		}
	}
	
	@Test
	public void testFullTransform(){
		
		NiftyCommand command = new RegAladin(null);
		addAladinF3dParameters(command);
		new CommandRunner(command, errOut, logOut, new CrashOnException()).run();
		
		command = new RegF3d(null);
		addAladinF3dParameters(command);
		command.setParameter(new IntParameter("-sx", -10));
		command.setParameter(new DoubleParameter("-be", 0.95));
		command.setParameter(new FileParameter("-cpp", cpp));
		new CommandRunner(command, errOut, logOut, new CrashOnException()).run();
	}
	
	public void addAladinF3dParameters(NiftyCommand command){
		command.setParameter(new FileParameter("-ref", testBrain));
		command.setParameter(new FileParameter("-flo", avgBrain));
		command.setParameter(new FileParameter("-aff", affOut));
		command.setParameter(new IntParameter("-ln", 6));
		command.setParameter(new IntParameter("-lp", 4));
		command.setParameter(new FileParameter("-res", tmpResOut));
	}
}
