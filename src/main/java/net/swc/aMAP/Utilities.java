package net.swc.aMAP;

import java.awt.Component;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
	
	public static File getResource(String resourceStr){
		File targetFile = null;
			URL resource = ClassLoader.getSystemClassLoader().getResource(resourceStr);
			if (resource==null){ // if the class loader can't find the resource, check the filesystem
				resource = Utilities.class.getResource(Utilities.class.getSimpleName()+".class");
				targetFile = UrlToFile(resource);
				targetFile = targetFile.getParentFile(); // now we have the program's parent folder
				try {
					targetFile = new File(new URI("file://"+targetFile.getAbsolutePath()+"/"+resourceStr));
				} catch (URISyntaxException e) {
					targetFile = null;
				}
			} else
				targetFile = UrlToFile(resource);
		return targetFile;
	}
	
	public static File UrlToFile(URL url){
		String urlStr = url.toString();
		int endPos = urlStr.contains("!/")? urlStr.indexOf("!/") : urlStr.length();
		urlStr = urlStr.substring(urlStr.indexOf("file:/"), endPos);
		try {
			url = new URL(urlStr);
			return new File(url.toURI());
		} catch (URISyntaxException e) {
			throw new Error("Unexpected error during resource decoding! ("+url+")"); //TODO: proper error handling
		} catch (MalformedURLException e1) {
			throw new Error("Unexpected error during resource decoding! ("+urlStr+")"); //TODO: proper error handling
		}
	}
}
