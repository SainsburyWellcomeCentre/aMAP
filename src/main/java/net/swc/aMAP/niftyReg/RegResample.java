package net.swc.aMAP.niftyReg;

import java.util.Set;
import java.util.TreeSet;

import net.swc.aMAP.parameters.CheckDummyParameter;
import net.swc.aMAP.parameters.Parameter;

public class RegResample extends NiftyCommand {

	public RegResample() {super();}

	public RegResample(String resampleExec) {
		super(resampleExec);
	}

	@Override
	public String commandName() {
		return "reg_resample";
	}

	@Override
	protected Set<Parameter> getAdditionalParameters() {
		Set<Parameter> addParams = new TreeSet<Parameter>();
		addParams.add(new CheckDummyParameter("-inter"));
		addParams.add(new CheckDummyParameter("-cpp"));
		return addParams;
	}
	
	

}
