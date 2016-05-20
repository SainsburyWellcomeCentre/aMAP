package net.swc.aMAP.parameters;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import net.miginfocom.swing.MigLayout;
import net.swc.aMAP.Utilities;
import net.swc.aMAP.gui.FileButton;
import net.swc.aMAP.gui.HelpProvider;

public class FileUiParameter extends FileParameter implements UiParameter, ActionListener {
	JLabel label;
	private JPanel filePanel;
	private JTextField fileField;
	private FileButton fbtn;
	
	
	public FileUiParameter(String parameter, String text, List<FileFlag> flags, File value) {
		super(parameter, flags, value);
		initComponents(text);
	}

	public FileUiParameter(String parameter, String text, File value, List<FileFlag> flags,
			FileFilter filter) {
		super(parameter, value, flags, filter);
		initComponents(text);
	}

	public FileUiParameter(String parameter, String text, File value) {
		super(parameter, value);
		initComponents(text);
	}
	
	public FileUiParameter(String parameter, String text, File value,
			FileFilter fileFilter) {
		super(parameter, value, fileFilter);
		initComponents(text);
	}

	private void initComponents(String labelText){
		label = new JLabel(labelText);
		filePanel = new JPanel();
		filePanel.setLayout(new MigLayout("insets 0","[grow, fill][]"));
		fileField = new JTextField();
		fileField.setText(getValueString());
		fileField.addActionListener(this);
		filePanel.add(fileField, "height 30:30:30");
		fbtn = new FileButton(fileField);
		fbtn.getFileChooser().setFileFilter(getFilter());
		if(necessaryFlags.contains(FileFlag.IS_FOLDER))
			fbtn.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		filePanel.add(fbtn);
		filePanel.setToolTipText(HelpProvider.getInstance().getTooltip(this));
		fileField.setToolTipText(HelpProvider.getInstance().getTooltip(this));
		fbtn.setToolTipText(HelpProvider.getInstance().getTooltip(this));
	}
	
	public JLabel getLabel() {
		return label;
	}

	public Component getValueComponent() {
		return filePanel;
	}
	
	@Override
	public void setValueInternal(String value) throws IllegalValueException{
		super.setValueInternal(value);
		fileField.setText(value);
	}
	
	@Override
	public void checkValueInternal() throws IllegalValueException{
		super.setValue(fileField.getText());
		super.checkValueInternal();
	}

	public void actionPerformed(ActionEvent e) {
		try {
			super.setValue(fileField.getText());
		} catch (IllegalValueException e1) {
			Utilities.exceptionPopup(filePanel, e1);
		}
	}
	
	public void setEnabled(boolean value) {
		label.setEnabled(value);
		fileField.setEnabled(value);
		fbtn.setEnabled(value);
	}
	
}
