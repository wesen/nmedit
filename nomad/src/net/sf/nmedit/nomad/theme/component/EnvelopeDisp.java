package net.sf.nmedit.nomad.theme.component;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.Event;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ParameterListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DParameter;

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

public class EnvelopeDisp extends NomadDisplay implements ParameterListener 
{

    // adsr / ad / ahd
    
    // sustain-release enabled
    private boolean srEnabled = true;
    // hold enabled
    private boolean hEnabled = true;

    private double va = 0;
    private double vd = 0;
    private double vh = 0;
    private double vs = 0;
    private double vr = 0;

    public final static int LOG = 0;
    public final static int LIN = 1;
    public final static int EXP = 2;
    
    private int attackType = LOG;
    private boolean inverse = false;

    public final static String ATTACK = "parameter#0";
    public final static String DECAY = "parameter#1";
    public final static String HOLD = "parameter#2";
    public final static String SUSTAIN = "parameter#3";
    public final static String RELEASE = "parameter#4";
    public final static String INVERSE = "parameter#5";

    public void setAttackParameterSpec(DParameter p)
    {   setParameterInfo(ATTACK, p); }
    public DParameter getAttackParameterSpec()
    {   return getParameterInfo(ATTACK); }

    public void setDecayParameterSpec(DParameter p)
    {   setParameterInfo(DECAY, p); }
    public DParameter getDecayParameterSpec()
    {   return getParameterInfo(DECAY); }

    public void setHoldParameterSpec(DParameter p)
    {   setParameterInfo(HOLD, p); }
    public DParameter getHoldParameterSpec()
    {   return getParameterInfo(HOLD); }

    public void setSustainParameterSpec(DParameter p)
    {   setParameterInfo(SUSTAIN, p); }
    public DParameter getSustainParameterSpec()
    {   return getParameterInfo(SUSTAIN); }

    public void setReleaseParameterSpec(DParameter p)
    {   setParameterInfo(RELEASE, p); }
    public DParameter getReleaseParameterSpec()
    {   return getParameterInfo(RELEASE); }

    public void setInverseParameterSpec(DParameter p)
    {   setParameterInfo(INVERSE, p); }
    public DParameter getInverseParameterSpec()
    {   return getParameterInfo(INVERSE); }

    Parameter parAttack = null;
    Parameter parDecay = null;
    Parameter parHold = null;
    Parameter parSustain = null;
    Parameter parRelease = null;
    Parameter parInv = null;

    public void link(Module module) {
        
        DParameter p ;
        if ((p=getParameterInfo(ATTACK))!=null)
        {
            parAttack = module.getParameter(p.getContextId());
            if (parAttack!=null) parAttack.addParameterListener(this);
        }
        if ((p=getParameterInfo(DECAY))!=null)
        {
            parDecay = module.getParameter(p.getContextId());
            if (parDecay!=null) parDecay.addParameterListener(this);
        }
        if ((p=getParameterInfo(HOLD))!=null)
        {
            parHold = module.getParameter(p.getContextId());
            if (parHold!=null) parHold.addParameterListener(this);
        }
        if ((p=getParameterInfo(SUSTAIN))!=null)
        {
            parSustain = module.getParameter(p.getContextId());
            if (parSustain!=null) parSustain.addParameterListener(this);
        }
        if ((p=getParameterInfo(RELEASE))!=null)
        {
            parRelease = module.getParameter(p.getContextId());
            if (parRelease!=null) parRelease.addParameterListener(this);
        }
        if ((p=getParameterInfo(INVERSE))!=null)
        {
            parInv = module.getParameter(p.getContextId());
            if (parInv!=null) parInv.addParameterListener(this);
        }
        updateValues();
    }

    public void unlink() {
        if (parAttack!=null) parAttack.removeParameterListener(this);
        if (parDecay!=null) parDecay.removeParameterListener(this);
        if (parHold!=null) parHold.removeParameterListener(this);
        if (parSustain!=null) parSustain.removeParameterListener(this);
        if (parRelease!=null) parRelease.removeParameterListener(this);
        if (parInv!=null) parInv.removeParameterListener(this);

        parAttack = parDecay = parHold = parSustain = parRelease = parInv = null;
    }

    protected void updateValues()
    {
        if (parAttack!=null) setAttack(getDoubleValue(parAttack));
        if (parDecay!=null) setDecay(getDoubleValue(parDecay));
        if (parHold!=null) setHold(getDoubleValue(parHold));
        if (parSustain!=null) setSustain(getDoubleValue(parSustain));
        if (parRelease!=null) setRelease(getDoubleValue(parRelease));
        if (parInv!=null) setInverse(parInv.getValue()==1);
    }
    
    public void parameterValueChanged( Event e )
    {
        Parameter p = e.getParameter();        
        if (parAttack==p) setAttack(getDoubleValue(p));
        else
        if (parDecay==p) setDecay(getDoubleValue(p));
        else
        if (parHold==p) setHold(getDoubleValue(p));
        else
        if (parSustain==p) setSustain(getDoubleValue(p));
        else
        if (parRelease==p) setRelease(getDoubleValue(p));
        else
        if (parInv==p) setInverse(parInv.getValue()==1);
    }

    public int getAttackType()
    {
        return attackType;
    }
    
    public void setAttackType(int t)
    {
        switch (t)
        {
            case LOG:break;
            case LIN:break;
            case EXP:break;
            default :
                throw new IllegalArgumentException("Invalid attack type");
        }
        if (attackType!=t)
        {
            attackType=t;
            repaint();
        }
    }
    
    public void setInverse(boolean e)
    {
        if (inverse!=e)
        {
            this.inverse = e;
            repaint();
        }
    }
    
    public boolean isInverse()
    {
        return inverse;
    }
    
    public boolean isSREnabled()
    {
        return srEnabled;
    }
    
    public void setSREnabled(boolean e)
    {
        if (e!=srEnabled)
        {
            srEnabled = e;
            repaint();
        }
    }

    public boolean isHoldEnabled()
    {
        return hEnabled;
    }
    
    public void setHoldEnabled(boolean e)
    {
        if (e!=hEnabled)
        {
            hEnabled = e;
            repaint();
        }
    }

    public EnvelopeDisp()
    {
        setAttack(1);
        setDecay(1);
        setHold(1);
        setSustain(0.5);
        setRelease(1);
    }
    
    public void configureADSR()
    {
        setHoldEnabled(false);
        setSREnabled(true);
        setInverse(false);
        setAttackType(LOG);
    }
    
    public void configureAD()
    {
        setHoldEnabled(false);
        setSREnabled(false);
        setInverse(false);
        setAttackType(LIN);
    }
    
    public void configureAHD()
    {
        setHoldEnabled(true);
        setSREnabled(false);
        setInverse(false);
        setAttackType(LIN);
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        final double segments =
            2 + (hEnabled?1:0) + (srEnabled?2:0);
        
        // each segment has space (0,+1) => scale by 1/segments
        
        GeneralPath gp = new GeneralPath();
        gp.moveTo(0, 0); // always start at origin
        
        float left = 0; // left offset
        {   // attack
            final float ax = (float)va;
            final float ay = 1;

            left+=ax;
            switch(attackType)
            {
                case LOG:
                    gp.curveTo(
                            0, 0.25f,
                            0, 1f,  
                            left, ay);
                    break;
                case LIN:
                    gp.lineTo(left, ay);
                    break;
                case EXP: 
                    gp.curveTo(
                            (float)va, 0,
                            (float)va, 1,  
                            left, ay);
                    break;
            }
        }
        
        if (hEnabled)
        {   // hold
            final float hx = (float) vh; 
            final float hy = 1;
            
            left+=hx;
            gp.lineTo(left, hy);
        }
        
        {   // decay
            final float dx = (float) (srEnabled ? (vd*(1-vs)) : vd);
            final float dy = srEnabled ? (float) vs : 0;
            final float l= left;
            left+=dx;
            //gp.lineTo(left, dy);
            /*gp.curveTo(
                    l, dy-dx, // (1-dy)*0.25f,
                    l, dy,
                    
                    left, dy);*/
            gp.curveTo(
              l, (1-dy)*0.5f+dy,
              (left-l)*0.5f+l, dy,
              left, dy);
        }
        
        if (srEnabled)
        {   
            {   // sustain+(1-vr)+(1-vd)+
                final float sx = (float)(1+(1-vd*(1-vs))+(1-vr*vs)+(1-va)+(hEnabled?(1-vh):0));
                final float sy = (float) vs;
                
                left+=sx;
                gp.lineTo(left, sy);
            }
            
            {   // release
                final float rx = (float)(vr*vs);
                final float ry = 0;
                final float l = left;
                left+=rx;
                //gp.lineTo(left, ry);
                gp.curveTo(                        
                        l, left-l,
                        l, 0, 
                        left, ry);
            }
        }

        AffineTransform at = new AffineTransform();
        
        if (!inverse)
        {
           at.scale(1, -1);
           at.translate(0, -(getHeight()-1));
        }
        
        at.scale((getWidth()-1)/segments, getHeight()-1);

        gp.transform(at);

        g.setColor(getForeground());
        ((Graphics2D) g).draw(gp);
        
    }
    
    public void setAttack(double v)
    {
        v = bounded(v);
        if (va!=v)
        {
            this.va = v;
            repaint();
        }
    }
    
    public void setDecay(double v)
    {
        v = bounded(v);
        if (vd!=v)
        {
            this.vd = v;
            repaint();
        }
    }
    
    public void setHold(double v)
    {
        v = bounded(v);
        if (vh!=v)
        {
            this.vh = v;
            repaint();
        }
    }
    
    public void setSustain(double v)
    {
        v = bounded(v);
        if (vs!=v)
        {
            this.vs = v;
            repaint();
        }
    }
    
    public void setRelease(double v)
    {
        v = bounded(v);
        if (vr!=v)
        {
            this.vr = v;
            repaint();
        }
    }
    
    private double bounded(double v)
    {
        return Math.max(0, Math.min(v, 1.0d));
    }
    public void parameterMorphValueChanged( Event e )
    {
        // TODO Auto-generated method stub
        
    }
    public void parameterKnobAssignmentChanged( Event e )
    {
        // TODO Auto-generated method stub
        
    }
    public void parameterMorphAssignmentChanged( Event e )
    {
        // TODO Auto-generated method stub
        
    }
    public void parameterMidiCtrlAssignmentChanged( Event e )
    {
        // TODO Auto-generated method stub
        
    }
    
}
