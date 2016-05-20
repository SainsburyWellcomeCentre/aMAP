package net.swc.aMAP.niftyReg;

import java.util.Set;
import java.util.TreeSet;

import net.swc.aMAP.parameters.CheckDummyParameter;
import net.swc.aMAP.parameters.Parameter;



public class RegAladin extends NiftyCommand {

	public RegAladin() {super();}
	public RegAladin(String aladinExec) {
		super(aladinExec);
	}

	@Override
	public String commandName() {
		return "reg_aladin";
	}

	@Override
	protected Set<Parameter> getAdditionalParameters() {
		Set<Parameter> addParams = new TreeSet<Parameter>();
		addParams.add(new CheckDummyParameter("-aff"));
		addParams.add(new CheckDummyParameter("-ln"));
		addParams.add(new CheckDummyParameter("-lp"));
		return addParams;
	}

}
