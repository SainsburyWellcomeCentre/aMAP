package net.swc.aMAP.parameters;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import net.swc.aMAP.Utilities;
import net.swc.aMAP.gui.HelpProvider;

public class StringUiParameter extends StringParameter implements UiParameter, ActionListener {

	private JLabel label;
	private JTextField textField;

	public StringUiParameter(String parameter, String text, String value) {
		super(parameter, value);
		label = new JLabel(text);
		textField = new JTextField(value);
		textField.setToolTipText(HelpProvider.getInstance().getTooltip(this));
	}

	public JLabel getLabel() {
		return label;
	}

	public Component getValueComponent() {
		return textField;
	}

	public void setEnabled(boolean value) {
		label.setEnabled(value);
		textField.setEnabled(value);
	}
	
	@Override
	public void setValueInternal(String value) throws IllegalValueException{
		super.setValueInternal(value);
		textField.setText(value);
	}
	
	@Override
	public void checkValueInternal() throws IllegalValueException{
		super.setValue(textField.getText());
		super.checkValueInternal();
	}

	public void actionPerformed(ActionEvent e) {
		try {
			super.setValue(textField.getText());
		} catch (IllegalValueException e1) {
			Utilities.exceptionPopup(textField , e1);
		}
	}

}
