package net.swc.aMAP.parameters;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractParameter implements Parameter {
	
	final String parameter;
	private IllegalValueException invalid;
	
	public AbstractParameter(String parameter){
		this.parameter = parameter;
	}
	
	public final void setValue(String value) throws IllegalValueException{
		try{
			setValueInternal(value);
			invalid = null;
		} catch(IllegalValueException e){
			invalid = e;
			throw e;
		}
	}
	
	public final void checkValue() throws IllegalValueException{
		try{
			checkValueInternal();
			if (invalid != null)
				throw invalid;
		} catch(IllegalValueException e){
			invalid = e;
			throw e;
		}
	}
	public abstract void setValueInternal(String val) throws IllegalValueException;
	public abstract void checkValueInternal() throws IllegalValueException;
	public abstract String getValueString();
	public String getParameter(){
		return parameter.trim();
	}
	
	public String getFullParameterString(){
		if (getValueString()==null || getValueString().isEmpty())
			return getParameter();
		return getParameter()+" "+getValueString();
	}
	
	public List<String> getFullParameterTokenized(){
		List<String> params = new ArrayList<String>();
		params.add(getParameter());
		if (!(getValueString()==null || getValueString().isEmpty()))
			params.add(getValueString());
		return params;
	}
	
	@Override
	public String toString(){
		return getFullParameterString();
	}
	
	@Override
	public boolean equals(Object o){
		return o!=null 
				&& o instanceof Parameter 
				&& ((Parameter)o).getParameter().equals(parameter);
	}
	
	@Override
	public int hashCode(){
		return parameter.hashCode();
	}

	public int compareTo(Parameter o) {
		return parameter.compareTo(o.getParameter());
	}

}
