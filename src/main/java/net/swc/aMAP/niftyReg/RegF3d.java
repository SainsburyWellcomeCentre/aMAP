package net.swc.aMAP.niftyReg;

import java.util.Set;
import java.util.TreeSet;

import net.swc.aMAP.parameters.CheckDummyParameter;
import net.swc.aMAP.parameters.Parameter;

public class RegF3d extends NiftyCommand {

	public RegF3d() {super();}
	public RegF3d(String f3dExec) {
		super(f3dExec);
	}

	@Override
	public String commandName() {
		return "reg_f3d";
	}
	
	@Override
	protected Set<Parameter> getAdditionalParameters() {
		Set<Parameter> addParams = new TreeSet<Parameter>();
		addParams.add(new CheckDummyParameter("-aff"));
		addParams.add(new CheckDummyParameter("-cpp"));
		addParams.add(new CheckDummyParameter("-ln"));
		addParams.add(new CheckDummyParameter("-lp"));
		addParams.add(new CheckDummyParameter("-sx"));
		addParams.add(new CheckDummyParameter("-be"));
		addParams.add(new CheckDummyParameter("-smooR"));
		addParams.add(new CheckDummyParameter("-smooF"));
		addParams.add(new CheckDummyParameter("--fbn"));
		addParams.add(new CheckDummyParameter("--rbn"));
		return addParams;
	}

}
