package org.nomad.patch.ui;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nomad.patch.Module;
import org.nomad.theme.ModuleComponent;
import org.nomad.theme.component.ModuleGuiTitleLabel;
import org.nomad.theme.component.NomadComponent;
import org.nomad.util.iterate.ComponentIterator;
import org.nomad.xml.dom.module.DModule;

public class ModuleUI extends NomadComponent implements ModuleComponent {

	public static class Metrics {

		public final static int WIDTH 		= 255;
		public final static int HEIGHT 		= 15;
		public final static int WIDTH_DIV_2 = 128;
		public final static int HEIGHT_DIV_2= 8;
		
		public static Point getPixelLocation(Module module) {
			return new Point(
					Metrics.getPixelX(module.getX()), 
					Metrics.getPixelY(module.getY()));
		}
		
		public static Point getGridLocation(ModuleUI module) {
			return new Point(
					Metrics.getGridX(module.getX()),
					Metrics.getGridY(module.getY()));
		}

	    public static int getPixelX(int gridX) { return gridX * Metrics.WIDTH; }
	    public static int getPixelY(int gridY) { return gridY * Metrics.HEIGHT; }
	    public static int getGridX(int pxX) { return pxX / Metrics.WIDTH; }
	    public static int getGridY(int pxY) { return pxY / Metrics.HEIGHT; }
	    public static int getHeight(DModule info) {
	    	return info.getHeight() * Metrics.HEIGHT;
	    }
	}
	
	private final static Border border = BorderFactory.createRaisedBevelBorder();
	
	private ModuleGuiTitleLabel nameLabel = null;
	private Module module = null;
	private ModuleSectionUI moduleSection = null;

    JPopupMenu menu = new JPopupMenu();
    JMenuItem removeItem = new JMenuItem("Remove");
    
    private DModule info = null;
    private LocationChangedAction locationListener = null;


    public ModuleUI(DModule info) {
    	this.info = info;
		setBackground(UIManager.getColor("Button.background"));
		setOpaque(true);
		
		ModuleMouseAction mouse = new ModuleMouseAction(this);
		
    	addMouseListener(mouse);
    	addMouseMotionListener(mouse);
    	setBorder(border);

	    removeItem.addActionListener(new RemoveModule());
	    menu.add(removeItem);
	    
        setLayout(null);
    	
    	nameLabel = new ModuleGuiTitleLabel(info);
        nameLabel.setLocation(3,0);
        nameLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
        add(nameLabel);
        
        setFocusable(true);
        addMouseListener(new MouseAdapter(){
        	public void mousePressed(MouseEvent event) {
        		requestFocus();
        	}
        });
        
        setSize(Metrics.WIDTH, Metrics.getHeight(info));
        
        // TODO ?Why is the icon empty here?
        /*if (module.getDModule().getIcon() != null) {
        	NomadImageView imageView = new NomadImageView();
        	imageView.setDynamicOverlay(false); // otherwise it is cached and on the module
        	imageView.setLocation(3,3);
        	imageView.setImage( ( new ImageIcon(module.getDModule().getIcon()) ).getImage() );
        	add(imageView);
        }*/
    }
    
    public ModuleUI(Module module) {
    	this(module.getInfo());
    	setModule(module);
    }
	
	public void link(Module module) {
		for (Iterator<NomadComponent> iter=
			new ComponentIterator<NomadComponent>(NomadComponent.class, this);
		iter.hasNext();) {
			iter.next().link(module);
		}
	}
	
	public void unlink() {
		
		module.removeLocationChangeListener(locationListener);
		
		for (Iterator<NomadComponent> iter=
			new ComponentIterator<NomadComponent>(NomadComponent.class, this);
		iter.hasNext();) {
			iter.next().unlink();
		}
	}

	public void setModule(Module module) {
    	this.module = module;
    	nameLabel.setModule(module);

    	link(module);
		setLocation( Metrics.getPixelLocation(module) );

		
    	locationListener = new LocationChangedAction();
    	module.addLocationChangeListener(locationListener);
	}
    
	
    private class LocationChangedAction implements ChangeListener {

		public void stateChanged(ChangeEvent event) {
			if (module!=null)
				setLocation( Metrics.getPixelLocation(module) );
		}
    	
    }
    
    JPopupMenu getPopup() {
    	return menu;
    }

    public ModuleSectionUI getModuleSection() {
    	return moduleSection;
    }
    
    public void setModuleSectionUI(ModuleSectionUI moduleSection) {
    	this.moduleSection = moduleSection;
    }

	public Iterator getExportableNomadComponents() {
		return new Iterator() {
			int index = 0;

			public boolean hasNext() {
				if(index<getComponentCount()) {
					if (getComponent(index)==nameLabel) index++;
					return index<getComponentCount();
				}
				return false;
			}

			public Object next() {
				if (!hasNext()) throw new NoSuchElementException();
				return getComponent(index++);
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}};
	}
    
    public void setNameLabel(String name, int width) {
        nameLabel.setSize(width, 16);
    	nameLabel.setText(name);
    }
    
    public Module getModule() {
    	return module;
    }

	public DModule getModuleInfo() {
		return info;
	}
	
	private static class ModuleMouseAction extends MouseAdapter implements MouseMotionListener { 

//	  --- drag
		int dragX = 0, dragY = 0;
	    int oldModuleDragX = 0;
	    int oldModuleDragY = 0;
	
		private ModuleUI moduleUI;

		public ModuleMouseAction(ModuleUI moduleUI) {
			this.moduleUI = moduleUI;
		}
		
		public ModuleUI getModuleUI() {
			return moduleUI;
		}
		
		public ModuleSectionUI getParent() {
			return getModuleUI().getModuleSection();
		}

		public void mousePressed(MouseEvent e) {
			// Sadly enough, e.isPopupTrigger() does always return false
	//		System.out.println(e.isPopupTrigger()?"Popup":"Normal");		
	//		System.out.println(e.getButton());
			if (SwingUtilities.isLeftMouseButton(e)) {
		        dragX = e.getX();
		        dragY = e.getY();
		        oldModuleDragX = dragX;
		        oldModuleDragY = dragY;
		        getParent().setLayer(getModuleUI(), JLayeredPane.DRAG_LAYER.intValue());
		    }
		}
	
		public void mouseReleased(MouseEvent e) {
			if (!e.isPopupTrigger()) { 
				getParent().setLayer(getModuleUI(), JLayeredPane.DEFAULT_LAYER.intValue());

		    	Module m = getModuleUI().getModule();
		    	
		    	Point l = Metrics.getGridLocation(getModuleUI());
		    	
		    	if (getModuleUI().getX()%Metrics.WIDTH - Metrics.WIDTH_DIV_2 >= 0)
		    		l.x++;
		    	
		    	l.x = Math.max(0, l.x);
		    	
		    	m.setLocation(l);
		    	
		    	m.getModuleSection().rearangeModules(m);
		    } else {
		    	getModuleUI().getPopup().show(e.getComponent(), e.getX(), e.getY());
	        }
		}
	
		public void mouseDragged(MouseEvent e) {
			// Sadly enough, e.isPopupTrigger() or e.getButton() always return false or 0
	//		System.out.println(e.isPopupTrigger()?"Popup":"Normal");		
	//		System.out.println(e.getButton());
			if (SwingUtilities.isLeftMouseButton(e)) {
				getModuleUI().
	            setLocation(getModuleUI().getX() + (e.getX() - dragX), getModuleUI().getY() + (e.getY() - dragY));
	        }
		}
	
		public void mouseMoved(MouseEvent e) { }
	}
    class RemoveModule implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	getModule().getModuleSection().remove(getModule());
        }
    }

}
