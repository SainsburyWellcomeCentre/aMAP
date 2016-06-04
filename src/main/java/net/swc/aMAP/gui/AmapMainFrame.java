package net.swc.aMAP.gui;


import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;
import net.swc.aMAP.Utilities;
import net.swc.aMAP.niftyReg.RegAladin;
import net.swc.aMAP.niftyReg.RegF3d;
import net.swc.aMAP.niftyReg.RegResample;
import net.swc.aMAP.parameters.DoubleUiParameter;
import net.swc.aMAP.parameters.FileParameter;
import net.swc.aMAP.parameters.FileUiParameter;
import net.swc.aMAP.parameters.IllegalValueException;
import net.swc.aMAP.parameters.IntUiParameter;
import net.swc.aMAP.parameters.StringUiParameter;
import net.swc.aMAP.parameters.UiParameter;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;

public class AmapMainFrame extends JFrame{

	private static final long serialVersionUID = 839947739963898118L;

	private static AmapMainFrame instance;

	private List<UiParameter> aladinParameters;
	private List<UiParameter> f3dParameters;
	private final AmapController con;

	private FileFilter niiFilter;
	private FileUiParameter targetBrain;
	private FileUiParameter avgBrainParameter;
	private FileUiParameter atlasParameter;
	private FileUiParameter outDir;
	private StringUiParameter resamplePath;
	private StringUiParameter f3dPath;
	private StringUiParameter aladinPath;
	private JRadioButton rdbtnDefaultAtlas;
	private JRadioButton rdbtnDefaultBackend;
	private JLabel statusLabel;
	private JButton runButton;

	private final static String CANCEL = "Cancel Run";
	private final static String RUN = "Run";

	private final static int initialWidth = 620;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
					AmapMainFrame frame = getInstance();
					frame.setVisible(true);
			}
		});
	}
	
	public static synchronized AmapMainFrame getInstance(){
		if (instance==null)
			instance = new AmapMainFrame();
		return instance;
	}

	/**
	 * Create the frame.
	 */
	private AmapMainFrame() {

		con = new AmapController(this);
		initParameters();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, initialWidth, 690);
		setContentPane(makeContentPane());
		this.pack();
		Dimension size = getSize();
		this.setSize(new Dimension(initialWidth, (int) size.getHeight()));
		setTitle("Automated Mouse Atlas Propagation (aMAP)");
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e){
				cleanUpAndQuit();
			}
		});
	}

	private void initParameters(){
		aladinParameters = new ArrayList<UiParameter>();
		aladinParameters.add(new IntUiParameter("-ln", 6, 1, 20));
		aladinParameters.add(new IntUiParameter("-lp", 5, 1, 20));

		f3dParameters = new ArrayList<UiParameter>();
		f3dParameters.add(new IntUiParameter("-ln", 6, 1, 20));
		f3dParameters.add(new IntUiParameter("-lp", 4, 1, 20));
		f3dParameters.add(new IntUiParameter("-sx", -10, Integer.MIN_VALUE, Integer.MAX_VALUE));
		f3dParameters.add(new DoubleUiParameter("-be", 0.95, 0, 1));
		f3dParameters.add(new DoubleUiParameter("-smooR", -1, -Double.MAX_VALUE, Double.MAX_VALUE));
		f3dParameters.add(new DoubleUiParameter("-smooF", -1, -Double.MAX_VALUE, Double.MAX_VALUE));
		f3dParameters.add(new IntUiParameter("--fbn", 128, 1, Integer.MAX_VALUE));
		f3dParameters.add(new IntUiParameter("--rbn", 128, 1, Integer.MAX_VALUE));

		niiFilter = new FileNameExtensionFilter("NiftyReg-compatible files: .nii, .nii.gz, .hdr", "nii", "nii.gz", "hdr");

		targetBrain = new FileUiParameter("-ref", "Target Brain", AmapController.testBrain, niiFilter);
		avgBrainParameter = defaultAvg();
		atlasParameter = defaultAtlas();

		List<FileParameter.FileFlag> outFolderFlags = new ArrayList<FileParameter.FileFlag>();
		outFolderFlags.add(FileParameter.FileFlag.EXISTS);
		outFolderFlags.add(FileParameter.FileFlag.WRITE);
		outFolderFlags.add(FileParameter.FileFlag.READ);
		outFolderFlags.add(FileParameter.FileFlag.IS_FOLDER);
		outDir = new FileUiParameter("-outDir", "Output Folder", outFolderFlags, new File(""));

		aladinPath = defaultAladin();
		f3dPath = defaultF3d();
		resamplePath = defaultResample();
	}

	private JPanel makeContentPane(){
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		contentPane.setLayout(new MigLayout("fillx, insets 8", "[fill]"));

		contentPane.add(makeInputDataPane(),"wrap");
		contentPane.add(new JSeparator(), "wrap");

		contentPane.add(makeAtlasPane(), "wrap");
		contentPane.add(new JSeparator(), "wrap");

		contentPane.add(makeBackendPane(), "wrap");
		contentPane.add(new JSeparator(), "wrap");

		contentPane.add(makeParameterPane(), "wrap");
		contentPane.add(new JSeparator(), "wrap");

		statusLabel = new JLabel(" ");
		contentPane.add(statusLabel, "wrap");

		contentPane.add(makeButtonPanel());

		setBackendToDefault();
		setAtlasToDefault();

		return contentPane;
	}

	private JPanel makeInputDataPane() {
		JPanel inputPane = new JPanel();
		inputPane.setLayout(new MigLayout("fillx, insets 0", "[][grow, fill]"));
		inputPane.add(new JLabel("Data Locations"),"alignx center, spanx, wrap");

		inputPane.add(targetBrain.getLabel());
		inputPane.add(targetBrain.getValueComponent(),"wrap");

		inputPane.add(outDir.getLabel());
		inputPane.add(outDir.getValueComponent(), "wrap");

		return inputPane;
	}

	private JPanel makeAtlasPane() {
		JPanel atlasPane = new JPanel();
		atlasPane.setLayout(new MigLayout("fillx, insets 0", "[][]rel[fill,grow]", "[][][]"));
		JLabel lblAtlasSettings = new JLabel("Atlas Settings");
		atlasPane.add(lblAtlasSettings, "alignx center, spanx, wrap");

		rdbtnDefaultAtlas = new JRadioButton("Default Atlas");
		rdbtnDefaultAtlas.setSelected(true);
		atlasPane.add(rdbtnDefaultAtlas, "cell 0 1");
		JRadioButton rdbtnCustomAtlas = new JRadioButton("Custom Atlas");
		atlasPane.add(rdbtnCustomAtlas, "cell 0 2");

		ButtonGroup atlasGrp = new ButtonGroup();
		atlasGrp.add(rdbtnDefaultAtlas);
		atlasGrp.add(rdbtnCustomAtlas);

		rdbtnDefaultAtlas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setAtlasToDefault();
			}});
		rdbtnCustomAtlas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setAtlasToCustom();
			}});

		atlasPane.add(avgBrainParameter.getLabel(),"cell 1 1");
		atlasPane.add(avgBrainParameter.getValueComponent(),"cell 2 1");

		atlasPane.add(atlasParameter.getLabel(),"cell 1 2");
		atlasPane.add(atlasParameter.getValueComponent(),"cell 2 2");
		return atlasPane;
	}

	private JPanel makeBackendPane() {
		JPanel backendSettingsPane = new JPanel();
		backendSettingsPane.setLayout(new MigLayout("fill, insets 0","[][][fill,grow][][fill,grow]"));

		JLabel lblBackendSettings = new JLabel("Backend Settings");
		backendSettingsPane.add(lblBackendSettings, "alignx center, spanx, wrap");

		rdbtnDefaultBackend = new JRadioButton("Default NiftyReg Backend");
		rdbtnDefaultBackend.setSelected(true);

		backendSettingsPane.add(rdbtnDefaultBackend, "cell 0 1");
		JRadioButton rdbtnCustomBackend = new JRadioButton("Custom NiftyReg Backend");

		backendSettingsPane.add(rdbtnCustomBackend, "cell 0 2");

		ButtonGroup backendGrp = new ButtonGroup();
		backendGrp.add(rdbtnDefaultBackend);
		backendGrp.add(rdbtnCustomBackend);

		rdbtnDefaultBackend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBackendToDefault();
			}});
		rdbtnCustomBackend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBackendToCustom();
			}});

		backendSettingsPane.add(aladinPath.getLabel(),"cell 1 1");
		backendSettingsPane.add(aladinPath.getValueComponent(),"wrap");
		backendSettingsPane.add(f3dPath.getLabel());
		backendSettingsPane.add(f3dPath.getValueComponent());
		backendSettingsPane.add(resamplePath.getLabel(), "cell 3 1");
		backendSettingsPane.add(resamplePath.getValueComponent());

		return backendSettingsPane;
	}

	private JPanel makeParameterPane(){
		JPanel parameterPane = new JPanel();
		parameterPane.setLayout(new MigLayout("fill, insets 0","[align center,grow][align center,grow]","[][grow]"));

		parameterPane.add(new JLabel("Parameters"), "alignx center, spanx, wrap");

		JPanel aladinParamPane = new JPanel();
		aladinParamPane.setLayout(new MigLayout("fill","[][fill,grow]"));
		aladinParamPane.add(new JLabel("Affine Registration (reg_aladin)"), "alignx center, spanx, wrap");
		addParametersToPane(aladinParamPane, aladinParameters);

		JPanel f3dParameterPane = new JPanel();
		f3dParameterPane.setLayout(new MigLayout("fill","[][fill,grow]"));
		f3dParameterPane.add(new JLabel("Free-Form Registration (reg_f3d)"), "alignx center, spanx, wrap");
		addParametersToPane(f3dParameterPane, f3dParameters);

		parameterPane.add(aladinParamPane);
		parameterPane.add(f3dParameterPane);

		return parameterPane;
	}

	private void addParametersToPane(JPanel paramPane, List<UiParameter> parameters) {
		for (UiParameter p : parameters){
			paramPane.add(p.getLabel());
			paramPane.add(p.getValueComponent(),"growx, wrap");
		}
	}

	private JPanel makeButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new MigLayout("fillx, insets 0", "[][][fill, grow][align right]"));

		runButton = new JButton(RUN);
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand()==RUN)runAmap();
				if (e.getActionCommand()==CANCEL)cancelAmap();
			}
		});
		buttonPanel.add(runButton);

		JButton helpButton = new JButton("Help");
		helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HelpProvider.getInstance().openHelpWindow();
			}
		});
		buttonPanel.add(helpButton);

		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cleanUpAndQuit();
			}
		});
		buttonPanel.add(quitButton, "skip");
		return buttonPanel;

	}

	private FileUiParameter defaultAvg(){
		return new FileUiParameter("-flo", "Average Brain", AmapController.defaultAvgFile, niiFilter);
	}
	private FileUiParameter defaultAtlas(){
		return new FileUiParameter("-flo", "Atlas", AmapController.defaultAtlasFile, niiFilter);
	}
	private StringUiParameter defaultF3d(){
		return new StringUiParameter("-f3dExec", "reg_aladin", new RegF3d().getBaseCommand());
	}
	private StringUiParameter defaultAladin(){
		return new StringUiParameter("-aladinExec", "reg_aladin", new RegAladin().getBaseCommand());
	}
	private StringUiParameter defaultResample(){
		return new StringUiParameter("-transformExec", "reg_aladin", new RegResample().getBaseCommand());
	}

	protected void setBackendToDefault() {
		aladinPath.setEnabled(false);
		f3dPath.setEnabled(false);
		resamplePath.setEnabled(false);
	}

	protected void setBackendToCustom() {
		aladinPath.setEnabled(true);
		f3dPath.setEnabled(true);
		resamplePath.setEnabled(true);
	}

	protected void setAtlasToCustom() {
		avgBrainParameter.setEnabled(true);
		atlasParameter.setEnabled(true);
	}

	protected void setAtlasToDefault() {
		avgBrainParameter.setEnabled(false);
		atlasParameter.setEnabled(false);
	}

	protected void cleanUpAndQuit() {
		if (con.isRunning() && JOptionPane.showConfirmDialog(this, "Segmentation is still running, are you sure you want to exit?")!=JOptionPane.YES_OPTION)
			return;
		con.requestAbort();
		System.exit(0);
	}

	protected void runAmap() {
		setRunButtonMode(CANCEL);
		AmapRunConfig rc = null;
		try{
			rc = createRunConfig();
			con.runAmap(rc);
		} catch(IllegalValueException e){
			Utilities.exceptionPopup(this, e);
			setRunButtonMode(RUN);
		}

	}

	protected void cancelAmap(){
		con.requestAbort();
		setRunButtonMode(RUN);
	}

	private void setRunButtonMode(String mode){
		if (mode==RUN){
			runButton.setActionCommand(RUN);
			runButton.setText(RUN);
		} else if(mode==CANCEL){
			runButton.setActionCommand(CANCEL);
			runButton.setText(CANCEL);
		}
	}

	public void setStatus(String text){
		statusLabel.setText(text);
	}

	public void done() {
		setStatus("Done!");
		runButton.setText(RUN);
		runButton.setActionCommand(RUN);
	}

	protected AmapRunConfig createRunConfig() throws IllegalValueException {
		AmapRunConfig runConfig = new AmapRunConfig();
		runConfig.setOutDir(outDir);
		runConfig.setTargetBrain(targetBrain);
		runConfig.setAvgBrain(rdbtnDefaultAtlas.isSelected()?defaultAvg() : avgBrainParameter);
		runConfig.setAtlas(rdbtnDefaultAtlas.isSelected()?defaultAtlas() : atlasParameter);

		if(!rdbtnDefaultBackend.isSelected()){
			runConfig.setCustomAladin(aladinPath);
			runConfig.setCustomF3d(f3dPath);
			runConfig.setCustomResample(resamplePath);
		}

		runConfig.setAdditionalAladinParameters(aladinParameters);
		runConfig.setAdditionalf3dParameters(f3dParameters);
		return runConfig;
	}
}
