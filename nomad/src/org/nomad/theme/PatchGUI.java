package org.nomad.theme;

import javax.swing.JPanel;

import org.nomad.patch.Patch;


public class PatchGUI extends JPanel {
	Patch patch = null;
	
	public void rebuildUI() {
		patch.rebuildUI();
	}
	
	public PatchGUI(Patch patch) {
		this.patch = patch;
	}
	
	public Patch getPatch() {
		return patch;
	}
}
