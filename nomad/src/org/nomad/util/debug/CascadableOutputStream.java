package org.nomad.util.debug;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CascadableOutputStream extends FilterOutputStream {

	public final static String DEFAULT_CONTEXT_SEPARATOR = "  | "; 
	private final static String LINE_SEPARATOR = System.getProperty("line.separator");
	private CascadeModel model = null;
	private boolean newLine = true;

	public CascadableOutputStream(OutputStream out) {
		this(out, out instanceof CascadableOutputStream ? DEFAULT_CONTEXT_SEPARATOR : null);
	}

	public CascadableOutputStream(OutputStream out, String prefix) {
		this(out, new CascadeModel.Constant(prefix));
	}

	public CascadableOutputStream(OutputStream out, CascadeModel model) {
		super(out);
		if (model==null)
			model = new CascadeModel.Constant(null);
		this.model = model;
	}
	
	public OutputStream out() {
		return out;
	}

	public void resetLine() {
		newLine = true;
	}

	public boolean hasUnfinishedLine() {
		return !newLine;
	}
	
	public void write(int b) throws IOException {
		 write(new byte[] {(byte)b});
	}
	
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}
	
	public void write(byte[] b, int off, int len) throws IOException {
		if (len>0) {
			String prefix = newLine?model.prefix():"";
			String text = new String(b, off, len);
			if (text.endsWith(LINE_SEPARATOR)) {
				text = text.substring(0, text.length()-LINE_SEPARATOR.length());
				newLine = true;
			} else {
				newLine = false;
			}
			
			int index = 0;
			while ((index=text.indexOf(LINE_SEPARATOR, index))>=0) {
				String before = text.substring(0, index)+LINE_SEPARATOR+model.prefix();
				String after  = text.substring(index+LINE_SEPARATOR.length(), text.length());
				index = before.length();
				text = before+after;
			}
			
			text = prefix+text+(newLine?LINE_SEPARATOR:"");
			out.write(text.getBytes());
		}
	}
	
}
