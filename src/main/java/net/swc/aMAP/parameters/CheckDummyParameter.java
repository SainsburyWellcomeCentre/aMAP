package net.swc.aMAP.parameters;


public class CheckDummyParameter extends AbstractParameter {

	public CheckDummyParameter(String parameter) {
		super(parameter);
	}

	@Override
	public void setValueInternal(String value){
	}

	@Override
	public String getValueString() {
		return null;
	}

	@Override
	public void checkValueInternal() throws IllegalValueException {
	}

}
