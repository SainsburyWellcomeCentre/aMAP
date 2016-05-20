package net.swc.aMAP.parameters;


public class StringParameter extends AbstractParameter {

	private String value;

	public StringParameter(String parameter, String value) {
		super(parameter);
		this.value = value;
	}

	@Override
	public void setValueInternal(String value) throws IllegalValueException {
		if (value==null)
			throw new IllegalValueException(this, "Value can't be null.");
		this.value = value;
	}

	@Override
	public String getValueString() {
		return value;
	}

	@Override
	public void checkValueInternal() throws IllegalValueException {
		if (value==null)
			throw new IllegalValueException(this, "Value can't be null.");
	}

}
