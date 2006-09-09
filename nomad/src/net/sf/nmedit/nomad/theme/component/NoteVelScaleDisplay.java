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
package net.sf.nmedit.nomad.theme.component;


import java.awt.Color;
import java.awt.Graphics;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.Event;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ParameterListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DParameter;
import net.sf.nmedit.jtheme.PropertyName;


public class NoteVelScaleDisplay extends NomadDisplay implements ParameterListener 
{ 

    private double vlGain = 0.5; // 0 - 1, 0.5=center
    private double vrGain = 0.5; // 0 - 1, 0.5=center
    private double vbreakPoint = 0.5; // 0 - 1

    private Parameter parLGain = null;
    private Parameter parRGain = null;
    private Parameter parBreak = null;

    public final static String SLGAIN = "parameter#0";
    public final static String SRGAIN = "parameter#1";
    public final static String SBREAK = "parameter#2";
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        final int w = getWidth();
        final int h = getHeight();

        final int cy = h/2;
        final int len = (int) Math.sqrt(w*w+h*h); // diagonal:rect(w,h)
        
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, cy, w-1, cy); // horizontal line
        
        g.setColor(getForeground());
        
        // origin 
        final int ox = (int) Math.round(vbreakPoint*(w-1));
        final double flg = ((1-vlGain)*2-1)*24/25;
        final double frg = (vrGain*2-1)*24/25;

        g.drawLine(ox, cy, ox+len, (int) (cy+flg*len) );
        g.drawLine(ox, cy, ox-len, (int) (cy-frg*len) );
    }
    
    private double bounded(double v)
    {
        return Math.max(0, Math.min(v, 1.0d));
    }

    public void setBreakPoint( double v )
    {
        this.vbreakPoint = bounded(v);
        repaint();
    }

    public void setLeftGain( double v )
    {
        this.vlGain = bounded(v);
        repaint();
    }

    public void setRightGain( double v )
    {
        this.vrGain = bounded(v);
        repaint();
    }

    @PropertyName(name="lgain")
    public void setLeftGainParamSpec(DParameter p)
    {  setParameterInfo(SLGAIN, p); }
    @PropertyName(name="rgain")
    public void setRightGainParamSpec(DParameter p)
    {  setParameterInfo(SRGAIN, p); }
    @PropertyName(name="breakpoint")
    public void setBreakParamSpec(DParameter p)
    {  setParameterInfo(SBREAK, p); }

    @PropertyName(name="lgain")
    public DParameter getLeftGainParamSpec()
    {  return getParameterInfo(SLGAIN); }
    @PropertyName(name="rgain")
    public DParameter getRightGainParamSpec()
    {  return getParameterInfo(SRGAIN); }
    @PropertyName(name="breakpoint")
    public DParameter getBreakParamSpec()
    {  return getParameterInfo(SBREAK); }

    public void link(Module module) {
        parLGain = module.getParameter(getParameterInfo(SLGAIN).getContextId());
        if (parLGain!=null) parLGain.addParameterListener(this);
        parRGain = module.getParameter(getParameterInfo(SRGAIN).getContextId());
        if (parRGain!=null) parRGain.addParameterListener(this);
        parBreak = module.getParameter(getParameterInfo(SBREAK).getContextId());
        if (parBreak!=null) parBreak.addParameterListener(this);
        
        updateValues();
    }

    public void unlink() {
        if (parLGain!=null) parLGain.removeParameterListener(this);
        if (parRGain!=null) parRGain.removeParameterListener(this);
        if (parBreak!=null) parBreak.removeParameterListener(this);

        parLGain = null;
        parRGain = null;
        parBreak = null;
    }
    
    protected void updateValues()
    {
        if (parLGain!=null) setLeftGain(getDoubleValue(parLGain));
        if (parRGain!=null) setRightGain(getDoubleValue(parRGain));
        if (parBreak!=null) setBreakPoint(getDoubleValue(parBreak));
    }
    
    public void parameterValueChanged( Event e )
    {
        Parameter p = e.getParameter();
        if (parLGain==p) setLeftGain(getDoubleValue(p));
        else if (parRGain==p) setRightGain(getDoubleValue(p));
        else if (parBreak==p) setBreakPoint(getDoubleValue(p));
    }

    public void parameterMorphValueChanged( Event e )
    { }

    public void parameterKnobAssignmentChanged( Event e )
    { }

    public void parameterMorphAssignmentChanged( Event e )
    { }

    public void parameterMidiCtrlAssignmentChanged( Event e )
    { }
    
    
}
