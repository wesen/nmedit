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

import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jpatch.event.ParameterEvent;
import net.sf.nmedit.jpatch.event.ParameterValueChangeListener;
import net.sf.nmedit.jtheme.JTContext;

public class JTTextDisplay extends JTLabel implements ParameterValueChangeListener
{

    final public static String uiClassID = "TextDisplayUI";
    
    private Parameter par;
    
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

    public void setParameter(Parameter par)
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

    protected void install(Parameter p)
    {
        p.addParameterValueChangeListener(this);
    }

    protected void uninstall(Parameter p)
    {
        p.removeParameterValueChangeListener(this);
    }

    protected void updateText()
    {
        if (par != null)
            setText(par.getFormattedValue());
        else
            setText(null);
    }
    
    public void parameterValueChanged(ParameterEvent e)
    {
        updateText();
        repaint();
    }
    
}

