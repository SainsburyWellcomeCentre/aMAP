package net.swc.aMAP.parameters;


public class IllegalValueException extends Exception {

	private static final long serialVersionUID = -5576106885450143986L;

	public IllegalValueException(Parameter p, String message, Throwable cause) {
		super(makeMessage(p,message), cause);
	}

	public IllegalValueException(Parameter p, String message) {
		super(makeMessage(p,message));
	}

	private static String makeMessage(Parameter p, String message) {
		return "Error setting Parameter "+p.getParameter()+": "+message;
	}

}
