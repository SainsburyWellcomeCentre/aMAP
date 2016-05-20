package net.swc.aMAP.parameters;


public class DoubleParameter extends AbstractParameter{
	
	private double min;
	private double max;
	private double value;

	public DoubleParameter(String parameter, double value) {
		this(parameter, value, Double.MIN_VALUE, Double.MAX_VALUE);
	}
	
	public DoubleParameter(String parameter, double value, double min, double max) {
		super(parameter);
		this.min = min;
		this.max = max;
		this.value = value;
	}

	@Override
	public void setValueInternal(String value) throws IllegalValueException {
		double val;
		try{val=Double.valueOf(value);
		}catch (NumberFormatException e){
		 throw new IllegalValueException(this,"Couldn't convert the input to double!: "+value, e);	
		}
		checkRange(val);
		this.value = Double.valueOf(value);
	}

	@Override
	public void checkValueInternal() throws IllegalValueException {
		checkRange(value);
	}
	
	private void checkRange(double val) throws IllegalValueException{
		if (!(val >=min && val <max))
			throw new IllegalValueException(this,"Value was outside the allowed Range! value: "+val+" min: "+min+" max: "+max);
	}

	@Override
	public String getValueString() {
		return String.valueOf(value);
	}

}
