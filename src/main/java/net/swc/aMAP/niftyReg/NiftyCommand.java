package net.swc.aMAP.niftyReg;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.swc.aMAP.Utilities;
import net.swc.aMAP.parameters.CheckDummyParameter;
import net.swc.aMAP.parameters.Parameter;

public abstract class NiftyCommand implements Command {
	
	
	abstract protected Set<Parameter> getAdditionalParameters();
	
	final Set<Parameter> validParameters;
	final Set<Parameter> activeParameters;
	private String command;
	
	public NiftyCommand(){
		this(null);
	}
	
	public NiftyCommand(String command){
		validParameters = new TreeSet<Parameter>();
		validParameters.addAll(getAdditionalParameters());
		validParameters.add(new CheckDummyParameter("-ref"));
		validParameters.add(new CheckDummyParameter("-flo"));
		validParameters.add(new CheckDummyParameter("-res"));
		activeParameters = new TreeSet<Parameter>();
		this.command = command;
	}
	
	public String getBaseCommand(){
		return command==null? getDefaultCommand(): command;	
	}
	
	public String getDefaultCommand(){
		return getCommandResource(commandName());
	}
	
	abstract public String commandName();

	public void setParameters(List<? extends Parameter> aladinParameters){
		for (Parameter p : aladinParameters)
			setParameter(p);
	}
	
	public void setParameter(Parameter p){
		if (!validParameters.contains(p))
			throw new Error("Invalid parameter type "+p.getFullParameterString());
		if (activeParameters.contains(p))
			activeParameters.remove(p);
		activeParameters.add(p);
	}
	
	protected static String getCommandResource(String name){
		String resourceString = "";
		if (Utilities.isMac())
			resourceString = "niftyReg/bin/osX/"+name;
		else if (Utilities.isLinux())
			resourceString = "niftyReg/bin/linux_x64/"+name;
		else{
			throw new Error("System platform not supported. Currently this software will only run on 64 bit Mac and Linux machines");
		}
		return Utilities.getResource(resourceString).getAbsolutePath();
	}
	
	public final List<String> getCommand(){
		List<String> commandList = new ArrayList<String>();
		commandList.add(getBaseCommand());
		for (Parameter activeParameter : activeParameters){
			commandList.addAll(activeParameter.getFullParameterTokenized());
		}
		return commandList;
	}
	
}
