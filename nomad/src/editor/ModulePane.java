package editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;

import nomad.gui.AbstractModuleGUI;
import nomad.gui.model.component.AbstractUIComponent;
import nomad.misc.FontInfo;
import nomad.model.descriptive.DModule;

public class ModulePane extends AbstractModuleGUI {
	private DModule module ;
	private ComponentSelectionListener csl = null;
	private ArrayList selectedComponents = new ArrayList();
	private JLabel title = null;
	private SelectionMotionHandler smh = null;
	
	public ModulePane(DModule module) {
		this.setLayout(null);
		this.module = module;
		title = new JLabel(module.getName());
		
		title.setSize(FontInfo.getTextRect(title.getText(), title.getFont(), title));
		title.setLocation(0,0);
		this.add(title);
		
		csl = new ComponentSelectionListener();
		addMouseListener(csl);
		addMouseMotionListener(csl);
		
		smh = new SelectionMotionHandler();
		
	}
	
	public ArrayList getSelectedComponents() {
		return selectedComponents;
	}
	
	public boolean isSelected(Component c) {
		for (int i=0;i<selectedComponents.size();i++)
			if (c==selectedComponents.get(i)) 
				return true;

		return false;
	}
	
	public Component add(Component c) {
		Component result = super.add(c);
		
		if (result!=title) {
			// we want to be notified, if components are dragged
			// se we can drag a whole selection
			result.addMouseListener(smh);
			result.addComponentListener(smh);
		}
		
		return result;
	}
	
	class SelectionMotionHandler implements ComponentListener, MouseListener {

		private Point oldLocation = null;
		Component fix = null;
		
		public void componentResized(ComponentEvent event) { }

		public void componentMoved(ComponentEvent event) {
			if (fix==event.getSource()) {
				
				if (isSelected(fix)) {
	
					Point delta = new Point(
							fix.getLocation().x-oldLocation.x,
							fix.getLocation().y-oldLocation.y);
					oldLocation = fix.getLocation();
					
					for (int i=0;i<selectedComponents.size();i++) {
						Component c = (Component) selectedComponents.get(i);
						if (c!=fix) {
							c.setLocation(
								c.getLocation().x+delta.x,
								c.getLocation().y+delta.y
							);
						}
					}
				}			
			}
			
			if (selectedComponents.size()>0)
				repaint();
		}

		public void componentShown(ComponentEvent event) { }

		public void componentHidden(ComponentEvent event) { }

		public void mouseClicked(MouseEvent event) { }

		public void mousePressed(MouseEvent event) {
			fix = ((Component)event.getSource());
			oldLocation = fix.getLocation();
		}

		public void mouseReleased(MouseEvent event) { 
			fix = null;
		}

		public void mouseEntered(MouseEvent event) { }

		public void mouseExited(MouseEvent event) { }
		
	}

	public DModule getModule() {
		return module;
	}
	
	public void initHack() {
		Iterator iter = getModuleComponents().getAllComponents();
		while (iter.hasNext()) {
			addExistingUIComponent((AbstractUIComponent)iter.next());
		}
	}


	private void addExistingUIComponent(AbstractUIComponent component) {
		new Draggable(component.getComponent());
	}
	
	public void addUIComponent(AbstractUIComponent component) {
		getModuleComponents().addComponent(component);
		addExistingUIComponent(component);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
				
		if (csl.rect!=null) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(csl.rect.x, csl.rect.y, csl.rect.width, csl.rect.height);
			g.setColor(Color.BLUE);
			g.drawRect(csl.rect.x, csl.rect.y, csl.rect.width, csl.rect.height);
		}
		Stroke old = null;

		if (g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D) g;
			old = g2.getStroke();
			float dash[] = {6.0f};
	        g2.setStroke(new BasicStroke(1.0f, 
	        	BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
	            6.0f, dash, 0.0f));
		}

		g.setColor(Color.RED);
		for (int i=0;i<selectedComponents.size();i++)
			paintComponentBounds(g, (Component) selectedComponents.get(i));

		if (g instanceof Graphics2D) {
			((Graphics2D) g).setStroke(old);
		}
	}

	protected void paintComponentBounds(Graphics g, Component c) {
		Rectangle r = c.getBounds();
		g.drawRect(r.x-1, r.y-1, r.width+1, r.height+1);
	}
	
	class ComponentSelectionListener implements MouseListener, MouseMotionListener {

		Point p1 = null;
		Point p2 = null;
		Rectangle rect = null;
		
		
		public void mouseClicked(MouseEvent event) {	}

		public void mousePressed(MouseEvent event) {
			p1 = event.getPoint();
			p2 = null;
			rect = null;
			setRectangularSelection(null);
		}

		public void mouseReleased(MouseEvent event) {
			rect = null;
			repaint();
		}

		public void mouseEntered(MouseEvent event) { }

		public void mouseExited(MouseEvent event) { }

		public void mouseDragged(MouseEvent event) {
			if (p1!=null) {
				p2 = event.getPoint();
				
				int rx = Math.min(p1.x, p2.x);
				int ry = Math.min(p1.y, p2.y);
				int rw = Math.abs(p1.x-p2.x);
				int rh = Math.abs(p1.y-p2.y);
				
				setRectangularSelection(new Rectangle(rx, ry, rw, rh));			
			}
		}

		public void mouseMoved(MouseEvent event) { }
		
		public void setRectangularSelection(Rectangle r) {
			rect = r;
			selectedComponents = new ArrayList();
			if (r!=null) {
				for (int i=0;i<getComponentCount();i++) {
					Component c = getComponent(i);
					if (c!=title)
						if (c.getBounds().intersects(r))
							selectedComponents.add(c);
				}
			}
			repaint();
		}
	}
	
}
