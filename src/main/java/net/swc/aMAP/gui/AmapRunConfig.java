package net.swc.aMAP.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.swc.aMAP.niftyReg.NiftyCommand;
import net.swc.aMAP.niftyReg.RegAladin;
import net.swc.aMAP.niftyReg.RegF3d;
import net.swc.aMAP.niftyReg.RegResample;
import net.swc.aMAP.parameters.FileParameter;
import net.swc.aMAP.parameters.IllegalValueException;
import net.swc.aMAP.parameters.IntParameter;
import net.swc.aMAP.parameters.Parameter;
import net.swc.aMAP.parameters.StringParameter;
import net.swc.aMAP.parameters.StringUiParameter;

public class AmapRunConfig {
	private List<? extends Parameter> aladinParameters;
	private List<? extends Parameter> f3dParameters;
	
	private FileParameter outDir;
	private FileParameter avgBrain;
	private FileParameter atlas;
	
	private FileParameter targetBrain;
	private String aladinExec;
	private String f3dExec;
	private String resampleExec;
	
	public AmapRunConfig(){
		aladinParameters = new ArrayList<Parameter>();
		f3dParameters = new ArrayList<Parameter>();
	}
	
	public void setOutDir(FileParameter outDir) throws IllegalValueException{
		outDir.checkValue();
		this.outDir = outDir;
	}
	
	public void setTargetBrain(FileParameter targetBrain) throws IllegalValueException{
		targetBrain.checkValue();
		this.targetBrain = targetBrain;
	}
	
	public void setAvgBrain(FileParameter avgBrain) throws IllegalValueException{
		avgBrain.checkValue();
		this.avgBrain = avgBrain;
	}
	
	public void setAtlas(FileParameter atlas) throws IllegalValueException{
		atlas.checkValue();
		this.atlas = atlas;
	}
	
	public void setAdditionalAladinParameters(List<? extends Parameter> aladinParameters) throws IllegalValueException{
		for (Parameter p : aladinParameters)
			p.checkValue();
		this.aladinParameters = aladinParameters;
	}
	
	public void setAdditionalf3dParameters(List<? extends Parameter> f3dParameters) throws IllegalValueException{
		for (Parameter p : f3dParameters)
			p.checkValue();
		this.f3dParameters = f3dParameters;
	}
	
	public void setCustomAladin(StringParameter aladinPath) throws IllegalValueException{
		aladinPath.checkValue();
		this.aladinExec = aladinPath.getValueString();
	}
	
	public void setCustomF3d(StringParameter f3dExec) throws IllegalValueException{
		f3dExec.checkValue();
		this.f3dExec = f3dExec.getValueString();
	}

	public void setCustomResample(StringParameter resampleExec) throws IllegalValueException{
		resampleExec.checkValue();
		this.resampleExec = resampleExec.getValueString();
	}
	
	public File affineFile(){
		return new File(outDir.getValue(), targetBrain.getValue().getName()+"AFF.txt");
	}
	
	public File averageOutFile(){
		return new File(outDir.getValue(), targetBrain.getValue().getName()+"AVERAGE_TRANSFORMED.nii");
	}
	
	public File segmentationOutFile(){
		return new File(outDir.getValue(), targetBrain.getValue().getName()+"SEGMENTATION.nii");
	}
	
	public File cppOutFile(){
		return new File(outDir.getValue(), targetBrain.getValue().getName()+"CPP.nii");
	}
	
	public File getErrOut(String command) {
		return new File(outDir.getValue(), targetBrain.getValue().getName()+"_"+command+"ERR.txt");
	}
	
	public File getLogOut(String command) {
		return new File(outDir.getValue(), targetBrain.getValue().getName()+"_"+command+"LOG.txt");
	}
	
	public NiftyCommand getAladinCommand() {
		NiftyCommand aladin = new RegAladin(aladinExec);
		aladin.setParameter(targetBrain);
		aladin.setParameter(avgBrain);
		aladin.setParameter(new FileParameter("-aff", affineFile()));
		aladin.setParameter(new FileParameter("-res", averageOutFile()));
		aladin.setParameters(aladinParameters);
		return aladin;
	}
	
	public NiftyCommand getf3dCommand() {
		NiftyCommand f3d = new RegF3d(f3dExec);
		f3d.setParameter(targetBrain);
		f3d.setParameter(avgBrain);
		f3d.setParameter(new FileParameter("-aff", affineFile()));
		f3d.setParameter(new FileParameter("-res", averageOutFile()));
		f3d.setParameter(new FileParameter("-cpp", cppOutFile()));
		f3d.setParameters(f3dParameters);
		return f3d;
	}
	
	public NiftyCommand getResampleCommand() {
		NiftyCommand resample = new RegResample(resampleExec);
		resample.setParameter(targetBrain);
		resample.setParameter(atlas);
		resample.setParameter(new FileParameter("-cpp", cppOutFile()));
		resample.setParameter(new FileParameter("-res", segmentationOutFile()));
		resample.setParameter(new IntParameter("-inter", 0));
		return resample;
	}

}
