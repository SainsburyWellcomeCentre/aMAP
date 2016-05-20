package net.swc.aMAP.parameters;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

public class FileParameter extends AbstractParameter {

	private File file;
	private final FileFilter filter;
	protected final List<FileFlag> necessaryFlags;
	public static final String FILE_PREFIX = "file://";
	
	public static enum FileFlag{
		EXISTS, READ, WRITE, EXECUTE, IS_FOLDER
	}
	
	public FileParameter(String parameter, File value){
		this(parameter, defaultFlags(), value);
	}
	
	public FileParameter(String parameter, File value, FileFilter fileFilter) {
		this(parameter, value, defaultFlags(), fileFilter);
	}
	
	public FileParameter(String parameter, List<FileFlag> flags, File value){
		this(parameter, value, flags, acceptAllFilter());
	}

	public FileParameter(String parameter, File value, List<FileFlag> flags, FileFilter filter) {
		super(parameter);
		this.file = value==null? null : value.getAbsoluteFile();
		this.filter = filter;
		this.necessaryFlags = flags;
	}

	@Override
	public void setValueInternal(String value) throws IllegalValueException {
		value = value.trim();
		File newValue;
		try{
		if (value.startsWith("file://"))
				newValue = new File(new URL(value).toURI());
		else
			newValue = new File(value);
		} catch (MalformedURLException e) {
			throw new IllegalValueException(this, "could not convert the following String to file: "+value+" - Potential Drag/n/Drop issue");
		} catch (URISyntaxException e) {
			throw new IllegalValueException(this, "could not convert the following String to file: "+value+" - Potential Drag/n/Drop issue");
		}
		checkFile(newValue);
		this.file = newValue;
	}

	@Override
	public void checkValueInternal() throws IllegalValueException {
		checkFile(file);
	}

	@Override
	public String getValueString() {
		return file==null? "" : file.getAbsolutePath();
	}
	
	public File getValue(){
		return new File(file.getAbsolutePath());
	}
	
	private void checkFile(File file) throws IllegalValueException{
		if(file==null)
			throw new IllegalValueException(this, "File not set!");
		if(!filter.accept(file))
			throw new IllegalValueException(this, "File "+file.getAbsolutePath()+" is of wrong fileType ("+filter.getDescription()+")");
		if((necessaryFlags.contains(FileFlag.EXISTS) || necessaryFlags.contains(FileFlag.READ)) && !file.exists())
			throw new IllegalValueException(this,"File "+file.getAbsolutePath()+" does not exist!");
		if(necessaryFlags.contains(FileFlag.READ) && !file.canRead())
			throw new IllegalValueException(this,"Can't open "+file.getAbsolutePath()+" for reading - Insufficient rights?");
		if(necessaryFlags.contains(FileFlag.WRITE) && !file.canWrite())
			throw new IllegalValueException(this,"Don't have write access to "+file.getAbsolutePath());
		if(necessaryFlags.contains(FileFlag.EXECUTE) && !file.canExecute())
			throw new IllegalValueException(this,"Don't have execution access to "+file.getAbsolutePath());
		if(necessaryFlags.contains(FileFlag.IS_FOLDER) && !file.isDirectory())
			throw new IllegalValueException(this,"I'm expecting a Directory as input, but got a file: "+file.getAbsolutePath());
		if(!necessaryFlags.contains(FileFlag.IS_FOLDER) && file.isDirectory())
			throw new IllegalValueException(this,"I'm expecting a File as input, but got a directory: "+file.getAbsolutePath());
	}
	
	private static final List<FileFlag> defaultFlags(){
		List<FileFlag> def = new ArrayList<FileFlag>();
		def.add(FileFlag.EXISTS);
		def.add(FileFlag.READ);
		return def;
	}
	
	private static FileFilter acceptAllFilter() {
		return new FileFilter() {	
			public boolean accept(File arg0) {
				return true;
			}
			@Override
			public String getDescription() {
				return "Accepts All Filetypes";
			}
		};
	}

	public FileFilter getFilter() {
		return filter;
	}
}
