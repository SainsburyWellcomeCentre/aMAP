package net.swc.aMAP.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.swc.aMAP.Utilities;
import net.swc.aMAP.parameters.Parameter;

public class HelpProvider {
	
	public static final int charsBeforeLineBreak = 60;
	public static final File helpHeader = Utilities.getResource("help/HelpHeader.txt");
	private static final HelpProvider uniqueInstance = new HelpProvider();
	private Map<String, String> tooltips;

	public static HelpProvider getInstance(){
		return uniqueInstance;
	}

	private HelpProvider(){
		initTooltips();
	}

	private void initTooltips() { //TODO: load help messages from external file
		tooltips = new HashMap<String, String>();
		tooltips.put("-ref", 
				"The “reference” image, the image that remains unchanged during the registration. Set this value to the downscaled brain you wish to segment");
		tooltips.put("-flo", 
				"The “floating” image, the image that is morphed to increase similarity to the reference. Set this to the average brain belonging to the atlas (aladin/f3d) or the atlas itself (resample).");
		tooltips.put("-res", 
				"The output path for the resampled floating image.");
		tooltips.put("-aff",
				"The text file for the affine transformation matrix. The parameter can either be an output (aladin) or input (f3d) parameter.");
		tooltips.put("-ln",
				"Registration starts with further downsampled versions of the original data to optimize the global fit of the result and prevent "
						+ "“getting stuck” in local minima of the similarity function. This parameter determines how many downsampling steps are being performed, "
						+ "with each step halving the data size along each dimension.");
		tooltips.put("-lp", 
				"Determines how many of the downsampling steps defined by -ln will have their registration computed. "
						+ "The combination -ln 3 -lp 2 will e.g. calculate 3 downsampled steps, each of which is half the size of the previous one "
						+ "but only perform the registration on the 2 smallest resampling steps, skipping the full resolution data.");
		tooltips.put("-sx", "Sets the control point grid spacing in x. Positive values are interpreted as real values in mm, "
				+ "negative values are interpreted as distance in voxels. If -sy and -sz are not defined seperately, they are set to the value given here.");
		tooltips.put("-be","Sets the bending energy, which is the coefficient of the penalty term, preventing the freeform registration from overfitting. "
				+ "The range is between 0 and 1 (exclusive) with higher values leading to more restriction of the registration.");
		tooltips.put("-smooR", "Adds a gaussian smoothing to the reference image (e.g. the brain to be segmented), with the sigma defined by the number. "
				+ "Positive values are interpreted as real values in mm, negative values are interpreted as distance in voxels.");
		tooltips.put("-smooF", "Adds a gaussian smoothing to the floating image (e.g. the average brain), with the sigma defined by the number. "
				+ "Positive values are interpreted as real values in mm, negative values are interpreted as distance in voxels.");
		tooltips.put("--fbn", "Number of bins used for the Normalized Mutual Information histogram on the floating image.");
		tooltips.put("--rbn", "Number of bins used for the Normalized Mutual Information histogram on the reference image.");
		tooltips.put("-outDir", "All output and log files will be written to this folder. Pre-existing files will be overwritten without warning!");
	}
	
	public String getTooltip(Parameter p){
		String tt =tooltips.get(p.getParameter());
		return tt==null? "" : Utilities.textToMultiLineHTML(tt, charsBeforeLineBreak);
	}
	
	public void openHelpWindow(){
		JTextArea helpText = new JTextArea();
		helpText.setText(getHelpString());
		helpText.setLineWrap(true);
		helpText.setWrapStyleWord(true);
		JOptionPane helper = new JOptionPane(new JScrollPane(helpText), JOptionPane.INFORMATION_MESSAGE);
		final JDialog helpWindow =helper.createDialog(AmapMainFrame.getInstance(), "Help");
		helpWindow.setModal(false);
		helpWindow.setResizable(true);
		helpWindow.setSize(AmapMainFrame.getInstance().getSize());
		helpWindow.setVisible(true);
	}
	
	public String getHelpString(){
		StringBuilder sb = new StringBuilder();
		BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader(helpHeader));
			String line;
			while ((line=r.readLine())!=null){
				sb.append(line);
				sb.append("\n");
			}
		} catch (FileNotFoundException e) {
			Utilities.exceptionPopup(AmapMainFrame.getInstance(), e);
		} catch (IOException e) {
			Utilities.exceptionPopup(AmapMainFrame.getInstance(), e);
		} finally{try {
			r.close();
		} catch (IOException e) {
			Utilities.exceptionPopup(AmapMainFrame.getInstance(), e);
		}}
		
		sb.append("\n\n");
		
		for (Map.Entry<String, String> paramDescription : tooltips.entrySet()){
			sb.append(paramDescription.getKey());
			sb.append("\t");
			sb.append(paramDescription.getValue());
			sb.append("\n\n");
		}
		
		return sb.toString();
		
	}
	


}
