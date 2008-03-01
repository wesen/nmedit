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
 */package net.sf.nmedit.jtheme.clavia.nordmodular;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.clavia.nordmodular.graphics.MultiEnvelope;
import net.sf.nmedit.jtheme.component.JTControlAdapter;
import net.sf.nmedit.jtheme.component.JTDisplay;
import net.sf.nmedit.jtheme.store2.BindParameter;

public class JTMultiEnvDisplay extends JTDisplay implements ChangeListener {

	/**
     * 
     */
    private static final long serialVersionUID = 9061406842045130579L;

    private MultiEnvelope multiEnv;

	private JTControlAdapter levelAdapter[];
	private JTControlAdapter timeAdapter[];
	private JTControlAdapter sustainAdapter;
	private JTControlAdapter curveAdapter;
	

	public JTMultiEnvDisplay(JTContext context) {
		super(context);

		multiEnv = new MultiEnvelope();
		
		// adapters themselves are created externally and set with the 
		// setAdapter method
		levelAdapter = new JTControlAdapter[4];		
		timeAdapter = new JTControlAdapter[5];
	}

    protected void paintComponent(Graphics g)
    {
        if (multiEnv.isModified())
        {
            multiEnv.setModified(false);
            setDoubleBufferNeedsUpdate();
        }
        
        super.paintComponentWithDoubleBuffer(g);
    }

	protected void paintDynamicLayer(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (multiEnv.getCurve() == 0){ 
			//lin curve type, draw the ref line
			int h = getHeight();
			int w = getWidth();
			Insets insets = getInsets();
			g.setColor(JTNM1Context.GRAPH_DISPLAY_LINE);
	        int y = (int)(insets.top+h*.45);
	        g.drawLine(insets.left, y, w-1, y);
		}
		g.setColor(getForeground());
		multiEnv.setBounds(0, 0, getWidth(), getHeight());				
		g.draw(multiEnv);
	}

	public int getCurve(){
		return multiEnv.getCurve();
	}
	
	public int getLevel(int seg) {
		return multiEnv.getLevel(seg+1);
	}

	
	public int getTime(int seg) {
		return multiEnv.getTime(seg+1);
	}
	
	
	public int getSustain() {
		return multiEnv.getSustainSeg();
	}

	public void setLevel(int seg, int value) {
		if (getLevel(seg) != value) {
			multiEnv.setLevel(seg+1, value);
			repaint();
		}
	}
	
	public void setTime(int seg, int value) {
		if (getTime(seg) != value) {
			multiEnv.setTime(seg+1, value);
			repaint();
		}
	}
	
	public void setSustain(int value) {
		if (getSustain() != value) {
			multiEnv.setSustainSeg(value);
			repaint();
		}
	}
	
	public void setCurve(int value) {
		if (getCurve() != value) {
			multiEnv.setCurve(value);
			repaint();
		}
	}
	
	
	public JTControlAdapter getLevelAdapter(int seg) {
		return levelAdapter[seg];
	}

	public JTControlAdapter getSustainAdapter() {
		return sustainAdapter;		
	}

	public JTControlAdapter getCurveAdapter() {
		return curveAdapter;		
	}
	
	public JTControlAdapter getTimeAdapter(int seg) {
		return timeAdapter[seg];
	}
	
	@BindParameter(name="l", count=4)
	public void setLevelAdapter(int seg, JTControlAdapter adapter) {
		JTControlAdapter oldAdapter = this.levelAdapter[seg];

		if (oldAdapter != adapter) {
			if (oldAdapter != null)
				oldAdapter.setChangeListener(null);
			this.levelAdapter[seg] = adapter;
			if (adapter != null)
				adapter.setChangeListener(this);

			updateLevel(seg);
		}
	}

    @BindParameter(name="t", count=5)
	public void setTimeAdapter(int seg, JTControlAdapter adapter) {
		JTControlAdapter oldAdapter = this.timeAdapter[seg];

		if (oldAdapter != adapter) {
			if (oldAdapter != null)
				oldAdapter.setChangeListener(null);
			this.timeAdapter[seg] = adapter;
			if (adapter != null)
				adapter.setChangeListener(this);

			updateTime(seg);
		}
	}	

    @BindParameter(name="sustain")
	public void setSustainAdapter(JTControlAdapter adapter) {
		JTControlAdapter oldAdapter = this.sustainAdapter;

		if (oldAdapter != adapter) {
			if (oldAdapter != null)
				oldAdapter.setChangeListener(null);
			this.sustainAdapter = adapter;
			if (adapter != null)
				adapter.setChangeListener(this);

			updateSustain();
		}
	}

    @BindParameter(name="curve")
	public void setCurveAdapter(JTControlAdapter adapter) {
		JTControlAdapter oldAdapter = this.curveAdapter;

		if (oldAdapter != adapter) {
			if (oldAdapter != null)
				oldAdapter.setChangeListener(null);
			this.curveAdapter = adapter;
			if (adapter != null)
				adapter.setChangeListener(this);

			updateCurve();
		}
	}
	
	protected void updateLevel(int seg) {		
		if (levelAdapter[seg] != null)
			setLevel(seg,levelAdapter[seg].getValue());
	}

	protected void updateTime(int seg) {
		if (timeAdapter[seg] != null)
			setTime(seg,timeAdapter[seg].getValue());
	}
	
	protected void updateSustain() {
		if (sustainAdapter != null)
			setSustain(sustainAdapter.getValue());
	}

	protected void updateCurve(){
		if (sustainAdapter != null)
			setCurve(curveAdapter.getValue());
	}
	
	public void stateChanged(ChangeEvent e) {
		for (int i = 0 ; i < levelAdapter.length ; i++)
		{
			if (e.getSource() == levelAdapter[i]) {
				updateLevel(i);
				return;
			}
		}
		
		for (int i = 0 ; i < timeAdapter.length ; i++)
		{
			if (e.getSource() == timeAdapter[i]) {
				updateTime(i);
				return;
			}
		}
				
		if (e.getSource() == sustainAdapter) {
			updateSustain();
			return;
		}	
		
		if (e.getSource() == curveAdapter) {
			updateCurve();
			return;
		}	
	}
}
