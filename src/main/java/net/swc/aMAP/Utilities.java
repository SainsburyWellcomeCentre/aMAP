package net.swc.aMAP;

import java.awt.Component;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

public class Utilities {
	
	
	public static void exceptionPopup(Component source, Exception e){
		JOptionPane.showMessageDialog(source, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
	}
	
	public static String textToMultiLineHTML(String text, int charsBeforeLineBreak){
		StringBuilder sb = new StringBuilder();
		sb.append("<HTML><div style=\"text-align: left\">");
		StringTokenizer st = new StringTokenizer(text);
		int charCount = 0;
		while (st.hasMoreTokens()){
			String nextToken = st.nextToken();
			sb.append(nextToken);
			sb.append(" ");
			charCount += nextToken.length();
			if (charCount>charsBeforeLineBreak){
				sb.append("<BR>");
				charCount = 0;
			}
		}
		sb.append("</div></HTML>");
		return sb.toString();
	}

	// TODO test these more extensively!
	public static boolean isMac() {
		return System.getProperty("os.name").toLowerCase().contains("mac");
	}

	public static boolean isLinux() {
	 return System.getProperty("os.name").toLowerCase().contains("linux");
	}
	
	public static File resourceStringToFile(String resourceStr){
		File targetFile = null;
		try {
			String path = ClassLoader.getSystemClassLoader().getResource(resourceStr).getPath();
			path = URLDecoder.decode(path, "UTF-8");
			targetFile = new File(path);
		} catch (UnsupportedEncodingException e) {
			throw new Error("Couldn't get base Path!"); //TODO: proper error handling
		}
		return targetFile;
	}
}
