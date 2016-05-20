package net.swc.aMAP.parameters;

import java.util.List;

public interface Parameter extends Comparable<Parameter> {

	
	public void setValue(String value) throws IllegalValueException;
	
	public String getParameter();
	
	public String getValueString();
	
	public String getFullParameterString();
	
	public List<String> getFullParameterTokenized();
	
	public void checkValue() throws IllegalValueException;
	
}
