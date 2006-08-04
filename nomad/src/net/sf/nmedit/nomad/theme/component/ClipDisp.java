package net.sf.nmedit.nomad.theme.component;

import java.awt.Color;
import java.awt.Graphics;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ParameterEvent;
import net.sf.nmedit.nomad.theme.property.ParameterProperty;
import net.sf.nmedit.nomad.theme.property.PropertySet;

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
 * Created on Jul 24, 2006
 */

public class ClipDisp extends NomadDisplay implements EventListener<ParameterEvent>
{

    private double vclip = 1; // 0 - 1
    private boolean vsym = false; //|log

    public ClipDisp()
    {
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        final int w = getWidth();
        final int h = getHeight();

        final int cx = w/2;
        final int cy = h/2;
        final int len = Math.min(w, h)-1; // sidelen
        
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(cx, cy-(len/2), cx, cy+(len/2));
        g.drawLine(cx-(len/2), cy, cx+(len/2), cy);
        
        g.setColor(getForeground());
        
        double delta = (len*(1-vclip))/2.0d;
        int dx = (int) Math.ceil(delta);
        int dy = dx;

        g.drawLine(cx, cy, cx+dx, cy-dy);
        g.drawLine(cx+dx, cy-dy, cx+(len/2), cy-dy);
        
        if (vsym)
        {
            g.drawLine(cx, cy, cx-dx, cy+dy);
            g.drawLine(cx-dx, cy+dy, cx-(len/2), cy+dy);
        }
        else
        {
            g.drawLine(cx, cy, cx-(len/2), cy+(len/2));
        }
    }
    
    public void setClip(double v)
    {
        this.vclip = bounded(v);
        repaint();
    }
    
    public void setSymmetric(boolean v)
    {
        this.vsym = v;
        repaint();
    }

    protected double bounded(double v)
    {
        return Math.max(0, Math.min(v, 1.0d));
    }

    private Parameter clipParam = null;
    private Parameter symParam = null;

    public final static String ICLIP = "parameter#0";
    public final static String ISYM = "parameter#1";

    public void registerProperties(PropertySet set) {
        super.registerProperties(set);
        set.add(new ParameterProperty(0));
        set.add(new ParameterProperty(1));
    }

    public void link(Module module) {
        //addValueChangeListener(broadcast);

        symParam = module.getParameter(getParameterInfo(ISYM).getContextId());
        if (symParam!=null) 
        {
            symParam.addListener(this);
        }
        
        clipParam = module.getParameter(getParameterInfo(ICLIP).getContextId());
        if (clipParam!=null) 
        {
            clipParam.addListener(this);
        }
        updateValues();
    }

    public void unlink() {
//      removeValueChangeListener(broadcast);
        if (symParam!=null) 
        {
            symParam.removeListener(this);
            symParam = null;
        }
        if (clipParam!=null) 
        {
            clipParam.removeListener(this);
            clipParam = null;
        }
    }

    public void event(ParameterEvent event)
    {
        if (event.getID()==ParameterEvent.PARAMETER_VALUE_CHANGED)
        {
            updateValues();
        }
    }

    private void updateValues()
    {
        double v;
        
        if (clipParam!=null)
        v = (clipParam.getValue()-clipParam.getMinValue())
        / (double) (clipParam.getMaxValue()-clipParam.getMinValue());
        else
            v = 0;
        setClip(v);
        
        if (symParam!=null)
            setSymmetric(symParam.getValue() == symParam.getMaxValue());
    }

}
