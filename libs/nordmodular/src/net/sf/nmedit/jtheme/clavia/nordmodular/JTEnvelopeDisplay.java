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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.JTControlAdapter;
import net.sf.nmedit.jtheme.component.JTDisplay;
import net.sf.nmedit.jtheme.store2.BindParameter;

/*
 * Created on Jul 24, 2006
 */

public class JTEnvelopeDisplay extends JTDisplay implements ChangeListener
{

    // adsr / ad / ahd
    
    /**
     * 
     */
    private static final long serialVersionUID = 6469770227264863413L;
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

    private JTControlAdapter attackAdapter;
    private JTControlAdapter attackTypeAdapter;
    private JTControlAdapter decayAdapter;
    private JTControlAdapter holdAdapter;
    private JTControlAdapter sustainAdapter;
    private JTControlAdapter releaseAdapter;
    private JTControlAdapter inverseAdapter;
    private boolean modified = true;

    public JTEnvelopeDisplay(JTContext context)
    {
        super(context);
        setAttack(1);
        setDecay(1);
        setHold(1);
        setSustain(0.5);
        setRelease(1);
        configure();
    }
    
    protected void setModified(boolean modified)
    {
        this.modified = modified;
    }
    
    protected void paintComponent(Graphics g)
    {
        if (modified)
        {
            modified = false;
            setDoubleBufferNeedsUpdate();
        }
        
        super.paintComponentWithDoubleBuffer(g);
    }
    
    protected void configure()
    {
        // no op
    }
    
    public JTControlAdapter getAttackAdapter()
    {
        return attackAdapter;
    }

    public JTControlAdapter getAttackTypeAdapter()
    {
        return attackTypeAdapter;
    }
    
    public JTControlAdapter getDecayAdapter()
    {
        return decayAdapter;
    }
    
    public JTControlAdapter getHoldAdapter()
    {
        return holdAdapter;
    }
    
    public JTControlAdapter getSustainAdapter()
    {
        return sustainAdapter;
    }
    
    public JTControlAdapter getReleaseAdapter()
    {
        return releaseAdapter;
    }
    
    public JTControlAdapter getInverseAdapter()
    {
        return inverseAdapter;
    }
    
    @BindParameter(name="attack")
    public void setAttackAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.attackAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.attackAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateAttack();
        }
    }

    @BindParameter(name="attack-type")
    public void setAttackTypeAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.attackTypeAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.attackTypeAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);

            updateAttackType();
        }
    }

    @BindParameter(name="decay")
    public void setDecayAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.decayAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.decayAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateDecay();
        }
    }

    @BindParameter(name="hold")
    public void setHoldAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.holdAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.holdAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateHold();
        }
    }

    @BindParameter(name="sustain")
    public void setSustainAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.sustainAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.sustainAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateSustain();
        }
    }

    @BindParameter(name="release")
    public void setReleaseAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.releaseAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.releaseAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateRelease();
        }
    }

    @BindParameter(name="inverse")
    public void setInverseAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.inverseAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.inverseAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateInverse();
        }
    }
    
    protected void updateAttack()
    {
        if (attackAdapter != null)
            setAttack(attackAdapter.getNormalizedValue());
    }
    
    protected void updateAttackType()
    {
        if (attackTypeAdapter != null)
            setAttackType(attackTypeAdapter.getValue());
    }
    
    protected void updateDecay()
    {
        if (decayAdapter != null)
            setDecay(decayAdapter.getNormalizedValue());
    }
    
    protected void updateHold()
    {
        if (holdAdapter != null)
            setHold(holdAdapter.getNormalizedValue());
    }
    
    protected void updateSustain()
    {
        if (sustainAdapter != null)
            setSustain(sustainAdapter.getNormalizedValue());
    }
    
    protected void updateRelease()
    {
        if (releaseAdapter != null)
            setRelease(releaseAdapter.getNormalizedValue());
    }
    
    protected void updateInverse()
    {
        if (inverseAdapter != null)
          setInverse(partitionValue(inverseAdapter,2) == 1);
    }

    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() == attackAdapter)
        {
            updateAttack();
            return;
        }
        if (e.getSource() == attackTypeAdapter)
        {
            updateAttackType();
            return;
        }
        if (e.getSource() == decayAdapter)
        {
            updateDecay();
            return;
        }
        if (e.getSource() == holdAdapter)
        {
            updateHold();
            return;
        }
        if (e.getSource() == sustainAdapter)
        {
            updateSustain();
            return;
        }
        if (e.getSource() == releaseAdapter)
        {
            updateRelease();
            return;
        }
        if (e.getSource() == inverseAdapter)
        {
            updateInverse();
            return;
        }
    }
    
    protected int partitionValue(JTControlAdapter adapter, int partitions)
    {
        return partitionValue(adapter.getValue(), adapter.getMinValue(), adapter.getMaxValue(), partitions);
    }
    
    protected int partitionValue(int value, int minValue, int maxValue, int partitions)
    {
        value -= minValue;
        maxValue -= minValue;
        
        if (maxValue == 0)
            return 0;
        
        return ((partitions -1)*value) / maxValue;
        
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
            setModified(true);
            attackType=t;
            repaint();
        }
    }
    
    public void setInverse(boolean e)
    {
        if (inverse!=e)
        {
            setModified(true);
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
            setModified(true);
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
            setModified(true);
            hEnabled = e;
            repaint();
        }
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

    
    public void paintDynamicLayer(Graphics2D g2)
    {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
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
        
        Insets insets = getInsets();
        int w = getWidth()-insets.left-insets.right;
        int h = getHeight()-insets.top-insets.bottom;
        
        if (!inverse)
        {
           at.scale(1, -1);
           at.translate(insets.left, -insets.top-(h-1));
        }
        
        at.scale((w-1)/segments, h-1);

        gp.transform(at);

        g2.setColor(getForeground());
        g2.draw(gp);
        
    }
    
    public void setAttack(double v)
    {
        v = bounded(v);
        if (va!=v)
        {
            setModified(true);
            this.va = v;
            repaint();
        }
    }
    
    public void setDecay(double v)
    {
        v = bounded(v);
        if (vd!=v)
        {
            setModified(true);
            this.vd = v;
            repaint();
        }
    }
    
    public void setHold(double v)
    {
        v = bounded(v);
        if (vh!=v)
        {
            setModified(true);
            this.vh = v;
            repaint();
        }
    }
    
    public void setSustain(double v)
    {
        v = bounded(v);
        if (vs!=v)
        {
            setModified(true);
            this.vs = v;
            repaint();
        }
    }
    
    public void setRelease(double v)
    {
        v = bounded(v);
        if (vr!=v)
        {
            setModified(true);
            this.vr = v;
            repaint();
        }
    }
    
    private double bounded(double v)
    {
        return Math.max(0, Math.min(v, 1.0d));
    }
    
}
