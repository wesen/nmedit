package org.nomad.theme.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import org.nomad.theme.property.ParameterProperty;
import org.nomad.util.graphics.BackgroundRenderer;
import org.nomad.xml.dom.module.DParameter;

/**
 * @author Christian Schneider
 * @hidden
 */
public class VocoderBandDisplay extends NomadComponent {

	public final static int NUM_BANDS = 16; // number of bands
	public final Color defaultFGColor = Color.decode("#00FF00");
	public final Color defaultBGColor = Color.decode("#008080");
	private int[] vocoderBands = new int[NUM_BANDS];
	private VocoderBandArrayGraphics gcalc = new VocoderBandArrayGraphics();
	private BackgroundRenderer renderer = null;
	private int bgwidth = 0;
	private int bgheight = 0;
	private Border border = BorderFactory.createLoweredBevelBorder();
	
	private ArrayList bandChangeListeners = new ArrayList();
	private DParameter[] bandsInfo = new DParameter[NUM_BANDS];
	
	public VocoderBandDisplay() {
		super();
		setBackground(defaultBGColor);
		setForeground(defaultFGColor);
		setBands(0);
		setPreferredSize(new Dimension(NUM_BANDS*11, 60));
	
		for (int i=0;i<NUM_BANDS;i++)
			getAccessibleProperties().add(new ParamBandProperty(this,i));
	}
	
	private class ParamBandProperty extends ParameterProperty {
		int band;
		public ParamBandProperty(NomadComponent component, int band) {
			super(component);
			setName("parameter#"+band);
			this.band = band;
		}
		public void setDParameter(DParameter p) { bandsInfo[band] = p; }
		public DParameter getDParameter() { return bandsInfo[band]; }
	}
	
	public void setBackgroundRenderer(BackgroundRenderer renderer) {
		if (this.renderer!=renderer) {
			this.renderer = renderer;
			repaint();
		}
	}
	
	public void addBandChangeListener(VocoderBandChangeListener listener) {
		if (!bandChangeListeners.contains(listener))
			bandChangeListeners.add(listener);
	}
	
	public void removeBandChangeListener(VocoderBandChangeListener listener) {
		if (bandChangeListeners.contains(listener))
			bandChangeListeners.remove(listener);
	}
	
	public void fireBandChangedEvent(VocoderBandChangeEvent event) {
		for (int i=0;i<bandChangeListeners.size();i++)
			((VocoderBandChangeListener)bandChangeListeners.get(i))
				.vocoderBandChanged(event);
	}

	protected void fireBandChangedEvent(int band) {
		fireBandChangedEvent(new VocoderBandChangeEvent(this, band));
	}

	protected void fireBandChangedEvent() {
		fireBandChangedEvent(new VocoderBandChangeEvent(this));
	}
	
	public int getBand(int band) {
		return isValidBandIndex(band) ? vocoderBands[band] : 0;
	}
	
	public void setBand(int band, int value) {
		if (isValidBandIndex(band)) {
			value = value % (NUM_BANDS+1);
			if (value<0)
				value = (NUM_BANDS-1)-value;
			
			vocoderBands[band]=value;
			bandsChanged();
			fireBandChangedEvent(band);
		}
	}
	
	public void setBandsRnd() {
		for (int i=0;i<NUM_BANDS;i++)
			vocoderBands[i] = 1+(int)(Math.random()*16);
		bandsChanged();
		fireBandChangedEvent();
	}
	
	public void setBandsInv() {
		for (int i=0;i<NUM_BANDS;i++)
			vocoderBands[i] = NUM_BANDS-i;
		bandsChanged();
		fireBandChangedEvent();
	}

	public void setBands(int offset) {
		for (int i=0;i<NUM_BANDS;i++) {
			vocoderBands[i] = i+1-offset;
			if (vocoderBands[i]<0 || vocoderBands[i]>NUM_BANDS)
				vocoderBands[i] = 0;
		}
		bandsChanged();
		fireBandChangedEvent();
	}
	
	protected void bandsChanged() {
		repaint();
	}
	
	public void paintDecoration(Graphics2D g) {
		// paint
		int w = this.getWidth();
		int h = this.getHeight();
		
		if (w<=0||h<=0)
			return ;
		
		if (renderer!=null&&(w!=bgwidth||h!=bgheight)) {
			bgwidth = w;
			bgheight = h;
		}
		
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		if (renderer!=null) {
			// draw background
			renderer.drawTo(this, g);
		}
		else {
			g.setColor(getBackground());
			g.fillRect(0, 0, w, h);
		}
		
		g.setColor(getForeground());
		gcalc.update(w, h); // recalculate if necessary
		
		for (int  i=0;i<NUM_BANDS;i++) {
			// check if band is enabled
			if (vocoderBands[i]>0) {
				// yes it is enabled -> draw line
				gcalc.drawLine(g, i, vocoderBands[i]-1);
			}
			
		}
		
		border.paintBorder(this, g, 0, 0, w, h);
		
	}

	
	public boolean isValidBandIndex(int index) {
		return 0<=index&&index<NUM_BANDS;
	}

	/* class that does the graphics related calculations */
	private class VocoderBandArrayGraphics {

		public int[] lx	= new int[NUM_BANDS]; // line x offset
		public int w = 0;
		public int h = 0;

		public void update(int w, int h) {
			if (w==0||h==0) {
				this.w = 0;
				this.h = 0;
				return;
			} else if (w==this.w && h==this.h) {
				return;
			}
			this.w = w;
			this.h = h;

			double step = (double)w / (double)(NUM_BANDS+1);
			for (int i=0;i<NUM_BANDS;i++) 
				lx[i] = (int)(step*(double)(i+1));
		}
		
		public void drawLine(Graphics g, int sourceBand, int targetBand) {
			if (isValidBandIndex(sourceBand)&& isValidBandIndex(targetBand))
				g.drawLine(lx[sourceBand], h, lx[targetBand], 0);
		}
		
	}

}
