package net.swc.aMAP.parameters;


public class IntParameter extends AbstractParameter {
	
	
	private int min;
	private int max;
	private int value;

	public IntParameter(String parameter, int value) {
		this(parameter, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public IntParameter(String parameter, int value, int min, int max) {
		super(parameter);
		this.value = value;
		this.min = min;
		this.max = max;
	}

	@Override
	public void setValueInternal(String value) throws IllegalValueException {
		int val;
		try{val=Integer.valueOf(value);
		}catch (NumberFormatException e){
		 throw new IllegalValueException(this,"Couldn't convert the input to int!: "+value, e);	
		}
		checkRange(val);
		this.value = Integer.valueOf(value);
	}

	@Override
	public void checkValueInternal() throws IllegalValueException {
		checkRange(value);
	}
	
	private void checkRange(int val) throws IllegalValueException{
		if (!(val >=min && val <max))
			throw new IllegalValueException(this,"Value was outside the allowed Range! value: "+val+" min: "+min+" max: "+max);
	}

	@Override
	public String getValueString() {
		return String.valueOf(value);
	}
	

}
