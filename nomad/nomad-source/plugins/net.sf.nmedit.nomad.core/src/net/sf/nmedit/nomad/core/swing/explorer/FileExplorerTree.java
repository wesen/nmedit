package net.sf.nmedit.nomad.core.swing.explorer;

public class FileExplorerTree extends ExplorerTree {
	/**
     * 
     */
    private static final long serialVersionUID = -1065473531878285689L;

    public void installUI() {
    	setUI(new FileExplorerTreeUI());
	}
}
