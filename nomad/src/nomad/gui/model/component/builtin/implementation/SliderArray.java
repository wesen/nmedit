package nomad.gui.model.component.builtin.implementation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JComponent;

import nomad.graphics.BackgroundRenderer;

public class SliderArray extends JComponent {

	private SimpleSlider[] sliders = new SimpleSlider[] {};
	private BackgroundRenderer renderer = null;
	private boolean modern = false;
	private Color barColor = Color.BLUE;

	public SliderArray() {
		setOpaque(false);
		setSliderCount(16);
	}
	
	public void setModern(Color barColor) {
		modern = true;
		this.barColor = barColor;
		for (int i=0;i<sliders.length;i++)
			sliders[i].useModernStyle(barColor);
	}
	
	public void setBackgroundRenderer(BackgroundRenderer renderer) {
		this.renderer = renderer;
		setOpaque(renderer!=null);
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		if (isOpaque()) {
			if (renderer!=null) {
				renderer.drawTo(this, this.getSize(), g);
				return;
			} 
		}
		super.paintComponent(g);
	}
	
	protected void doUnSetup(SimpleSlider[] sliders) {
		for (int i=0;i<sliders.length;i++)
			remove(sliders[i]);
	}

	protected void doSetup(SimpleSlider[] sliders) {
		if (sliders.length>0)
			setLayout(new GridLayout(0, sliders.length));
		
		for (int i=0;i<sliders.length;i++) {
			sliders[i].setOpaque(false);

			if (modern)
				sliders[i].useModernStyle(barColor);

			add(sliders[i]);
		}
	}
	
	public void setSliderCount(int count) {
		doUnSetup(sliders);
		sliders = new SimpleSlider[count];
		for (int i=0;i<sliders.length;i++)
			sliders[i] = new SimpleSlider();
		doSetup(sliders);
	}
	
	public SimpleSlider[] getSliders() {
		return sliders;
	}
	
}
