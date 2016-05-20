package net.swc.aMAP.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class FileButton extends JButton implements ActionListener {

	private static final long serialVersionUID = -6250143165777552314L;
	private final JTextField connectedField;
	private final JFileChooser fc;
	private File selectedFile = null;

	public FileButton(JTextField connectedField){
		this.connectedField = connectedField;
		fc = new JFileChooser();
		this.addActionListener(this);
		setText("Choose File");
	}

	public JFileChooser getFileChooser(){
		return fc;
	}
	
	public JTextField getTextField(){
		return connectedField;
	}

	public void actionPerformed(ActionEvent e) {
		if (fc.showOpenDialog(this.connectedField) == JFileChooser.APPROVE_OPTION){
			selectedFile = fc.getSelectedFile();
			connectedField.setText(selectedFile.getAbsolutePath());
			for(ActionListener a: connectedField.getActionListeners()){
				a.actionPerformed(new ActionEvent(connectedField, ActionEvent.ACTION_PERFORMED, null));
			}
		}	
	}

}


