package nomad.gui;

import javax.swing.JPanel;

import nomad.patch.Patch;

public class PatchGUI extends JPanel {
	Patch patch = null;
	
	public PatchGUI(Patch patch) {
		this.patch = patch;
	}
	
	public Patch getPatch() {
		return patch;
	}
}
