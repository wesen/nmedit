package nomad.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;

import nomad.graphics.BackgroundRenderer;
import nomad.gui.model.UIFactory;
import nomad.gui.model.component.AbstractUIComponent;
import nomad.misc.JPaintComponent;

public class AbstractModuleGUI extends JPaintComponent {

	private ModuleGUIComponents components = new ModuleGUIComponents();
	private ArrayList ownedComponents = new ArrayList();
	private UIFactory factory = null;
	private BackgroundRenderer renderer = null;
	
	public AbstractModuleGUI(UIFactory factory) {
		this.factory = factory;
		setBackground(Color.WHITE);
		setOpaque(true);
		setDoubleBuffered(false); // we have our own buffer
	}
	
	public BackgroundRenderer getBackgroundRenderer() {
		return renderer;
	}
	
	public void setBackgroundRenderer(BackgroundRenderer renderer) {
		this.renderer = renderer;
		repaint();
	}
	
	public void add(AbstractUIComponent component) {
		components.add(component);
		if (component.isDecoratingComponent()&&!factory.isEditing()) {
			ownedComponents.add(component);
		} else
			add(component.getComponent());
	}
    
	public void remove(AbstractUIComponent component) {
		components.remove(component);
		if (component.isDecoratingComponent()&&!factory.isEditing())
			ownedComponents.remove(component);
		else
			remove(component.getComponent());
	}
	
    public ModuleGUIComponents getModuleComponents() {
    	return components;
    }

    protected ArrayList getOwnedUIComponents() {
    	return ownedComponents;
    }
    
    public void drawOwnedComponents(Graphics g) {
    	for (int i=0;i<ownedComponents.size();i++) {
    		AbstractUIComponent ui = (AbstractUIComponent) ownedComponents.get(i);
    		Component c = ui.getComponent();
    			
    		Graphics cgraphics = g.create();
    		cgraphics.translate(c.getX(), c.getY());
    		c.paint(cgraphics);
    	}
    }
    
    public void paintBorder(Graphics g) {}
    
    public void paintBuffer(Graphics g) {
    	if (renderer!=null)
    		renderer.drawTo(this, g);
    	else {
    		g.setColor(getBackground());
    		g.fillRect(0, 0, getWidth(), getHeight());
    	}
    	drawOwnedComponents(g);
    	super.paintBorder(g);
    }
    
}
