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
package net.sf.nmedit.jtheme.clavia.nordmodular;

import java.awt.Color;
import java.awt.Dimension;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTControl;
import net.sf.nmedit.jtheme.component.JTKnob;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;

public class JTMorphModule extends JTModule
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 7164915539987099677L;
    private JTControl[] morphKnobs;
    private final static Color[] MORPH_COLORS = {
        NomadClassicColors.MORPH_RED,
        NomadClassicColors.MORPH_GREEN,
        NomadClassicColors.MORPH_BLUE,
        NomadClassicColors.MORPH_YELLOW
    };
    
    public JTMorphModule(JTContext context)
    {
        super(context);
        setOpaque(false);
        initSavely();
    }

    void initSavely()
    {
        try
        {
            init();
        }
        catch (JTException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    void init() throws JTException
    {
        final int size = 17;
        final int pad = 3;
        
        morphKnobs = new JTKnob[4];
        JTContext context = getContext();
        for (int i=0;i<morphKnobs.length;i++)
        {
            JTControl knob = (JTControl) context.createComponent(JTContext.TYPE_KNOB);
            knob.setSize(size, size);
            knob.setBackground(MORPH_COLORS[i]);
            knob.setLocation(pad*(i+1)+size*i, 1);
            morphKnobs[i] = knob;
            add(knob);
        }
        
        Dimension d = new Dimension(size*morphKnobs.length+
                pad*(morphKnobs.length+2), size+2);
        
        setSize(d);
        setPreferredSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
    }
    
    public int getMorphIndex(JTControl knob) {
    	for (int i = 0; i < morphKnobs.length; i++) {
    		if (morphKnobs[i] == knob)
    			return i;
    	}
    	return -1;
    }
    
    public void setModule(PModule module)
    {
        super.setModule(module);
        
        for (int i=Math.min(3, module.getParameterCount()-1);i>=0;i--)
        {
            PParameter m = module.getParameter(i);
            morphKnobs[i].setAdapter(new JTParameterControlAdapter(m));
        }
    }
    
}
