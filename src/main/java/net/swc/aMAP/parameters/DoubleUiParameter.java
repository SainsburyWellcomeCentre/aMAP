package net.swc.aMAP.parameters;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import net.swc.aMAP.Utilities;
import net.swc.aMAP.gui.HelpProvider;

public class DoubleUiParameter extends DoubleParameter implements UiParameter, ActionListener {

	private JLabel label;
	private JTextField tfValue;
	
	public DoubleUiParameter(String parameter, double value, double min, double max) {
		this(parameter, parameter, value, min, max);
	}
	
	public DoubleUiParameter(String parameter, String label, double value, double min, double max) {
		super(parameter, value, min, max);
		this.label = new JLabel(label);
		tfValue = new JTextField(String.valueOf(value));
		tfValue.addActionListener(this);
		tfValue.setToolTipText(HelpProvider.getInstance().getTooltip(this));
	}
	
	public JLabel getLabel() {
		return label;
	}

	public Component getValueComponent() {
		return tfValue;
	}

	public void actionPerformed(ActionEvent arg0) {
		try {
			super.setValue(tfValue.getText().trim());
		} catch (IllegalValueException e) {
			Utilities.exceptionPopup(tfValue, e);
		}
	}

	@Override
	public void setValueInternal(String value) throws IllegalValueException{
		super.setValueInternal(value);
		tfValue.setText(value);
	}
	
	@Override
	public void checkValueInternal() throws IllegalValueException{
		super.setValue(tfValue.getText().trim());
		super.checkValueInternal();
	}

	public void setEnabled(boolean value) {
		label.setEnabled(value);
		tfValue.setEnabled(value);
	}
}
