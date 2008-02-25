package net.sf.nmedit.nmutils.swing;

import java.awt.datatransfer.Clipboard;

public interface CopyCutPasteTarget {
	public boolean canCopy();
	public void performCopy(Clipboard clipBoard);
	public boolean canCut();
	public void performCut(Clipboard clipBoard);
	public boolean canPaste();
	public void performPaste(Clipboard clipBoard);
}
