package net.sf.nmedit.nomad.theme.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DParameter;
import net.sf.nmedit.nomad.util.graphics.BackgroundRenderer;
import net.sf.nmedit.nomad.util.graphics.LCDBackgroundRenderer;


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
	
	private ArrayList<VocoderBandChangeListener> 
		bandChangeListeners = new ArrayList<VocoderBandChangeListener>();
	private VocoderParameterLink paramLink = null;
	
	public VocoderBandDisplay() {
		super();
		setOpaque(true);
		setBackground(defaultBGColor);
		setForeground(defaultFGColor);
		setBands(0);
		setSize(NUM_BANDS*11, 60);

		LCDBackgroundRenderer lcd = new LCDBackgroundRenderer();
		lcd.randomizeBehaviour(getSize());
		setBackgroundRenderer(lcd);
		
		paramLink = new VocoderParameterLink(this);
	}

    public DParameter getBand1Spec() { return getDefinition(0); }
    public DParameter getBand2Spec() { return getDefinition(1); }
    public DParameter getBand3Spec() { return getDefinition(2); }
    public DParameter getBand4Spec() { return getDefinition(3); }
    public DParameter getBand5Spec() { return getDefinition(4); }
    public DParameter getBand6Spec() { return getDefinition(5); }
    public DParameter getBand7Spec() { return getDefinition(6); }
    public DParameter getBand8Spec() { return getDefinition(7); }
    public DParameter getBand9Spec() { return getDefinition(8); }
    public DParameter getBand10Spec() { return getDefinition(9); }
    public DParameter getBand11Spec() { return getDefinition(10); }
    public DParameter getBand12Spec() { return getDefinition(11); }
    public DParameter getBand13Spec() { return getDefinition(12); }
    public DParameter getBand14Spec() { return getDefinition(13); }
    public DParameter getBand15Spec() { return getDefinition(14); }
    public DParameter getBand16Spec() { return getDefinition(15); }

    public void setBand1Spec(DParameter p) {   setDefinition(0,p); }
    public void setBand2Spec(DParameter p) {   setDefinition(1,p); }
    public void setBand3Spec(DParameter p) {   setDefinition(2,p); }
    public void setBand4Spec(DParameter p) {   setDefinition(3,p); }
    public void setBand5Spec(DParameter p) {   setDefinition(4,p); }
    public void setBand6Spec(DParameter p) {   setDefinition(5,p); }
    public void setBand7Spec(DParameter p) {   setDefinition(6,p); }
    public void setBand8Spec(DParameter p) {   setDefinition(7,p); }
    public void setBand9Spec(DParameter p) {   setDefinition(8,p); }
    public void setBand10Spec(DParameter p) {   setDefinition(9,p); }
    public void setBand11Spec(DParameter p) {   setDefinition(10,p); }
    public void setBand12Spec(DParameter p) {   setDefinition(11,p); }
    public void setBand13Spec(DParameter p) {   setDefinition(12,p); }
    public void setBand14Spec(DParameter p) {   setDefinition(13,p); }
    public void setBand15Spec(DParameter p) {   setDefinition(14,p); }
    public void setBand16Spec(DParameter p) {   setDefinition(15,p); }
    
    public DParameter getDefinition(int band) {
        return getParameterInfo("parameter#"+band);
    }

    public void setDefinition(int band, DParameter p) {
        setParameterInfo("parameter#"+band, p);
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
		for (VocoderBandChangeListener l : bandChangeListeners)
			l.vocoderBandChanged(event);
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
		if (isValidBandIndex(band) && (vocoderBands[band]!=value)) {
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
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
		// paint
		int w = this.getWidth();
		int h = this.getHeight();
		
		if (w<=0||h<=0)
			return ;
		
		if (renderer!=null&&(w!=bgwidth||h!=bgheight)) {
			bgwidth = w;
			bgheight = h;
		}

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		if (renderer!=null) {
			// draw background
			renderer.drawTo(this, g);
		}
		else {
			g.setColor(getBackground());
			g.fillRect(0, 0, w, h);
		}
		
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		gcalc.update(w, h); // recalculate if necessary
		
		g.setColor(Color.WHITE); // should be different
		g.setColor(getBackground().brighter().brighter());
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

			double step = (double)w / (double)(NUM_BANDS);
			for (int i=0;i<NUM_BANDS;i++) {
				lx[i] = (int)((step*(double)(i+1))-(step/2.0));
			}
		}
		
		public void drawLine(Graphics g, int sourceBand, int targetBand) {
			if (isValidBandIndex(sourceBand)&& isValidBandIndex(targetBand))
				g.drawLine(lx[sourceBand], h, lx[targetBand], 0);
		}
		
	}

	public void link(Module module) {
		paramLink.link();
	}

	public void unlink() {
		paramLink.unlink();
	}
	
}
