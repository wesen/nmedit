package net.sf.nmedit.nmutils.swing;

import java.awt.datatransfer.Clipboard;

public class ApplicationClipboard {
	private static Clipboard applicationClipboard = null;
	public static Clipboard getApplicationClipboard() { return applicationClipboard; }
	public static void setApplicationClipboard(Clipboard clipboard) { applicationClipboard = clipboard; }
}
