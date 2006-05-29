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
package net.sf.nmedit.nomad.patch.ui;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;
import net.sf.nmedit.nomad.patch.virtual.Patch;

public class PatchUI extends JSplitPane implements net.sf.nmedit.nomad.util.document.Document {

	private Patch patch;

	private JScrollPane scrollPanePoly = null;
	private JScrollPane scrollPaneCommon = null;
	private ModuleSectionUI commonSectionUI = null;
	private ModuleSectionUI polySectionUI = null;

	double lastLoc = 1.0d;

	protected PatchUI(Patch patch) {
		super(JSplitPane.VERTICAL_SPLIT);
        
        setBorder(null);
        setOneTouchExpandable(true);
        setContinuousLayout(false); // dont repaint when slider is moved
        setResizeWeight(1); // top component (poly section) gets extra space when component is resized
        setOpaque(true);
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
		if (scrollPaneCommon!=null) remove(scrollPaneCommon);
		if (scrollPanePoly!=null) 	remove(scrollPanePoly);
		if (commonSectionUI!=null) 	commonSectionUI.unlink();
		if (polySectionUI!=null) 	polySectionUI.unlink();

		// new
		polySectionUI 	= NomadEnvironment.sharedInstance().getFactory().getModuleSectionUI(getPatch().getPolyVoiceArea());
		polySectionUI.setSize(polySectionUI.getPreferredSize());
     //   polySectionUI.setPatchUI(this);
        scrollPanePoly = new JScrollPane(polySectionUI);
        scrollPanePoly.getViewport().setBorder(null);
        scrollPanePoly.setBorder(null);
        add(scrollPanePoly, JSplitPane.TOP);
		
		commonSectionUI = NomadEnvironment.sharedInstance().getFactory().getModuleSectionUI(getPatch().getCommonVoiceArea());
		commonSectionUI.setSize(commonSectionUI.getPreferredSize());
       // commonSectionUI.setPatchUI(this);
        scrollPaneCommon = new JScrollPane(commonSectionUI);
        scrollPaneCommon.getViewport().setBorder(null);
        scrollPaneCommon.setBorder(null);
        add(scrollPaneCommon, JSplitPane.BOTTOM);
        
        polySectionUI.populate();
        commonSectionUI.populate();

        // 0 = top, 4000 = bottom
        //setDividerLocation(getPatch().getHeader().getSeparatorPosition()/4000.0d);
	}

    public String getTitle()
    {
        return getName();
    }

    public JComponent getComponent()
    {
        return this;
    }
	
}
