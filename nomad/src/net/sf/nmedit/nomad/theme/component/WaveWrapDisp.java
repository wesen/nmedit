package net.sf.nmedit.nomad.theme.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ParameterEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DParameter;
import net.sf.nmedit.nomad.theme.property.ParameterProperty;
import net.sf.nmedit.nomad.theme.property.ParameterValue;
import net.sf.nmedit.nomad.theme.property.PropertySet;
import net.sf.nmedit.nomad.theme.property.Value;

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

public class WaveWrapDisp extends NomadDisplay implements EventListener<ParameterEvent> 
{

    private double vwrap = 0; // 0 - 1

    private int gw = 0;
    private int gh = 0;
    private boolean flagForceUpdate = true;
    private GeneralPath gp = new GeneralPath();


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

        updateGraph();
        g.setColor(getForeground());

        g = g.create(cx-len/2, cy-len/2, len, len);
        g.translate(-(cx-len/2),-(cy-len/2));
        ((Graphics2D)g).draw(gp);
        g.dispose();
    }
    
    private float x(double x, double len, int peaks)
    {
        x = (x+0.5)*len;

        final double divMin = 1;
        final double divMax = (peaks*2d-1);
        final double div = (divMax-divMin)*(vwrap)+divMin;
        x = x/div;
        
        return (float) x;
    }
    
    private float fww(double n)
    {
        n = (n*2-1)*Math.PI/2d;
        return (float) Math.sin(n);
    }
    
    private double bounded(double v)
    {
        return Math.max(0, Math.min(v, 1.0d));
    }

    public void setWaveWrap( double v )
    {
        this.vwrap = bounded(v);
        flagForceUpdate = true;
        repaint();
    }
    
    private void updateGraph()
    {
        final int w = getWidth();
        final int h = getHeight();

        if ((w==gw)&&(h==gh)&&(!flagForceUpdate)) return;
        
        gw = w;
        gh = h;
        gp.reset();
        flagForceUpdate = false;
        
        final int cx = w/2;
        final int cy = h/2;
        final int len = Math.min(w, h)-1; // sidelen

        gp.reset();
        final int peaks = 9;// 4+5
        {
            int n = -peaks;
            gp.moveTo(x(n, len, peaks), fww(n));
            for (;n<peaks;n++)
            {
                gp.lineTo(x(n,len,peaks), fww(n));
            }
        }

        AffineTransform at = new AffineTransform();
        at.translate(cx, cy);
        at.scale(1, len/2);
        gp.transform(at);
    }

    private Parameter parameter = null;

    public void registerProperties(PropertySet set) 
    {
        super.registerProperties(set);
        set.add(new ParameterProperty(0));
    }
    
    public void link(Module module) {
        parameter = module.getParameter(getParameterInfo("parameter#0").getContextId());
        if (parameter!=null) {
            setWaveWrapValue( parameter );
            parameter.addListener(this);
        }
    }

    public void unlink() {
//      removeValueChangeListener(broadcast);
        if (parameter!=null) 
        {
            parameter.removeListener(this);
            parameter = null;
        }
    }

    public void event(ParameterEvent event)
    {
        Parameter p = event.getParameter();
        
        if (event.getID()==ParameterEvent.PARAMETER_VALUE_CHANGED)
        {
            setWaveWrapValue( p );
        }
    }
    
    private void setWaveWrapValue(Parameter p)
    {
        double v = (p.getValue()-p.getMinValue())
            / (double) (p.getMaxValue()-p.getMinValue());
        setWaveWrap(v);
    }

}
