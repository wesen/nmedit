package nomad.gui;

import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;

import nomad.gui.model.UIFactory;
import nomad.gui.model.component.AbstractUIComponent;
import nomad.misc.JPaintComponent;

public class AbstractModuleGUI extends JPaintComponent {

	private ModuleGUIComponents components = new ModuleGUIComponents();
	private ArrayList ownedComponents = new ArrayList();
	private UIFactory factory = null;
	
	public AbstractModuleGUI(UIFactory factory) {
		this.factory = factory;
		setOpaque(true);
		setDoubleBuffered(false); // we have our own buffer
	}
	
	public void add(AbstractUIComponent component) {
		components.add(component);
		if (component.isFixComponent()&&!factory.isEditing()) {
			ownedComponents.add(component);
		} else
			add(component.getComponent());
	}
    
	public void remove(AbstractUIComponent component) {
		components.remove(component);
		if (component.isFixComponent()&&!factory.isEditing())
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
    
}
