/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Created on Feb 13, 2006
 */
package org.nomad.patch.ui;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.nomad.env.Environment;
import org.nomad.patch.Patch;

public class PatchUI extends JSplitPane {

	private Patch patch;

	private JScrollPane scrollPanePoly = null;
	private JScrollPane scrollPaneCommon = null;
	private ModuleSectionUI commonSectionUI = null;
	private ModuleSectionUI polySectionUI = null;

	protected PatchUI(Patch patch) {
		super(JSplitPane.VERTICAL_SPLIT);
		this.patch = patch;
	}
	
	public static PatchUI newInstance(Patch patch) {
		PatchUI ui = new PatchUI(patch);
		ui.rebuild();
		return ui;
	}

	public Patch getPatch() {
		return patch;
	}
	
	public ModuleSectionUI getCommonSection() {
		return commonSectionUI;
	}
	
	public ModuleSectionUI getPolySection() {
		return polySectionUI;
	}

	public void rebuild() {

		// remove
		if (scrollPaneCommon!=null) 	remove(scrollPaneCommon);
		if (scrollPanePoly!=null) 		remove(scrollPanePoly);
		if (getCommonSection()!=null) 	getCommonSection().unlink();
		if (getPolySection()!=null) 	getPolySection().unlink();

		// new
		polySectionUI 	= Environment.sharedInstance().getFactory().getModuleSectionUI(getPatch().getPolySection());
		polySectionUI.setSize(polySectionUI.getPreferredSize());
        scrollPanePoly = new JScrollPane(polySectionUI); 
        add(scrollPanePoly, JSplitPane.TOP);
		
		
		commonSectionUI = Environment.sharedInstance().getFactory().getModuleSectionUI(getPatch().getCommonSection());
		commonSectionUI.setSize(commonSectionUI.getPreferredSize());
        scrollPaneCommon = new JScrollPane(commonSectionUI);
        add(scrollPaneCommon, JSplitPane.BOTTOM);

        setDividerLocation(getPatch().getHeader().getSeparatorPosition() + 1);

        polySectionUI.populate();
        commonSectionUI.populate();
	}

}
