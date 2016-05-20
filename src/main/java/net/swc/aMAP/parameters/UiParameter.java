package net.swc.aMAP.parameters;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

public interface UiParameter extends Parameter, ActionListener {
	public JLabel getLabel();
	public Component getValueComponent();
	public void setEnabled(boolean value);
}
