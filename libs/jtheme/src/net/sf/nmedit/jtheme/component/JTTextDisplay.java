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
package net.sf.nmedit.jtheme.component;

import net.sf.nmedit.jpatch.event.PParameterEvent;
import net.sf.nmedit.jpatch.event.PParameterListener;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jtheme.JTContext;

public class JTTextDisplay extends JTLabel implements PParameterListener
{

    /**
     * 
     */
    private static final long serialVersionUID = -179265143363795185L;

    final public static String uiClassID = "TextDisplayUI";
    
    private PParameter par;
    
    public JTTextDisplay(JTContext context)
    {
        super(context);
        setOpaque(true);
    }

    public String getUIClassID() 
    {
        return uiClassID;
    }
    
    protected void setReducible(boolean r)
    { 
        // no op
    }

    public final boolean isReducible()
    {
        return false;
    }

    public void setParameter(PParameter par)
    {
        if (this.par != par)
        {
            if (this.par != null)
                uninstall(this.par);
            this.par = par;
            if (par != null)
                install(par);
            updateText();
            repaint();
        }
    }

    protected void install(PParameter p)
    {
        p.addParameterListener(this);
    }

    protected void uninstall(PParameter p)
    {
        p.removeParameterListener(this);
    }

    protected void updateText()
    {
        if (par != null)
            setText(par.getDisplayValue());
        else
            setText(null);
    }
    
    public void parameterValueChanged(PParameterEvent e)
    {
        updateText();
        repaint();
    }

    public void focusRequested(PParameterEvent e)
    {
        // no op
    }
    
}

