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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JViewport;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;

public class PatchUI extends JSplitPane 
{

	private Patch patch;

	private JScrollPane scrollPanePoly = null;
	private JScrollPane scrollPaneCommon = null;
	private ModuleSectionUI commonSectionUI = null;
	private ModuleSectionUI polySectionUI = null;
	private JSplitPane split = this;
    
	protected PatchUI(Patch patch) 
    {   
        super(JSplitPane.VERTICAL_SPLIT);
        this.patch = patch;
        patch.setProperty(Patch.UI, this);
        setOpaque(true);
        
        split.setBorder(BorderFactory.createLoweredBevelBorder());
        split.setContinuousLayout(false); // dont repaint when slider is moved
        split.setResizeWeight(1); // top component (poly section) gets extra space when component is resized
        split.setDividerLocation(1.0d);
        split.setOneTouchExpandable(true);
        split.setOpaque(false);
        split.setBackground(null);
        split.addPropertyChangeListener( JSplitPane.DIVIDER_LOCATION_PROPERTY, new DivLocProperty());
        
        //setLayout(new BorderLayout());
        //add(split, BorderLayout.CENTER);
	}

    private class DivLocProperty implements PropertyChangeListener
    {

        public void propertyChange( PropertyChangeEvent evt )
        {
            double d = split.getDividerLocation();
            double div = split.getHeight()-split.getDividerSize();
            /*
            System.out.println(
              "loc:"+split.getDividerLocation()+
              " h:"+split.getHeight()+
              "-s:"+split.getDividerSize()
              
            );
            */
            if (div!=0)
                d/=(double)div;
            else
                d = 1;
            d*=4000.0d;
         
            // TODO check when sep position can be set
            patch.getHeader().setValueWithoutNotification
            (
                    Format.HEADER_SECTION_SEPARATOR_POSITION,
                    Math.max(Format.HEADER_SECTION_SEPARATOR_POSITION_TOP_MOST,
                            Math.min((int)d, Format.HEADER_SECTION_SEPARATOR_POSITION_BOTTOM_MOST))
            );
        }
        
    }
    
	public static PatchUI newInstance(Patch patch) 
    {
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
    
    public void dispose()
    {
        unlinkInternal();
        patch = null;
        scrollPanePoly = null;
        scrollPaneCommon = null;
        commonSectionUI = null;
        polySectionUI = null;
        split = null;
    }
    
    private void unlinkInternal()
    {
        // remove
        if (scrollPaneCommon!=null) split.remove(scrollPaneCommon);
        if (scrollPanePoly!=null)   split.remove(scrollPanePoly);
        if (commonSectionUI!=null)  commonSectionUI.unlink();
        if (polySectionUI!=null)    polySectionUI.unlink();
    }

	public void rebuild() 
    {
        double sep = patch.getHeader().getSeparatorPosition(); 
        
        final int SCROLL_MODE = JViewport.BACKINGSTORE_SCROLL_MODE;//BLIT_SCROLL_MODE;

        unlinkInternal();
        
		// new
		polySectionUI 	= NomadEnvironment.sharedInstance().getTheme().getModuleSectionUI(getPatch().getPolyVoiceArea());
		polySectionUI.setSize(polySectionUI.getPreferredSize());
     //   polySectionUI.setPatchUI(this);
        scrollPanePoly = new JScrollPane(polySectionUI);
        scrollPanePoly.getViewport().setBorder(null);
        scrollPanePoly.setBorder(null);
        scrollPanePoly.getViewport().setScrollMode(SCROLL_MODE);
        scrollPanePoly.getHorizontalScrollBar().setUnitIncrement(10);
        scrollPanePoly.getVerticalScrollBar().setUnitIncrement(10);

        split.add(scrollPanePoly, JSplitPane.TOP);
		
		commonSectionUI = NomadEnvironment.sharedInstance().getTheme().getModuleSectionUI(getPatch().getCommonVoiceArea());
		commonSectionUI.setSize(commonSectionUI.getPreferredSize());
       // commonSectionUI.setPatchUI(this);
        scrollPaneCommon = new JScrollPane(commonSectionUI);
        scrollPaneCommon.getViewport().setBorder(null);
        scrollPaneCommon.setBorder(null);
        scrollPaneCommon.getViewport().setScrollMode(SCROLL_MODE);
        split.add(scrollPaneCommon, JSplitPane.BOTTOM);
        scrollPaneCommon.getHorizontalScrollBar().setUnitIncrement(10);
        scrollPaneCommon.getVerticalScrollBar().setUnitIncrement(10);

        polySectionUI.populate();
        commonSectionUI.populate();

        split.setDividerLocation(sep/4000.0d);
	}

}
