package main;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

class Patch extends JPanel {
    private PatchData patchData = null;
	
    private JPanel toolPanel;
	
	private JSplitPane splitPane; 
	private JScrollPane scrollPanePoly;
	private JScrollPane scrollPaneCommon;

	public Patch() {
		this.setLayout(new BorderLayout());
		patchData = new PatchData();
        patchData.setPanes(new JModAreaPane(true, patchData), new JModAreaPane(false, patchData));
	}
	
	public PatchData getPatchData() {
		return patchData;
	}
	
    public StringBuffer savePatch() {
        return patchData.savePatch(splitPane);
    }
    
	public JPanel createPatch(String patchFile) {

	    patchData.loadPatch(patchFile);

		toolPanel = new JPanel(); 
		
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		scrollPanePoly = new JScrollPane(patchData.getDesktopPane(true)); 
		scrollPaneCommon = new JScrollPane(patchData.getDesktopPane(false));

		splitPane.add(scrollPanePoly, JSplitPane.TOP);
		splitPane.add(scrollPaneCommon, JSplitPane.BOTTOM);

//		toolPanel.add(new JButton("Tools voor Patch"));

//		this.add(toolPanel, BorderLayout.NORTH);
		this.add(splitPane, BorderLayout.CENTER);

        patchData.setPreferredSizes();

		patchData.showPatch(splitPane);
		
		return this;
	}
}
